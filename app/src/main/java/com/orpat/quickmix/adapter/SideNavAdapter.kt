package com.orpat.quickmix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpat.quickmix.R
import com.orpat.quickmix.model.SideNavItem
import kotlinx.android.synthetic.main.item_side_nav.view.*

class SideNavAdapter(private val onItemClick: ((position: Int, item: SideNavItem) -> Unit)) :
    RecyclerView.Adapter<SideNavAdapter.SideNavVH>() {
    var menuItemsList = ArrayList<SideNavItem>()

    fun setNavItemsData( list: List<SideNavItem>){
        menuItemsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SideNavAdapter.SideNavVH {
        return SideNavVH(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_side_nav, viewGroup, false)
        )
    }

    inner class SideNavVH(inflate: View) : RecyclerView.ViewHolder(inflate) {
        fun setData(sideNavItem: SideNavItem) {
            itemView.setOnClickListener {
                sideNavItem.let {
                    onItemClick.invoke(adapterPosition, sideNavItem)
                }
            }
            itemView.tv_nav_text?.text = sideNavItem.itemName
        }
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }

    override fun onBindViewHolder(holder: SideNavVH, position: Int) {
        holder.setData(menuItemsList[position])
    }
}