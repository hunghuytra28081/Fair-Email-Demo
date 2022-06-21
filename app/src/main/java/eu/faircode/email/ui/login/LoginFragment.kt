package eu.faircode.email.ui.login

import android.Manifest
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AuthenticatorException
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import eu.faircode.email.*
import eu.faircode.email.extension.customTextViewAgree
import eu.faircode.email.extension.isValidEmail
import eu.faircode.email.extension.setAnimationCloud
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Boolean
import java.util.*
import java.util.concurrent.TimeUnit

class LoginFragment : FragmentBase() {

    private val activity by lazy { requireActivity() as LoginActivity }
    private val constraintTerms by lazy { activity.constraintTerms }
    private val constraintPrivacy by lazy { activity.constraintPrivacy }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initHandles()
    }

    private fun initView() {
        img_cloud_login_middle.setAnimationCloud(1500)
        img_cloud_login_top.setAnimationCloud(2000)
        checkEmailButton()
    }

    private fun checkEmailButton() {
        edt_email.doOnTextChanged { text, start, before, count ->
            if (text!!.isValidEmail()) {
                layout_email.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
                btn_start.setBackgroundResource(R.drawable.bg_btn_email_correct)
            } else {
                layout_email.endIconDrawable = null
                btn_start.setBackgroundResource(R.drawable.bg_btn_email_wrong)
            }
        }
    }

    private fun initHandles() {
        customTextViewAgree(this, terms_and_privacy, constraintTerms, constraintPrivacy)

        google_sign_in.setOnClickListener {
            try {
                val intent = AccountManager.newChooseAccountIntent(
                        null,
                        null, arrayOf(GmailState.TYPE_GOOGLE),
                        false,
                        null,
                        null,
                        null,
                        null
                )
                val pm = requireContext().packageManager
                if (intent.resolveActivity(pm) == null) // system whitelisted
                    android.util.Log.e("GmailFragment", "newChooseAccountIntent unavailable")
                startActivityForResult(intent, ActivitySetup.REQUEST_CHOOSE_ACCOUNT)
            } catch (ex: Throwable) {
                if (ex is IllegalArgumentException)
                    Toast.makeText(requireContext(), ex.stackTraceToString(), Toast.LENGTH_SHORT).show()
                else
                    Log.formatThrowable(ex, false)
                Toast.makeText(requireContext(), ex.stackTraceToString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                ActivitySetup.REQUEST_CHOOSE_ACCOUNT ->
                    if (resultCode == Activity.RESULT_OK && data != null) onAccountSelected(data)
                    else data?.let { onNoAccountSelected(resultCode, it) }
                ActivitySetup.REQUEST_DONE -> finish()
            }
        } catch (ex: Throwable) {
            Log.e(ex)
        }
    }

    private fun onAccountSelected(data: Intent) {
        val name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        val handler: Handler = mainHandler
        val disabled = getString(R.string.title_setup_advanced_protection)
        var found = false
        val am = AccountManager.get(context?.applicationContext ?: requireContext())
        val accounts = am.getAccountsByType(type)
        for (account in accounts) if (name.equals(account.name, ignoreCase = true)) {
            found = true
            Log.i("Requesting token name=" + account.name)
            am.getAuthToken(
                    account,
                    ServiceAuthenticator.getAuthTokenType(type),
                    Bundle(),
                    activity,
                    AccountManagerCallback { future ->
                        try {
                            val bundle = future.getResult(GET_TOKEN_TIMEOUT, TimeUnit.MILLISECONDS)
                            require(!future.isCancelled) { "Android failed to return a token" }
                            val token = bundle.getString(AccountManager.KEY_AUTHTOKEN)
                                    ?: throw java.lang.IllegalArgumentException("Android returned no token")
                            Log.i("Got token name=" + account.name)
                            if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) return@AccountManagerCallback
                            name?.let { onAuthorized(it, token) }
//                            (parentFragment as FragmentBase).finish()
                        } catch (ex: Throwable) {
                            // android.accounts.OperationCanceledException = ServiceDisabled?
                            if (ex is AuthenticatorException && "ServiceDisabled" == ex.message) java.lang.IllegalArgumentException(disabled, ex)
                            Log.e(ex)
                            if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) return@AccountManagerCallback
                        }
                    },
                    handler)
            break
        }
        if (!found) {
            val permission = Helper.hasPermission(context, Manifest.permission.GET_ACCOUNTS)
            val crumb: MutableMap<String, String?> = HashMap()
            crumb["type"] = type
            crumb["count"] = accounts.size.toString()
            crumb["permission"] = Boolean.toString(permission)
            Log.breadcrumb("Gmail", crumb)
            Log.e("Account missing")
        }
    }

    private fun onNoAccountSelected(resultCode: Int, data: Intent) {
        val am = AccountManager.get(context?.applicationContext ?: requireContext())
        val accounts = am.getAccountsByType(GmailState.TYPE_GOOGLE)
        if (accounts.isEmpty()) Log.e("newChooseAccountIntent without result=$resultCode data=$data")
        if (resultCode == Activity.RESULT_OK) {
            ToastEx.makeText(context, android.R.string.ok, Toast.LENGTH_SHORT).show()
        } else ToastEx.makeText(context, android.R.string.cancel, Toast.LENGTH_SHORT).show()
    }

    private fun onAuthorized(user: String, token: String) {
        val state = GmailState.jsonDeserialize(token)
        val args = Bundle()
        args.putString("name", edt_email.text.toString().trim { it <= ' ' })
//        args.putBoolean("update", cbUpdate.isChecked)
        args.putString("user", user)
        args.putString("password", state.jsonSerializeString())
        object : SimpleTask<Void?>() {
            override fun onPreExecute(args: Bundle) {
//                edt_name.isEnabled = false
//                cbUpdate.setEnabled(false)
                google_sign_in.isEnabled = false
//                pb_select.visibility = View.VISIBLE
            }

            override fun onPostExecute(args: Bundle) {
//                edt_name.isEnabled = true
//                cbUpdate.setEnabled(true)
                google_sign_in.isEnabled = true
//                pb_select.visibility = View.GONE
            }

            @Throws(Throwable::class)
            override fun onExecute(context: Context, args: Bundle): Void? {
                var name = args.getString("name")
                val user = args.getString("user")
                val password = args.getString("password")

                // Safety checks
                require(Helper.EMAIL_ADDRESS.matcher(user).matches()) { context.getString(R.string.title_email_invalid, user) }
                require(!TextUtils.isEmpty(password)) { context.getString(R.string.title_no_password) }
                val cm = Helper.getSystemService(context, ConnectivityManager::class.java)
                val ani = cm?.activeNetworkInfo
                require(!(ani == null || !ani.isConnected)) { context.getString(R.string.title_no_internet) }
                val at = user!!.indexOf('@')
                val username = user.substring(0, at)
                val provider = EmailProvider
                        .fromDomain(context, "gmail.com", EmailProvider.Discover.ALL)[0]
                var folders: List<EntityFolder>
                val aprotocol = if (provider.imap.starttls) "imap" else "imaps"
                val aencryption = if (provider.imap.starttls) EmailService.ENCRYPTION_STARTTLS else EmailService.ENCRYPTION_SSL
                EmailService(
                        context, aprotocol, null, aencryption, false,
                        EmailService.PURPOSE_CHECK, true).use { iservice ->
                    iservice.connect(
                            provider.imap.host, provider.imap.port,
                            ServiceAuthenticator.AUTH_TYPE_GMAIL, null,
                            user, password,
                            null, null)
                    folders = iservice.folders
                }
                var max_size: Long
                val iprotocol = if (provider.smtp.starttls) "smtp" else "smtps"
                val iencryption = if (provider.smtp.starttls) EmailService.ENCRYPTION_STARTTLS else EmailService.ENCRYPTION_SSL
                EmailService(
                        context, iprotocol, null, iencryption, false,
                        EmailService.PURPOSE_CHECK, true).use { iservice ->
                    iservice.connect(
                            provider.smtp.host, provider.smtp.port,
                            ServiceAuthenticator.AUTH_TYPE_GMAIL, null,
                            user, password,
                            null, null)
                    max_size = iservice.maxSize
                }
                var update: EntityAccount? = null
                val db = DB.getInstance(context)
                try {
                    db.beginTransaction()
                    if (args.getBoolean("update")) {
                        val accounts = db.account().getAccounts(user,
                                EntityAccount.TYPE_IMAP, intArrayOf(ServiceAuthenticator.AUTH_TYPE_GMAIL, ServiceAuthenticator.AUTH_TYPE_PASSWORD))
                        if (accounts != null && accounts.size == 1) update = accounts[0]
                    }
                    if (update == null) {
                        val primary = db.account().primaryAccount

                        // Create account
                        val account = EntityAccount()
                        account.host = provider.imap.host
                        account.encryption = aencryption
                        account.port = provider.imap.port
                        account.auth_type = ServiceAuthenticator.AUTH_TYPE_GMAIL
                        account.user = user
                        account.password = password!!
                        account.name = provider.name + "/" + username
                        account.synchronize = true
                        account.primary = primary == null
                        account.created = Date().time
                        account.last_connected = account.created
                        account.id = db.account().insertAccount(account)
                        args.putLong("account", account.id)
                        EntityLog.log(context, "Gmail account=" + account.name)

                        // Create folders
                        for (folder in folders) {
                            val existing = db.folder().getFolderByName(account.id, folder.name)
                            if (existing == null) {
                                folder.account = account.id
                                folder.setSpecials(account)
                                folder.id = db.folder().insertFolder(folder)
                                EntityLog.log(context, "Gmail folder=" + folder.name + " type=" + folder.type)
                                if (folder.synchronize) EntityOperation.sync(context, folder.id, true)
                            }
                        }

                        // Set swipe left/right folder
                        for (folder in folders) if (EntityFolder.TRASH == folder.type) account.swipe_left = folder.id else if (EntityFolder.ARCHIVE == folder.type) account.swipe_right = folder.id
                        db.account().updateAccount(account)
                        if (TextUtils.isEmpty(name)) name = user.split("@").toTypedArray()[0]

                        // Create identity
                        val identity = EntityIdentity()
                        identity.name = name!!
                        identity.email = user
                        identity.account = account.id
                        identity.host = provider.smtp.host
                        identity.encryption = iencryption
                        identity.port = provider.smtp.port
                        identity.auth_type = ServiceAuthenticator.AUTH_TYPE_GMAIL
                        identity.user = user
                        identity.password = password
                        identity.synchronize = true
                        identity.primary = true
                        identity.max_size = max_size
                        identity.id = db.identity().insertIdentity(identity)
                        EntityLog.log(context, "Gmail identity=" + identity.name + " email=" + identity.email)
                    } else {
                        args.putLong("account", update.id)
                        EntityLog.log(context, "Gmail update account=" + update.name)
                        db.account().setAccountSynchronize(update.id, true)
                        db.account().setAccountPassword(update.id, password, ServiceAuthenticator.AUTH_TYPE_GMAIL)
                        db.identity().setIdentityPassword(update.id, update.user, password, update.auth_type, ServiceAuthenticator.AUTH_TYPE_GMAIL)
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
                if (update == null) ServiceSynchronize.eval(context, "Gmail") else {
                    args.putBoolean("updated", true)
                    ServiceSynchronize.reload(context, update.id, true, "Gmail")
                }
                return null
            }

            override fun onExecuted(args: Bundle?, data: Void?) {
                val updated = args?.getBoolean("updated")
                if (updated == true) {
                    finish()
                    ToastEx.makeText(requireContext(), R.string.title_setup_oauth_updated, Toast.LENGTH_LONG).show()
                }
//                else {
//                    val fragment = FragmentDialogAccount()
//                    fragment.arguments = args
//                    fragment.setTargetFragment(this., Constant.REQUEST_DONE)
//                    fragment.show(getActivityManager(), "quick:review")
//                }
            }

            override fun onException(args: Bundle, ex: Throwable) {
                Log.e(ex)
                if (ex is java.lang.IllegalArgumentException) /*tv_error.text =*/ Log.formatThrowable(ex, false)
                else /*tv_error.text =*/ Log.formatThrowable(ex, false)
//                grpError.visibility = View.VISIBLE
//                Handler(Looper.getMainLooper()).post(Runnable {
//                    if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) return@Runnable
//                    scroll.smoothScrollTo(0, tv_error.bottom)
//                })
            }
        }.execute(this, args, "setup:gmail")

    }

    companion object {
        private const val GET_TOKEN_TIMEOUT = 20 * 1000L
        fun newInstance() = LoginFragment()
    }
}
