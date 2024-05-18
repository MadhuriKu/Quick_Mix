package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.AllProductsDataClass
import com.orpat.quickmix.model.Product
import com.orpat.quickmix.model.SingleProductDetail
import com.orpat.quickmix.model.SingleProductDetailModel
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_quick_buy_desc.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val PRODUCT_DATA = "product_data"
private const val IS_ACCESSORIE = "is_accessorie"

class QuickBuyDescFragment : Fragment() {
    private lateinit var product: Product
    private var is_accessorie: Int = 0
    private var isAddToCart: Boolean = true



    companion object {
        fun newInstance(param1: Product,param2:Int) =
                QuickBuyDescFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(PRODUCT_DATA, param1)
                        putInt(IS_ACCESSORIE,param2)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(PRODUCT_DATA) as Product
            is_accessorie = it.getInt(IS_ACCESSORIE,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quick_buy_desc, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  getApiData()
        val price = product.price
        quick_buy_desc_price.text = getString(R.string.rupee_string,price)
        quick_buy_desc_description.text = product.description
        quick_buy_desc_description_title.text = product.productName
        Glide.with(this).load(product.image).fitCenter()
                .placeholder(R.drawable.attachment1).into(quick_buy_desc_iv)

        var itemCount = product.quantity
        sc_count_txt.text = itemCount.toString()

        sc_minus_btn.setOnClickListener {
            if(itemCount > 0 ){
                itemCount--
            }
            sc_count_txt.text = itemCount.toString()
        }

        sc_plus_btn.setOnClickListener {
            itemCount++
            sc_count_txt.text = itemCount.toString()
        }

        quick_buy_desc_add_to_cart_btn.setOnClickListener {
            isAddToCart = true
            if(itemCount != 0){
                val jsonObject = JsonObject()
                try {
                    jsonObject.addProperty("product_id", product.productId)
                    jsonObject.addProperty("is_acccessories", is_accessorie)
                    jsonObject.addProperty("quantity", itemCount)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                addToCart(product.productId,itemCount,is_accessorie)
            }else{
                (activity as MainActivity).callSnackBar("Please select quantity")
            }
        }

        quick_buy_desc_buy_now_btn.setOnClickListener {
            isAddToCart = false
            if(itemCount != 0){
                val jsonObject = JsonObject()
                try {
                    jsonObject.addProperty("product_id", product.productId)
                    jsonObject.addProperty("is_acccessories", is_accessorie)
                    jsonObject.addProperty("quantity", itemCount)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                addToCart(product.productId,itemCount,is_accessorie)
            }else{
                (activity as MainActivity).callSnackBar("Please select quantity")
            }
        }
    }

    private fun addToCart(productId: Int, quantity: Int, isAccessorie: Int) {
        APIClient.ApiAuthInterface().addItemToCart(productId, quantity, isAccessorie)
                .enqueue(object : Callback<AllProductsDataClass> {
                    override fun onResponse(
                            call: Call<AllProductsDataClass>,
                            response: Response<AllProductsDataClass>
                    ) {
                        if (response.isSuccessful) {
                            (activity as MainActivity).callSnackBar("Item Added to Cart!")
                            if (isAddToCart){
                                activity?.supportFragmentManager!!.popBackStack()
                            }else{
                                callFragment(ShoppingCartFragment())
                            }
                        }
                    }

                    override fun onFailure(call: Call<AllProductsDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")

                    }
                })
    }

    private fun getApiData() {
        var is_prod = 1
        if(is_accessorie == 1)
            is_prod = 0

        APIClient.ApiAuthInterface().getSingleProductDetails(product.productId,is_prod)
                .enqueue(object : Callback<SingleProductDetailModel> {
                    override fun onResponse(
                            call: Call<SingleProductDetailModel>,
                            response: Response<SingleProductDetailModel>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()!!.data
                            updateImage(data)

                        }else{
                        }
                    }

                    override fun onFailure(call: Call<SingleProductDetailModel>, t: Throwable) {
                        (activity as MainActivity).callSnackBar("Something went wrong!")
                    }
                })
    }

    private fun updateImage(data: SingleProductDetail) {
        Glide.with(this).load(data.image).fitCenter()
                .placeholder(R.drawable.attachment1).into(quick_buy_desc_iv)
    }

    private fun callFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }
}