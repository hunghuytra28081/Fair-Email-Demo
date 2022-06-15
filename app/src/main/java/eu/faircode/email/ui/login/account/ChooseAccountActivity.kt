package eu.faircode.email.ui.login.account

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import eu.faircode.email.BuildConfig
import eu.faircode.email.R
import eu.faircode.email.extension.addFragment
import eu.faircode.email.extension.replaceFragmentBackstack
import eu.faircode.email.ui.login.account.gmail.GmailFragment

class ChooseAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        addFragment(R.id.mainFrame, AccountLoginFragment.newInstance())
    }

    override fun onResume() {
        super.onResume()
        val lbm = LocalBroadcastManager.getInstance(this)
        val iff = IntentFilter()
        iff.addAction(ACTION_QUICK_GMAIL)
        iff.addAction(ACTION_QUICK_OAUTH)
        iff.addAction(ACTION_QUICK_SETUP)
        iff.addAction(ACTION_QUICK_POP3)
        iff.addAction(ACTION_VIEW_ACCOUNTS)
        iff.addAction(ACTION_VIEW_IDENTITIES)
        iff.addAction(ACTION_EDIT_ACCOUNT)
        iff.addAction(ACTION_EDIT_IDENTITY)
        iff.addAction(ACTION_MANAGE_LOCAL_CONTACTS)
        iff.addAction(ACTION_MANAGE_CERTIFICATES)
        iff.addAction(ACTION_IMPORT_CERTIFICATE)
        iff.addAction(ACTION_SETUP_MORE)
        lbm.registerReceiver(receiver, iff)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                when (intent?.action) {
                    ACTION_QUICK_GMAIL -> {
                        onGmail(intent)
                    }
                }
            }
        }
    }

    private fun onGmail(intent: Intent) {
        val fragment = GmailFragment()
        fragment.arguments = intent.extras
        replaceFragmentBackstack(R.id.mainFrame, fragment, "quick")
    }

//    private fun onOAuth(intent: Intent) {
//        val fragment = FragmentOAuth()
//        fragment.setArguments(intent.extras)
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("quick")
//        fragmentTransaction.commit()
//    }
//
//    private fun onQuickSetup(intent: Intent) {
//        val fragment = FragmentQuickSetup()
//        fragment.setArguments(Bundle())
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("quick")
//        fragmentTransaction.commit()
//    }
//
//    private fun onQuickPop3(intent: Intent) {
//        val fragment: FragmentBase = FragmentPop()
//        fragment.setArguments(Bundle())
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("account")
//        fragmentTransaction.commit()
//    }
//
//    private fun onViewAccounts(intent: Intent) {
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, FragmentAccounts())
//            .addToBackStack("accounts")
//        fragmentTransaction.commit()
//    }
//
//    private fun onViewIdentities(intent: Intent) {
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, FragmentIdentities())
//            .addToBackStack("identities")
//        fragmentTransaction.commit()
//    }
//
//    private fun onEditAccount(intent: Intent) {
//        val protocol = intent.getIntExtra("protocol", EntityAccount.TYPE_IMAP)
//        val fragment: FragmentBase
//        when (protocol) {
//            EntityAccount.TYPE_IMAP -> fragment = FragmentAccount()
//            EntityAccount.TYPE_POP -> fragment = FragmentPop()
//            else -> throw IllegalArgumentException("Unknown protocol=$protocol")
//        }
//        fragment.setArguments(intent.extras)
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("account")
//        fragmentTransaction.commit()
//    }
//
//    private fun onEditIdentity(intent: Intent) {
//        val fragment = FragmentIdentity()
//        fragment.setArguments(intent.extras)
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("identity")
//        fragmentTransaction.commit()
//    }
//
//    private fun onManageLocalContacts(intent: Intent) {
//        val args = Bundle()
//        // All accounts
//        args.putBoolean("junk", intent.getBooleanExtra("junk", false))
//        val fragment = FragmentContacts()
//        fragment.setArguments(args)
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("contacts")
//        fragmentTransaction.commit()
//    }
//
//    private fun onManageCertificates(intent: Intent) {
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_frame, FragmentCertificates())
//            .addToBackStack("certificates")
//        fragmentTransaction.commit()
//    }
//
//    private fun onImportCertificate(intent: Intent) {
//        val open = Intent(Intent.ACTION_GET_CONTENT)
//        open.addCategory(Intent.CATEGORY_OPENABLE)
//        open.type = "*/*"
//        if (open.resolveActivity(packageManager) == null) // system whitelisted
//            ToastEx.makeText(this, R.string.title_no_saf, Toast.LENGTH_LONG)
//                .show() else startActivityForResult(
//            ThemedSpinnerAdapter.Helper.getChooser(this, open),
//            eu.faircode.email.ActivitySetup.REQUEST_IMPORT_CERTIFICATE
//        )
//    }
//
//    private fun onSetupMore(intent: Intent) {
//        drawerLayout.openDrawer(GravityCompat.START)
//    }

    companion object {

        private const val KEY_ITERATIONS = 65536
        private const val KEY_LENGTH = 256

        const val REQUEST_SOUND_INBOUND = 1
        const val REQUEST_SOUND_OUTBOUND = 2
        const val REQUEST_EXPORT = 3
        const val REQUEST_IMPORT = 4
        const val REQUEST_CHOOSE_ACCOUNT = 5
        const val REQUEST_DONE = 6
        const val REQUEST_IMPORT_CERTIFICATE = 7
        const val REQUEST_OAUTH = 8
        const val REQUEST_STILL = 9
        const val REQUEST_DELETE_ACCOUNT = 10
        const val REQUEST_IMPORT_PROVIDERS = 11

        const val PI_MISC = 1

        const val ACTION_QUICK_GMAIL = BuildConfig.APPLICATION_ID + ".ACTION_QUICK_GMAIL"
        const val ACTION_QUICK_OAUTH = BuildConfig.APPLICATION_ID + ".ACTION_QUICK_OAUTH"
        const val ACTION_QUICK_SETUP = BuildConfig.APPLICATION_ID + ".ACTION_QUICK_SETUP"
        const val ACTION_QUICK_POP3 = BuildConfig.APPLICATION_ID + ".ACTION_QUICK_POP3"
        const val ACTION_VIEW_ACCOUNTS = BuildConfig.APPLICATION_ID + ".ACTION_VIEW_ACCOUNTS"
        const val ACTION_VIEW_IDENTITIES = BuildConfig.APPLICATION_ID + ".ACTION_VIEW_IDENTITIES"
        const val ACTION_EDIT_ACCOUNT = BuildConfig.APPLICATION_ID + ".EDIT_ACCOUNT"
        const val ACTION_EDIT_IDENTITY = BuildConfig.APPLICATION_ID + ".EDIT_IDENTITY"
        const val ACTION_MANAGE_LOCAL_CONTACTS = BuildConfig.APPLICATION_ID + ".MANAGE_LOCAL_CONTACTS"
        const val ACTION_MANAGE_CERTIFICATES = BuildConfig.APPLICATION_ID + ".MANAGE_CERTIFICATES"
        const val ACTION_IMPORT_CERTIFICATE = BuildConfig.APPLICATION_ID + ".IMPORT_CERTIFICATE"
        const val ACTION_SETUP_MORE = BuildConfig.APPLICATION_ID + ".SETUP_MORE"
    }
}