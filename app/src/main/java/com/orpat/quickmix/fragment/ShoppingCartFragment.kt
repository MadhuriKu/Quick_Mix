package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.CartViewHolder
import com.orpat.quickmix.adapter.UpdateCartInterface
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.AllProductsData
import com.orpat.quickmix.model.AllProductsDataClass
import com.orpat.quickmix.model.Product
import com.orpat.quickmix.prefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *
 * ShoppingCartFragment
 *
 */
class ShoppingCartFragment : Fragment(), UpdateCartInterface {

    private val productList = ArrayList<Product>()
    lateinit var adapter : KRecyclerViewAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductRv()
        getApiData()
        progressBar.visibility = View.VISIBLE

        checkout_order.setOnClickListener {
            if(prefs.isUserRegistered){
                callFragment(AddressFragment())
            }else{
                callFragment(MobileFragment())
            }
        }
    }

    private fun callFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.
        replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().getCartItems()
                .enqueue(object : Callback<AllProductsDataClass> {
                    override fun onResponse(
                            call: Call<AllProductsDataClass>,
                            response: Response<AllProductsDataClass>
                    ) {
                        progressBar.visibility = View.GONE

                        if (response.isSuccessful) {
                            cart_empty_string.visibility = View.GONE
                            main_view.visibility = View.VISIBLE
                            productList.clear()

                            val tempProdList = response.body()!!.data?.productList
                            val tempSubProdList = response.body()!!.data?.subProductList

                            if(tempProdList !=null || tempSubProdList !=null){
                                initCartData(response.body()!!.data)
                            }else{
                                cart_empty_string.visibility = View.VISIBLE
                                main_view.visibility = View.GONE
                            }

                        }else{
                            (activity as MainActivity).callSnackBar(response.message())
                        }
                    }

                    override fun onFailure(call: Call<AllProductsDataClass>, t: Throwable) {
                       // (activity as MainActivity).callSnackBar("Cart is empty!")
                        progressBar.visibility = View.GONE

                        cart_empty_string.visibility = View.VISIBLE
                        main_view.visibility = View.GONE
                    }
                })
    }

    private fun initCartData(data: AllProductsData) {
        //Adding product
        data.productList?.let{
            for (product in it){
                product.is_accessorie = 0
            }
            productList.addAll(data.productList)
        }


        //Adding sub-product
        data.subProductList?.let{
            for (subProduct in it){
                subProduct.is_accessorie = 1
            }
            productList.addAll(it)
        }


        adapter.notifyDataSetChanged()

        data.summary?.subtotal?.let {
            sc_total_amt_text.text = getString(R.string.rupee_string,it)

        }

        data.summary?.totalQuantity?.let {
            sc_total_count.text = getString(R.string.total_items_string,it)
        }

        //Update total and price
        //"Total : $totalQuantity Items"
    }


    private fun setupProductRv() {
         adapter = KRecyclerViewAdapter(this.requireContext(), productList, object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cart_item_view, viewGroup, false)
                return CartViewHolder(view,this@ShoppingCartFragment)
            }
            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, null)
        sc_product_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sc_product_rv?.adapter = adapter
    }

    override fun updateCartValues(product:Product) {
        addToCart(product.productId,product.quantity,product.is_accessorie)
    }

    private fun addToCart(productId: Int, quantity: Int, isAccessorie: Int) {
        APIClient.ApiAuthInterface().addItemToCart(productId, quantity, isAccessorie)
                .enqueue(object : Callback<AllProductsDataClass> {
                    override fun onResponse(
                            call: Call<AllProductsDataClass>,
                            response: Response<AllProductsDataClass>
                    ) {
                        println("SUCCESS")
                        if (response.isSuccessful) {
                            (activity as MainActivity).callSnackBar("Cart Updated")
                            productList.clear()
                            val tempProdList = response.body()!!.data?.productList
                            val tempSubProdList = response.body()!!.data?.subProductList

                            if(tempProdList !=null || tempSubProdList !=null){
                                initCartData(response.body()!!.data)
                            }else{
                                cart_empty_string.visibility = View.VISIBLE
                                main_view.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<AllProductsDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")
                    }
                })
    }
}