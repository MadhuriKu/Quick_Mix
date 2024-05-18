package com.orpat.quickmix.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.kalpesh.krecyclerviewadapter.KRecyclerViewItemClickListener
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.QuickMixViewHolder
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.DiscountCouponData
import com.orpat.quickmix.model.DiscountCouponDataClass
import kotlinx.android.synthetic.main.activity_discount_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscountCodeActivity : AppCompatActivity() {
    private var couponAdapter: KRecyclerViewAdapter? = null
    private var couponList: ArrayList<DiscountCouponData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount_code)
        setupRv()
        getCoupons()

        cancel_btn.setOnClickListener {
            finish()
        }
    }

    private fun setupRv() {
        couponAdapter = KRecyclerViewAdapter(this, couponList, object :
            KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.coupon_item_view, viewGroup, false)
                return QuickMixViewHolder(view)
            }

            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }

        }, KRecyclerViewItemClickListener { _, _, p1 ->
            val intent = Intent()
            intent.putExtra("coupon_code",couponList[p1].couponCode)
            setResult(Activity.RESULT_OK,intent)
            finish()
        })


        discountRV?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        discountRV?.adapter = couponAdapter
    }
    private fun getCoupons() {
        APIClient.ApiAuthInterface().getCoupons()
            .enqueue(object : Callback<DiscountCouponDataClass> {
                override fun onResponse(
                    call: Call<DiscountCouponDataClass>,
                    response: Response<DiscountCouponDataClass>
                ) {
                    if (response.isSuccessful) {
                        couponList.clear()
                        couponList.addAll(response.body()!!.data)
                        couponAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<DiscountCouponDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                }
            })
    }
}