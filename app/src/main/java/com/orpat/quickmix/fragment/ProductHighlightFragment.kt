package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.AllProductsDataClass
import com.orpat.quickmix.model.Product
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product_highlight.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Product Highlights
 */
class ProductHighlightFragment : Fragment() {

    val productList = ArrayList<Product>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_highlight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getApiData()
        ph_buy_now.setOnClickListener {
            addToCart(productList[0].productId,1,0)
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
                        callFragment(ShoppingCartFragment())

                    }
                }

                override fun onFailure(call: Call<AllProductsDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().getAllProducts()
            .enqueue(object : Callback<AllProductsDataClass> {
                override fun onResponse(
                    call: Call<AllProductsDataClass>,
                    response: Response<AllProductsDataClass>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!.data
                        data.productList?.let{
                            productList.addAll(it)
                        }
                    }else{
                        Utils.showSnackbar(response.body()!!.message,drawer_layout)
                    }
                }

                override fun onFailure(call: Call<AllProductsDataClass>, t: Throwable) {
                    Utils.showSnackbar("Something went wrong!",drawer_layout)

                }
            })
    }

    private fun callFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }


}