package com.orpat.quickmix.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.orpat.quickmix.R
import com.orpat.quickmix.model.TimeSlotData
import kotlinx.android.synthetic.main.time_slot_menu_item.view.*

class TimeSLotViewHolder(var layoutView: View) : KRecyclerViewHolder(layoutView) {

    override fun setData(context: Context, itemObject: Any) {
        super.setData(context, itemObject)
        if (itemObject is TimeSlotData) {
            layoutView.item_time_slot.text = itemObject.startTime+" - " +itemObject.endTime
        }
    }

    override fun setSelected(context: Context, selected: Boolean) {
        super.setSelected(context, selected)
        if(selected){
            layoutView.item_time_slot.setBackgroundResource(R.drawable.red_background_outline)
            layoutView.item_time_slot.setTextColor(ContextCompat.getColor(context,R.color.theme_red))
        }else{
            layoutView.item_time_slot.setBackgroundResource(R.drawable.grey_background_outline)
            layoutView.item_time_slot.setTextColor(ContextCompat.getColor(context,R.color.grey_text_color))
        }
    }
}