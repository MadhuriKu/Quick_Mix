package com.orpat.quickmix.model

import android.graphics.drawable.Drawable

data class SideNavItem(
    var id:Int,
    var itemName:String)

data class AttachmentImageScroll(
    var id:Int,
    var itemName:String,
    var itemImage:Drawable
)
data class ProductImageScroll(
    var id:Int,
    var itemName:String,
    var itemImage:Drawable
)

data class ProductImageBuyScroll(
    var id:Int,
    var itemName:String,
    var itemImage:Drawable,
    var itemPrice:String
)
data class AboutScroll(
    var id:Int,
    var itemName:String,
    var itemSubString:String
)