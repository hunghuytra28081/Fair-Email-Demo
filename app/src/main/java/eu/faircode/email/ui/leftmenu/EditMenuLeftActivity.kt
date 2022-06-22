package eu.faircode.email.ui.leftmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import eu.faircode.email.R
import eu.faircode.email.data.model.MenuLeft
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import eu.faircode.email.ui.leftmenu.menu.adapter.MenuLeftAdapter
import eu.faircode.email.ui.leftmenu.menu.viewmodel.MenuLeftViewModel
import eu.faircode.email.ui.leftmenu.moremenu.adapter.MoreMenuAdapter
import kotlinx.android.synthetic.main.activity_edit_left_menu.*

class EditMenuLeftActivity : AppCompatActivity() {

    private val menuAdapter by lazy { MenuLeftAdapter(:: onItemClickMenu) }
    private val moreMenuAdapter by lazy { MoreMenuAdapter(::onItemClickMoreMenu) }
    private val menuViewModel by lazy { ViewModelProvider(this)[MenuLeftViewModel::class.java] }
    private val listMoreMenu: ArrayList<MenuLeft> by lazy { arrayListOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_left_menu)

        initView()
        initData()
    }

    private fun initView() {
        rcv_list_menu.adapter = menuAdapter
        rcv_list_more_menu.adapter = moreMenuAdapter
    }

    private fun initData() {
        /*menuViewModel.readAllDataLive.observe(this){
            menuAdapter.addItemData(it)
        }*/

        listMoreMenu.addAll(
                listOf(
                        MenuLeft("Send", R.drawable.ic_menu_send),
                        MenuLeft("Drafts", R.drawable.ic_menu_drafts),
                        MenuLeft("Pins", R.drawable.ic_menu_pins),
                        MenuLeft("Archive", R.drawable.ic_menu_archive),
                        MenuLeft("Trash", R.drawable.ic_menu_trash),
                        MenuLeft("Snoozed", R.drawable.ic_menu_snoozed),
                        MenuLeft("Spam", R.drawable.ic_menu_spam),
                        MenuLeft("Recently Seen", R.drawable.ic_menu_recently),
                        MenuLeft("Shared", R.drawable.ic_menu_share),
                        MenuLeft("Shared Drafts", R.drawable.ic_menu_share),
                        MenuLeft("Delegated", R.drawable.ic_menu_delegated),
                        MenuLeft("Reminders", R.drawable.ic_menu_reminders)
                )
        )
    }

    private fun onItemClickMoreMenu(menuLeft: MenuLeft) {

    }

    private fun onItemClickMenu(menuLeftEntity: MenuLeftEntity) {

    }
}
