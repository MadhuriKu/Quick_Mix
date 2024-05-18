package com.orpat.quickmix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.OrderSummaryViewModel
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.*
import com.orpat.quickmix.prefs
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.appbar_close.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class OrderSummaryActivity : AppCompatActivity() {
    private var couponCode: String? = null
    val decimalFormat = DecimalFormat(".00")

    val productList = ArrayList<OrderSummaryProductData>()
    lateinit var adapter : KRecyclerViewAdapter
    var checkoutId:Int = 0
    var paymentType:Int = 1 //1=online, 2=offline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        setupProductRv()
        getApiData()
    }

    private fun initView() {

        val gson = Gson()
        val json: String = prefs.userObject
        val userData: GetCustomer = gson.fromJson(json, GetCustomer::class.java)


        val userDetails = userData.mobileNo.toString() + "\n"+userData.email
        order_summary_text1.text = userData.name
        order_summary_text2.text = userDetails


        checkout_cod_order.setOnClickListener {
            paymentType = 2
            getPaymentLink()
        }

        checkout_order.setOnClickListener {
            callSnackBar("Payment Gateway will now open")
            paymentType = 1
            getPaymentLink()
        }

        add_coupon_code.setOnClickListener {
            val intent = Intent(this, DiscountCodeActivity::class.java)
            startActivityForResult(intent,200)
        }

        close_btn.setOnClickListener {
            finish()
        }
    }

    private fun getPaymentLink() {
        APIClient.ApiAuthInterface().processPayment(checkoutId,paymentType)
            .enqueue(object : Callback<PaymentLinkDataClass> {
                override fun onResponse(
                    call: Call<PaymentLinkDataClass>,
                    response: Response<PaymentLinkDataClass>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!.data
                        handlePayment(data)

                    }else{
                        callSnackBar(response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<PaymentLinkDataClass>, t: Throwable) {
                    callSnackBar("Something went wrong!")
                }
            })
    }

    private fun handlePayment(data: PaymentLinkData) {
        if(paymentType == 1 ){
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("PAYMENT_LINK",data.url)
            startActivity(intent)
        }else{
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("ORDER_ID","")
            startActivity(intent)
            finishAffinity()
//            val fragment = OrderConfirmationFragment()
//            val fragmentManager = activity?.supportFragmentManager
//            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
        }
    }


    private fun getApiData() {
        APIClient.ApiAuthInterface().orderSummary(couponCode)
            .enqueue(object : Callback<OrderSummaryDataClass> {
                override fun onResponse(
                    call: Call<OrderSummaryDataClass>,
                    response: Response<OrderSummaryDataClass>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            println(response.body()?.message)
                            initDataViews(it)
                        }

                        initView()
                    }else{
                       callSnackBar(response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<OrderSummaryDataClass>, t: Throwable) {
                    callSnackBar("Something went wrong!")
                }
            })
    }

    private fun initDataViews(data: OrderSummaryData) {
        productList.clear()
        productList.addAll(data.productList)
        productList.addAll(data.subProductList)
        adapter.notifyDataSetChanged()

        sc_total_count.text = getString(R.string.total_items_string,data.summary.totalQuantity)

        order_summary_text3.text = "ADDRESS : \n"+ data.shippingAddress.address

        checkoutId = data.checkoutId

        //Summary data
        val subTotalBeforeTax:String = decimalFormat.format(data.summary.grossPrice)
        sc_total_amt_text.text = getString(R.string.rupee__double_string,subTotalBeforeTax)

        data.summary.CGST?.let{
            CGSTView.visibility = View.VISIBLE
            cgst_amt_text.text = it
        }?: run{
            CGSTView.visibility = View.GONE
        }
        data.summary.SGST?.let{
            SGSTView.visibility = View.VISIBLE
            sgst_amt_text.text = it
        }?: run{
            SGSTView.visibility = View.GONE
        }

        data.summary.IGST?.let{
            osIGSTView.visibility = View.VISIBLE
            os_igst_amt_text.text = it
        }?: run{
            osIGSTView.visibility = View.GONE
        }

        val GSTPrice:String = decimalFormat.format(data.summary.GSTPrice)
        gst_amt_text.text = getString(R.string.rupee__double_string,GSTPrice)

        val totalPrice:String = decimalFormat.format(data.summary.netAmount)
        total_amt_text.text = getString(R.string.rupee__double_string,totalPrice)

        //Discount View
        data.couponDetails?.couponCode?.let {
            couponCodeView.visibility = View.VISIBLE
            couponCodDiscountView.visibility = View.VISIBLE
            coupon_code_text.text = data.couponDetails.couponCode
            val grossPriceWithDiscount:String = decimalFormat.format(data.summary.grossPriceWithDiscount)
            coupon_discount_amt_text.text = grossPriceWithDiscount

        } ?: run{
            couponCodeView.visibility = View.GONE
            couponCodDiscountView.visibility = View.GONE
        }



    }

    private fun setupProductRv() {
        adapter = KRecyclerViewAdapter(this, productList, object :
            KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cart_item_view, viewGroup, false)
                return OrderSummaryViewModel(view)
            }


            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, null)

        sc_product_rv?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        sc_product_rv?.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("coupon_code")
        couponCode = data?.extras?.getString("coupon_code",null)

        couponCode?.let{
            getApiData()
        }
    }

    fun callSnackBar(message:String){
        Utils.showSnackBar(message,this)
    }
}