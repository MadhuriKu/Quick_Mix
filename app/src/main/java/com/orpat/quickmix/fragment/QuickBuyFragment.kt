package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.kalpesh.krecyclerviewadapter.KRecyclerViewItemClickListener
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.QuickMixViewHolder
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.AllProductsDataClass
import com.orpat.quickmix.model.AttachmentImageScroll
import com.orpat.quickmix.model.Product
import com.orpat.quickmix.prefs
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_quick_buy.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * QuickBuyFragment
 *
 */
class QuickBuyFragment : Fragment() {


    val productList = ArrayList<Product>()
    val subProductList = ArrayList<Product>()
    lateinit var adapter : KRecyclerViewAdapter
    var isAccessories = 0
    var defaultQuantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quick_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("isTokenPresent"+ prefs.token)
        if (prefs.isTokenPresent) {
            println("yesssssssssssssssssss"+ prefs.isTokenPresent)
            getApiData()
        }else{
            println("nooooooooooooo"+ prefs.isTokenPresent)
        }
        setupAttachmentRv()


        quick_buy_product_iv.setOnClickListener {
            println("productListproductList"+productList[0])
            val fragment = QuickBuyDescFragment.newInstance(productList[0],isAccessories)
            callFragment(fragment)
        }

        quick_buy_now_btn.setOnClickListener{
            println("productIdproductId"+productList[0].productId)
                addToCart(productList[0].productId,defaultQuantity,isAccessories)
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
                            response.body()!!.data?.let {
                                productList.clear()
                                subProductList.clear()
                                it.productList?.let{ product ->
                                    println("productListproductList"+product[0])
                                    productList.addAll(product)
                                    println("productList"+productList[0])
                                }
                                it.subProductList?.let { subProduct ->
                                    subProductList.addAll(subProduct)
                                }


                                fillView()
                                adapter.notifyDataSetChanged()
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

    private fun fillView() {
        Glide.with(this).load(productList[0].image).fitCenter()
                .placeholder(R.drawable.attachment1).into(quick_buy_product_iv)
    }

    private fun setupAttachmentRv() {
        adapter = KRecyclerViewAdapter(this.requireContext(), subProductList, object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.quick_buy_item_1, viewGroup, false)
                return QuickMixViewHolder(view)
            }


            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, KRecyclerViewItemClickListener { _, _, p1 ->
            val currentProduct = subProductList[p1]
            val fragment = QuickBuyDescFragment.newInstance(currentProduct,1)
            callFragment(fragment)
        })
        quick_buy_attachment_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        quick_buy_attachment_rv?.adapter = adapter
    }

    private fun callFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()
        ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }

}