package com.orpat.quickmix.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.kalpesh.krecyclerviewadapter.KRecyclerViewItemClickListener
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.OrderSummaryViewModel
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.*
import com.orpat.quickmix.prefs
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.CitySpinner
import kotlinx.android.synthetic.main.fragment_my_account.StateSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


/**
 * My Account and Order Summary.
 */
class MyAccountFragment : Fragment() {

    val orderList = ArrayList<OrderListingData>()
    lateinit var adapter : KRecyclerViewAdapter
    private var state: Int? = 0
    private var city: Int? = 0
    private var allStateList = ArrayList<StateData>()
    private var allCityList = ArrayList<StateData>()
    var adapterState: ArrayAdapter<StateData>? = null
    var adapterCity: ArrayAdapter<StateData>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gson = Gson()
        val json: String = prefs.userObject

        if(json.isEmpty()){
            no_orders_title.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            initEmptyAccountView()
            initEmptyOrderView()
        }else{
            val userData: GetCustomer = gson.fromJson(json, GetCustomer::class.java)
            val customerID = userData.id.toString()
            customerID?.let {
                userProfileData(it)
            }
            initAccountView()
            setupOrderListRv()
            orderListingData()
            initSpinners()
        }




        my_order_tab_btn.setOnClickListener {
            if(json.isNotEmpty()){
                initOrderView()
            }else{
                initEmptyOrderView()
            }
        }


        my_account_tab_btn.setOnClickListener {
            if(json.isNotEmpty())
                initAccountView()
            else{
                initEmptyAccountView()
            }
        }

