package eu.faircode.email.ui.leftmenu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import eu.faircode.email.R
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import eu.faircode.email.ui.leftmenu.menu.viewmodel.MenuLeftViewModel
import eu.faircode.email.utils.Constant.PRE_ROOM
import kotlinx.android.synthetic.main.drawer_view.*

class MainMenuLeftActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private val mainMenuViewModel by lazy { ViewModelProvider(this)[MenuLeftViewModel::class.java] }
    private val mainMenuAdapter by lazy { MainMenuAdapter(::onItemClick) }
    private val listMenuLeft: ArrayList<MenuLeftEntity> by lazy { arrayListOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_left)

        initData()
        initView()
        initHandles()
    }

    private fun initView() {
        rcv_menu_left_main.adapter = mainMenuAdapter

    }

    private fun initData() {

        preferences = getSharedPreferences(PRE_ROOM, MODE_PRIVATE)
        val isFirst = preferences.getBoolean(preferences_name, true)

        listMenuLeft.addAll(
                listOf(
                        MenuLeftEntity("Send", R.drawable.ic_menu_send),
                        MenuLeftEntity("Drafts", R.drawable.ic_menu_drafts),
                        MenuLeftEntity("Pins", R.drawable.ic_menu_pins),
                        MenuLeftEntity("Archive", R.drawable.ic_menu_archive),
                        MenuLeftEntity("Trash", R.drawable.ic_menu_trash)
                )
        )

        /*if (isFirst) {
            mainMenuViewModel.insertItemMenu(listMenuLeft)
            preferences.edit().putBoolean(preferences_name,false).apply()
        } else return*/

       /* mainMenuViewModel.readAllDataLive.observe(this){
            mainMenuAdapter.addItemData(it)
        }*/
    }

    private fun initHandles() {
        linear_edit_list.setOnClickListener {
            val intent = Intent(this,EditMenuLeftActivity::class.java)
            startActivity(intent)
        }
    }

    fun onItemClick(menuLeftEntity: MenuLeftEntity) {

    }

    companion object {
        const val preferences_name = "preference_room"
    }
}
