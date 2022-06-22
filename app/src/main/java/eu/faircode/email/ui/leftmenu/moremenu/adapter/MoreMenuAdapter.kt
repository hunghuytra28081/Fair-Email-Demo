package eu.faircode.email.ui.leftmenu.moremenu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.faircode.email.R
import eu.faircode.email.data.model.MenuLeft
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import kotlinx.android.synthetic.main.item_left_menu_delete.view.*

class MoreMenuAdapter(
        private val onItemClick: (MenuLeft) -> Unit
) : RecyclerView.Adapter<MoreMenuAdapter.MoreMenuViewHolder>() {

    private var listMenuDelete = ArrayList<MenuLeft>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_more_menu, parent, false)
        return MoreMenuViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MoreMenuViewHolder, position: Int) {
        holder.onBind(listMenuDelete[position])
    }

    override fun getItemCount(): Int = listMenuDelete.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(menuLeft: List<MenuLeft>) {
        this.listMenuDelete.clear()
        this.listMenuDelete.addAll(menuLeft)
        notifyDataSetChanged()
    }

    class MoreMenuViewHolder(
            itemView: View,
            private val onItemClick: (MenuLeft) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun onBind(entity: MenuLeft){
            with(itemView){
                img_icon_item_left_menu.setImageResource(entity.image)
                tv_name_item_left_menu.text = entity.name

                setOnClickListener {
                    onItemClick(entity)
                }
            }
        }
    }
}
