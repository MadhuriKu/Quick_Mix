package com.orpat.quickmix.adapter

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.orpat.quickmix.R
import com.orpat.quickmix.model.Product
import kotlinx.android.synthetic.main.cart_item_view.view.*

class CartViewHolder(var layoutView: View, var mListener: UpdateCartInterface) : KRecyclerViewHolder(layoutView) {
    var updateCartInterface :UpdateCartInterface? = null

    override fun setData(context: Context, itemObject: Any) {
        super.setData(context, itemObject)
        if (itemObject is Product) {
            layoutView.sc_title.text = itemObject.productName
            layoutView.sc_title_desc.text = "Item: "+itemObject.itemId.toString()
            layoutView.sc_count_txt.text = itemObject.quantity.toString()
            layoutView.sc_price.text = context.resources.getString(R.string.rupee_string,itemObject.subTotal)

            if(itemObject.image != null)
                Glide.with(context).load(itemObject.image).fitCenter()
                        .placeholder(R.drawable.attachment1).into(layoutView.sc_iv)

            updateCartInterface = mListener

            layoutView.sc_minus_btn.setOnClickListener {
                if(itemObject.quantity > 0 ){
                    itemObject.quantity--
                }
                layoutView.sc_count_txt.text = itemObject.quantity.toString()
                updateCartInterface!!.updateCartValues(itemObject)
            }

            layoutView.sc_plus_btn.setOnClickListener {
                itemObject.quantity++
                layoutView.sc_count_txt.text = itemObject.quantity.toString()
                updateCartInterface!!.updateCartValues(itemObject)
            }

            layoutView.delete_item_btn.setOnClickListener {
                itemObject.quantity = 0
                updateCartInterface!!.updateCartValues(itemObject)
            }
        }
    }
}

interface UpdateCartInterface{
    fun updateCartValues(product :Product)
}