package com.orpat.quickmix.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.orpat.quickmix.R
import com.orpat.quickmix.model.ODItem
import com.orpat.quickmix.model.OrderListingData
import com.orpat.quickmix.model.OrderSummaryProductData
import kotlinx.android.synthetic.main.cart_item_view.view.*
import kotlinx.android.synthetic.main.order_list_item.view.*
import java.text.DecimalFormat

class OrderSummaryViewModel(var layoutView: View) : KRecyclerViewHolder(layoutView) {
    val decimalFormat = DecimalFormat(".00")

    override fun setData(context: Context, itemObject: Any) {
        super.setData(context, itemObject)

        //Order Summary
        if (itemObject is OrderSummaryProductData) {
            layoutView.sc_title.text = itemObject.productName
            layoutView.sc_title_desc.text = "Item: "+itemObject.itemId.toString()
            layoutView.sc_count_txt.text = itemObject.quantity.toString()
            val subTotal:String = decimalFormat.format(itemObject.subTotal)
            layoutView.sc_price.text = context.resources.getString(R.string.rupee__double_string,subTotal)

            if(itemObject.image != null)
                Glide.with(context).load(itemObject.image).fitCenter()
                        .placeholder(R.drawable.attachment1).into(layoutView.sc_iv)

            layoutView.sc_minus_btn.visibility = View.INVISIBLE
            layoutView.sc_plus_btn.visibility = View.INVISIBLE
            layoutView.delete_item_btn.visibility = View.INVISIBLE
        }
        //Order Detail
        if (itemObject is ODItem) {
            layoutView.sc_title.text = itemObject.name
            layoutView.sc_title_desc.text = "Item: "+itemObject.itemId.toString()
            layoutView.sc_count_txt.text = itemObject.quantity.toString()
            layoutView.sc_price.text = context.resources.getString(R.string.rupee_string,itemObject.subTotal)

            if(itemObject.image != null)
                Glide.with(context).load(itemObject.image).fitCenter()
                        .placeholder(R.drawable.attachment1).into(layoutView.sc_iv)

            layoutView.sc_minus_btn.visibility = View.INVISIBLE
            layoutView.sc_plus_btn.visibility = View.INVISIBLE
            layoutView.delete_item_btn.visibility = View.INVISIBLE
        }

        //Order Listing
        if (itemObject is OrderListingData) {
            val amount:String = decimalFormat.format(itemObject.amount)
            layoutView.ol_title.text = "OrderID #"+itemObject.orderId.toString()+"\nOrder Status : "+itemObject.status
            layoutView.ol_title_desc.text = "Ordered At :"+itemObject.createdAt
            layoutView.ol_price.text = "Price : "+ context.resources.getString(R.string.rupee__double_string,amount)
        }
    }
}