        update_profile_btn.setOnClickListener {
            val address1 = edit_address1_ET.text.toString().trim()
            val address2 = edit_address2_ET.text.toString().trim()
            val pincode = edit_pincode_ET.text.toString().trim()

            when {
                address1.isEmpty() -> {
                    (activity as MainActivity).callSnackBar("Address cannot be empty")
                }
                else -> {
                    updateProfile(address1,address2,state.toString(),city.toString(),pincode)
                }
            }
        }

    }

    private fun updateProfile(address1: String, address2: String, cityName: String, stateName: String, pincode: String) {
        APIClient.ApiAuthInterface().editUserProfile(address1,address2,cityName,stateName,pincode)
                .enqueue(object : Callback<AddressDataClass> {
                    override fun onResponse(
                            call: Call<AddressDataClass>,
                            response: Response<AddressDataClass>
                    ) {
                        if (response.isSuccessful) {
                            (activity as MainActivity).callSnackBar(response.message())
                        }
                    }

                    override fun onFailure(call: Call<AddressDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")

                    }
                })
    }

    private fun initOrderView() {
        edit_profile_title.text = "My Orders"
        my_orders_layout.visibility =View.VISIBLE
        my_account_layout.visibility =View.GONE

        my_order_tab_btn.background = resources.getDrawable(R.drawable.ic_button_red_tab)
        my_account_tab_btn.background = resources.getDrawable(R.drawable.ic_button_black_tab)
    }

    private fun initAccountView() {
        edit_profile_title.text = "Edit Profile"
        my_orders_layout.visibility =View.GONE
        my_account_layout.visibility =View.VISIBLE

        my_account_tab_btn.background = resources.getDrawable(R.drawable.ic_button_red_tab)
        my_order_tab_btn.background = resources.getDrawable(R.drawable.ic_button_black_tab)
    }

    private fun initEmptyOrderView() {
        edit_profile_title.text = "No Orders Available"
        my_orders_layout.visibility =View.GONE
        my_account_layout.visibility =View.GONE

        my_order_tab_btn.background = resources.getDrawable(R.drawable.ic_button_red_tab)
        my_account_tab_btn.background = resources.getDrawable(R.drawable.ic_button_black_tab)
    }

    private fun initEmptyAccountView() {
        edit_profile_title.text = "Please Register"
        my_orders_layout.visibility =View.GONE
        my_account_layout.visibility =View.GONE

        my_account_tab_btn.background = resources.getDrawable(R.drawable.ic_button_red_tab)
        my_order_tab_btn.background = resources.getDrawable(R.drawable.ic_button_black_tab)
    }

    private fun setupOrderListRv() {
        adapter = KRecyclerViewAdapter(this.requireContext(), orderList, object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_list_item, viewGroup, false)
                return OrderSummaryViewModel(view)
            }
            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, KRecyclerViewItemClickListener { _, _, p1 ->
            println("ORDER ID "+orderList[p1].orderId.toString())
            val fragment = OrderDetailFragment.newInstance(orderList[p1].orderId)
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()

        })

        val decorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        order_list_rv.addItemDecoration(decorator)
        order_list_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        order_list_rv?.adapter = adapter
    }



    private fun orderListingData() {
        APIClient.ApiAuthInterface().orderListing()
                .enqueue(object : Callback<OrderListingDataClass> {
                    override fun onResponse(
                            call: Call<OrderListingDataClass>,
                            response: Response<OrderListingDataClass>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()!!.data
                            initOrderData(data)

                        }else{

                        }
                    }

                    override fun onFailure(call: Call<OrderListingDataClass>, t: Throwable) {
                        (activity as MainActivity).callSnackBar("Something went wrong!")
                    }
                })
    }

    private fun initOrderData(data: List<OrderListingData>) {
        orderList.clear()
        orderList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun userProfileData(customerID: String) {
        APIClient.ApiAuthInterface().getUserProfile(customerID)
            .enqueue(object : Callback<UserProfileDataClass> {
                override fun onResponse(
                    call: Call<UserProfileDataClass>,
                    response: Response<UserProfileDataClass>
                ) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        initUserData(response.body()?.data)
                    }
                }

                override fun onFailure(call: Call<UserProfileDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }

    private fun initUserData(data: UserProfileData?) {
        edit_name_ET.setText(data?.name)
        edit_email_ET.setText(data?.email)
        edit_contact_ET.setText(data?.mobileNo.toString())
        edit_address1_ET.setText(data?.address1.toString())
        edit_address2_ET.setText(data?.address2.toString())
        edit_pincode_ET.setText(data?.zipCode.toString())
        state = data?.stateDetails?.id
        city = data?.cityDetails?.id
        stateDisplay.text = data?.stateDetails?.name
        cityDisplay.text = data?.cityDetails?.name
    }

    private fun initSpinners() {
        getAllStatesData()

        adapterState = ArrayAdapter<StateData>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allStateList)
        StateSpinner.adapter = adapterState
        adapterState!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapterCity = ArrayAdapter<StateData>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allCityList)
        adapterCity!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        CitySpinner.adapter = adapterCity


        StateSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                try {
                    (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    state = allStateList[p2].id
                    getAllCitiesData(allStateList[p2].id)
                    stateDisplay.text =""
                }catch (e :Exception){}
            }
        }

        CitySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                try{
                    (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    println(allCityList.get(p2).name)
                    city = allCityList[p2].id
                    cityDisplay.text = ""
                }catch (e :Exception){

                }
            }
        }
    }

    private fun getAllCitiesData(id: Int) {
        APIClient.ApiAuthInterface().getCity(id)
            .enqueue(object : Callback<StateDataClass> {
                override fun onResponse(
                    call: Call<StateDataClass>,
                    response: Response<StateDataClass>
                ) {
                    if (response.isSuccessful) {
                        allCityList.clear()
                        allCityList.addAll(response.body()!!.data)
                        if(allCityList.size != 0)
                            city = allCityList[0].id

                        adapterCity?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<StateDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }

    private fun getAllStatesData() {
        APIClient.ApiAuthInterface().getState()
            .enqueue(object : Callback<StateDataClass> {
                override fun onResponse(
                    call: Call<StateDataClass>,
                    response: Response<StateDataClass>
                ) {
                    if (response.isSuccessful) {
                        allStateList.clear()
                        allStateList.addAll(response.body()!!.data)
                        adapterState?.notifyDataSetChanged()

                    }
                }

                override fun onFailure(call: Call<StateDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }
}