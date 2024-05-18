package com.orpat.quickmix.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.orpat.quickmix.R
import com.orpat.quickmix.model.*
import kotlinx.android.synthetic.main.about_item_view.view.*
import kotlinx.android.synthetic.main.cart_item_view.view.*
import kotlinx.android.synthetic.main.coupon_item_view.view.*
import kotlinx.android.synthetic.main.main_recipe_item_view.view.*
import kotlinx.android.synthetic.main.quick_buy_item_1.view.*
import kotlinx.android.synthetic.main.quick_buy_item_1.view.quick_buy_iv
import kotlinx.android.synthetic.main.quick_buy_product_view.view.*
import kotlinx.android.synthetic.main.receipe_list_item_view.view.*
import kotlinx.android.synthetic.main.receipe_list_item_view.view.calories_tv
import kotlinx.android.synthetic.main.receipe_list_item_view.view.difficulty_tv
import kotlinx.android.synthetic.main.receipe_list_item_view.view.yield_tv

class QuickMixViewHolder(var layoutView: View) : KRecyclerViewHolder(layoutView) {

    override fun setData(context: Context, itemObject: Any) {
        super.setData(context, itemObject)

        if (itemObject is Product) {
             layoutView.quick_buy_desc.text = itemObject.productName
            if(itemObject.image != null)
                 Glide.with(context).load(itemObject.image).fitCenter()
                    .placeholder(R.drawable.recipe_placeholder).into(layoutView.quick_buy_iv)
        }

        if (itemObject is ProductImageScroll) {
             layoutView.quick_buy_product_desc.text = itemObject.itemName
             layoutView.quick_buy_product_iv.setImageDrawable(itemObject.itemImage)
        }
        if (itemObject is ProductImageBuyScroll) {
             layoutView.sc_title.text = itemObject.itemName
             layoutView.sc_price.text = itemObject.itemPrice
             layoutView.sc_iv.setImageDrawable(itemObject.itemImage)
        }

        if (itemObject is AboutScroll) {
             layoutView.about_title.text = itemObject.itemName
             layoutView.about_desc.text = itemObject.itemSubString
        }
        if (itemObject is RecipeCategoryData) {
             layoutView.res_tv_1.text = itemObject.categoryName
        }
        if (itemObject is RecipeData) {
            Glide.with(context).load(itemObject.image).fitCenter()
                    .placeholder(R.drawable.recipe_placeholder).into(layoutView.receipe_image)
             layoutView.res_title.text = itemObject.name
             layoutView.calories_tv.text = itemObject.calories
             layoutView.duration_tv.text = itemObject.duration
             layoutView.difficulty_tv.text = itemObject.difficulty
             layoutView.yield_tv.text = itemObject.yield
        }

        if (itemObject is DiscountCouponData) {
             layoutView.coupon_title.text = itemObject.couponCode
             layoutView.coupon_name.text = itemObject.name
             layoutView.coupon_desc.text = itemObject.description
             layoutView.expiry_tv.text = itemObject.expiryDate
        }


    }
}