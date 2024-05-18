package com.orpat.quickmix.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orpat.quickmix.BuildConfig
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.activity.OrderSummaryActivity
import com.orpat.quickmix.activity.SuccessActivity
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.AddressDataClass
import com.orpat.quickmix.model.StateData
import com.orpat.quickmix.model.StateDataClass
import kotlinx.android.synthetic.main.fragment_address.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddressFragment : Fragment() {


    private var state: Int = 0
    private var city: Int = 0
    private var allStateList = ArrayList<StateData>()
    private var allCityList = ArrayList<StateData>()
    var adapterState: ArrayAdapter<StateData>? = null
    var adapterCity: ArrayAdapter<StateData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinners()
//        if(BuildConfig.DEBUG){
//            address1_ET.setText("Sunny Home")
//            address2_ET.setText("Bazar road")
//            pincode_ET.setText("400012")
//        }


        register_submit.setOnClickListener {
            val address1 = address1_ET.text.toString().trim()
            val address2 = address2_ET.text.toString().trim()
            val pincode = pincode_ET.text.toString().trim()

            when {
                address1.isEmpty() -> {
                    (activity as MainActivity).callSnackBar("Address cannot be empty")
                }
                else -> {
                    saveUserAddress(address1,address2,state,city,pincode)
                }
            }
        }
    }

    private fun callOrderSummary() {
        val intent = Intent(activity, OrderSummaryActivity::class.java)
        startActivity(intent)

//        val fragment = OrderSummaryFragment()
//        val fragmentManager = activity?.supportFragmentManager
//        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }

    private fun saveUserAddress(address1: String, address2: String, state_id: Int, city_id: Int, pincode: String) {
        APIClient.ApiAuthInterface().saveAddress(address1,address2,state_id,city_id,pincode)
                .enqueue(object : Callback<AddressDataClass> {
                    override fun onResponse(
                            call: Call<AddressDataClass>,
                            response: Response<AddressDataClass>
                    ) {
                        if (response.isSuccessful) {
                            (activity as MainActivity).callSnackBar(response.message())
                            callOrderSummary()
                        }
                    }

                    override fun onFailure(call: Call<AddressDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")

                    }
                })
    }


    private fun initSpinners() {
        getAllStatesData()

        adapterState = ArrayAdapter<StateData>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allStateList)
        StateSpinner.adapter = adapterState
        adapterState!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        StateSpinner.prompt = "Select State"

        adapterCity = ArrayAdapter<StateData>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allCityList)
        adapterCity!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        CitySpinner.adapter = adapterCity
        CitySpinner.prompt = "Select City"


        StateSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("onNothingSelected")
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
               (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                print(p2)
                println("STATE SELCETE "+allStateList.get(p2))
                state = allStateList[p2].id
                getAllCitiesData(allStateList[p2].id)
                println("STATE SELCETE "+state)
            }
        }

        CitySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("onNothingSelected")
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                print("CITY"+allCityList[p2].id)
                println(allCityList.get(p2).name)
                city = allCityList[p2].id
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