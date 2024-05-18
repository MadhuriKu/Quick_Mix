package com.orpat.quickmix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.OrderSummaryViewModel
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.*
import kotlinx.android.synthetic.main.fragment_order_confirmaion.*
import kotlinx.android.synthetic.main.fragment_order_confirmaion.CGSTView
import kotlinx.android.synthetic.main.fragment_order_confirmaion.SGSTView
import kotlinx.android.synthetic.main.fragment_order_confirmaion.cgst_amt_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.couponCodDiscountView
import kotlinx.android.synthetic.main.fragment_order_confirmaion.couponCodeView
import kotlinx.android.synthetic.main.fragment_order_confirmaion.coupon_code_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.coupon_discount_amt_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.gst_amt_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.order_summary_text1
import kotlinx.android.synthetic.main.fragment_order_confirmaion.order_summary_text2
import kotlinx.android.synthetic.main.fragment_order_confirmaion.order_summary_text3
import kotlinx.android.synthetic.main.fragment_order_confirmaion.sc_product_rv
import kotlinx.android.synthetic.main.fragment_order_confirmaion.sc_total_amt_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.sgst_amt_text
import kotlinx.android.synthetic.main.fragment_order_confirmaion.total_amt_text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


private const val ORDER_ID = "order_id"

class OrderDetailFragment : Fragment() {
    private var order_id: Int = 1
    val productList = ArrayList<ODItem>()
    lateinit var adapter : KRecyclerViewAdapter
    val decimalFormat = DecimalFormat(".00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            order_id = it.getInt(ORDER_ID)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
                OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ORDER_ID, param1)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_confirmaion, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductRv()
        getApiData()
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().orderDetails(order_id)
            .enqueue(object : Callback<OrderDetailDataClass> {
                override fun onResponse(
                    call: Call<OrderDetailDataClass>,
                    response: Response<OrderDetailDataClass>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            initDataViews(it)
                        }

                    }else{
                        (activity as MainActivity).callSnackBar(response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<OrderDetailDataClass>, t: Throwable) {
                    (activity as MainActivity).callSnackBar("Something went wrong!")
                }
            })
    }

    private fun initDataViews(data: OrderDetailData) {
        productList.clear()
        productList.addAll(data.items)
        adapter.notifyDataSetChanged()

        data.amount?.let {


        //amount data
        val subTotalBeforeTax:String = decimalFormat.format(data.amount.grossPrice)
        sc_total_amt_text.text = getString(R.string.rupee__double_string,subTotalBeforeTax)

        data.amount.IGST?.let{
            IGSTView.visibility = View.VISIBLE
            igst_amt_text.text = it
        }?: run{
            IGSTView.visibility = View.GONE
        }

        data.amount.CGST?.let{
            CGSTView.visibility = View.VISIBLE
            cgst_amt_text.text = it
        }?: run{
            CGSTView.visibility = View.GONE
        }
        data.amount.SGST?.let{
            SGSTView.visibility = View.VISIBLE
            sgst_amt_text.text = it
        }?: run{
            SGSTView.visibility = View.GONE
        }

        val GSTPrice:String = decimalFormat.format(data.amount.GSTPrice)
        gst_amt_text.text = getString(R.string.rupee__double_string,GSTPrice)

        val totalPrice:String = decimalFormat.format(data.amount.netAmount)
        total_amt_text.text = getString(R.string.rupee__double_string,totalPrice)

        //Discount View
        data.isCouponApplied?.let {
            if(it == 0){
                couponCodeView.visibility = View.GONE
                couponCodDiscountView.visibility = View.GONE
            }else{
                couponCodeView.visibility =View.VISIBLE
                couponCodDiscountView.visibility =View.VISIBLE
                coupon_code_text.setText(data.couponCodeId?.couponCode)
                val grossPriceWithDiscount:String = decimalFormat.format(data.amount?.grossPriceWithDiscount)
                coupon_discount_amt_text.text = grossPriceWithDiscount
            }
        } ?: run{
            couponCodeView.visibility = View.GONE
            couponCodDiscountView.visibility = View.GONE
        }

        }
        order_summary_text1.text = "ORDER ID :"+data.orderId
        order_summary_text2.text = "Order Status : "+data.status
        order_summary_text3.text = data.shippingAddress.address
    }

    private fun setupProductRv() {
        adapter = KRecyclerViewAdapter(this.requireContext(), productList, object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cart_item_view, viewGroup, false)
                return OrderSummaryViewModel(view)
            }


            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, null)

        sc_product_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sc_product_rv?.adapter = adapter
    }

}