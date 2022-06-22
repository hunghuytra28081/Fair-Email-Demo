package eu.faircode.email.ui.leftmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import eu.faircode.email.R
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import eu.faircode.email.ui.leftmenu.menu.adapter.MenuLeftAdapter
import eu.faircode.email.ui.leftmenu.menu.viewmodel.MenuLeftViewModel

class EditMenuLeftActivity : AppCompatActivity() {

    private val menuAdapter by lazy { MenuLeftAdapter(:: onItemClick) }
    private val menuViewModel by lazy { ViewModelProvider(this)[MenuLeftViewModel::class.java] }
    private val listMenuLeft: ArrayList<MenuLeftEntity> by lazy { arrayListOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_left_menu)
    }

    private fun onItemClick(menuLeftEntity: MenuLeftEntity) {

    }
}
