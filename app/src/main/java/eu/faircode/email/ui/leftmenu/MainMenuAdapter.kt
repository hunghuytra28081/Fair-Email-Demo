package eu.faircode.email.ui.leftmenu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.faircode.email.R
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import kotlinx.android.synthetic.main.item_left_menu_delete.view.*

class MainMenuAdapter(
        private val onItemClick: (MenuLeftEntity) -> Unit
) : RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder>() {

    private var listMenuDelete = ArrayList<MenuLeftEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_left_main, parent, false)
        return MainMenuViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        holder.onBind(listMenuDelete[position])
    }

    override fun getItemCount(): Int = listMenuDelete.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItemData(entity: List<MenuLeftEntity>) {
        this.listMenuDelete.clear()
        this.listMenuDelete.addAll(entity)
        notifyDataSetChanged()
    }

    class MainMenuViewHolder(
            itemView: View,
            private val onItemClick: (MenuLeftEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun onBind(entity: MenuLeftEntity) {
            with(itemView) {
                img_icon_item_left_menu.setImageResource(entity.image)
                tv_name_item_left_menu.text = entity.name

                setOnClickListener {
                    onItemClick(entity)
                }
            }
        }
    }
}