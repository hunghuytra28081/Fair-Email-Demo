package eu.faircode.email.ui.login.account

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import eu.faircode.email.*
import eu.faircode.email.Helper.hasValidFingerprint
import eu.faircode.email.Helper.viewFAQ

import kotlinx.android.synthetic.main.fragment_account_login.*


class AccountLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandles()

    }

    private fun initHandles() {
        img_google.setOnClickListener {
            val context = context
            val popupMenu = context?.let { contextPopup ->
                PopupMenuLifecycle(
                    contextPopup,
                    viewLifecycleOwner, img_google
                )
            }
            val menu: Menu = popupMenu!!.menu

            val res = context.resources
            val pkg = context.packageName

            var order = 1
            val gmail = getString(R.string.title_setup_oauth, getString(R.string.title_setup_gmail))
            var item = menu.add(Menu.FIRST, R.string.title_setup_gmail, order++, gmail)
            var resid = res.getIdentifier("provider_gmail", "drawable", pkg)
            if (resid != 0) item.setIcon(resid)

            for (provider in EmailProvider.loadProfiles(context))
                if (provider.oauth != null &&
                    (provider.oauth.enabled || BuildConfig.DEBUG) &&
                    !TextUtils.isEmpty(provider.oauth.clientId)
                ) {
                    item = menu
                        .add(
                            Menu.FIRST,
                            -1,
                            order++,
                            getString(R.string.title_setup_oauth, provider.description)
                        )
                        .setIntent(
                            Intent(ChooseAccountActivity.ACTION_QUICK_OAUTH)
                                .putExtra("id", provider.id)
                                .putExtra("name", provider.description)
                                .putExtra("privacy", provider.oauth.privacy)
                                .putExtra("askAccount", provider.oauth.askAccount)
                                .putExtra("askTenant", provider.oauth.askTenant())
                                .putExtra("pop", provider.pop != null)
                        )
                    resid = res.getIdentifier("provider_" + provider.id, "drawable", pkg)
                    if (resid != 0) item.setIcon(resid)
                }

            menu.add(Menu.NONE, R.string.title_setup_other, order++, R.string.title_setup_other)
                .setIcon(R.drawable.twotone_auto_fix_high_24)

            menu.add(Menu.NONE, R.string.title_setup_classic, order++, R.string.title_setup_classic)
                .setIcon(R.drawable.twotone_settings_24).isVisible = false

            val ss = SpannableString(getString(R.string.title_setup_pop3))
            ss.setSpan(RelativeSizeSpan(0.9f), 0, ss.length, 0)
            menu.add(Menu.NONE, R.string.title_setup_pop3, order++, ss)

            menu.add(Menu.NONE, R.string.menu_faq, order++, R.string.menu_faq)
                .setIcon(R.drawable.twotone_support_24).isVisible = false

            popupMenu.insertIcons(context)

            MenuCompat.setGroupDividerEnabled(menu, true)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                val lbm = LocalBroadcastManager.getInstance(context)
                val itemId = item.itemId
                when {
                    itemId == R.string.title_setup_gmail -> {
                        if (hasValidFingerprint(getContext()) || BuildConfig.DEBUG) lbm.sendBroadcast(
                            Intent(ChooseAccountActivity.ACTION_QUICK_GMAIL)
                        ) else AlertDialog.Builder(
                            context
                        )
                            .setIcon(R.drawable.twotone_info_24)
                            .setTitle(item.title)
                            .setMessage(R.string.title_setup_gmail_support)
                            .setNeutralButton(R.string.title_info,
                                DialogInterface.OnClickListener { dialog, which ->
                                    Helper.viewFAQ(
                                        getContext(),
                                        6
                                    )
                                })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                        return@OnMenuItemClickListener true
                    }
                    itemId == R.string.title_setup_other -> {
                        lbm.sendBroadcast(Intent(ChooseAccountActivity.ACTION_QUICK_SETUP))
                        return@OnMenuItemClickListener true
                    }

                    itemId == R.string.title_setup_pop3 -> {
                        lbm.sendBroadcast(Intent(ChooseAccountActivity.ACTION_QUICK_POP3))
                        return@OnMenuItemClickListener true
                    }
/*                    itemId == R.string.menu_faq -> {
                        Helper.view(
                            getContext(),
                            Helper.getSupportUri(
                                getContext(),
                                "Providers:support"
                            ),
                            false
                        )
                        return@OnMenuItemClickListener true
                    }*/
                    item.intent == null -> false
                    else -> {
                        if (hasValidFingerprint(getContext()) || BuildConfig.DEBUG) lbm.sendBroadcast(
                            item.intent
                        ) else AlertDialog.Builder(
                            context
                        )
                            .setIcon(R.drawable.twotone_info_24)
                            .setTitle(item.title)
                            .setMessage(R.string.title_setup_oauth_permission)
                            .setNeutralButton(R.string.title_info,
                                DialogInterface.OnClickListener { dialog, which ->
                                    viewFAQ(
                                        getContext(),
                                        147
                                    )
                                })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                        true
                    }
                }
            })

            popupMenu.show()
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        try {
            Log.i("GmailFragment", "Start intent=$intent request=$requestCode")
            super.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            Helper.reportNoViewer(context, intent, e)
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    
    companion object {
        fun newInstance() = AccountLoginFragment()
    }
}
