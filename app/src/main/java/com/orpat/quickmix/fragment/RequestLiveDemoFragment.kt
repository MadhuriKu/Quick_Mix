package com.orpat.quickmix.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.activity.TimeSlotActivity
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.ResponseDataClass
import kotlinx.android.synthetic.main.fragment_request_live_demo.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RequestLiveDemoFragment : Fragment() {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var date:String? = null
    private var slotID:String?  = null
    private var selectedTimeSlotID:String?  = null
    private var mobileNumber:String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_live_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        ld_calendar_TV.text = selectedTimeSlotID
    }

    private fun initView() {

        ld_calendar_TV.text = selectedTimeSlotID

        ld_calendar_TV.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(
//                    requireContext(), DatePickerDialog.OnDateSetListener
//            { view, year, monthOfYear, dayOfMonth ->
//                ld_calendar_TV.setText("Date Selected :" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
//            }, year, month, day
//            )
//            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1
//            datePickerDialog.show()

//
//            var fragment = LiveDemoTimeSlotFragment()
//
//            val fragmentManager = activity?.supportFragmentManager
//            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()

            val intent = Intent(activity, TimeSlotActivity::class.java)
            startActivityForResult(intent,100)
        }

        ld_try_recipe.setOnClickListener {
            val fragment = MainRecipeListingFragment()
            val fragmentManager = activity?.supportFragmentManager
           fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()

        }

        ld_submit.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+.+[a-z]"
            val name = ld_name_ET.text.toString().trim()
            val email = ld_email_ET.text.toString().trim()
            val number = ld_contact_ET.text.toString().trim()

            when {
                name.isEmpty() -> {
                    (activity as MainActivity).callSnackBar("Name cannot be empty")
                }
                email.isEmpty() || !email.matches(emailPattern.toRegex()) -> {
                    (activity as MainActivity).callSnackBar("Email cannot be empty")

                }

                date.isNullOrEmpty() -> {
                    (activity as MainActivity).callSnackBar("Date cannot be empty")

                }

                slotID.isNullOrEmpty() -> {
                    (activity as MainActivity).callSnackBar("Time slot cannot be empty")

                }

                number.isEmpty() || number.length <= 9 -> {
                    (activity as MainActivity).callSnackBar("Mobile cannot be empty")
                }
                else -> {
                    mobileNumber = number
                    val jsonObject = JsonObject()
                    try {
                        jsonObject.addProperty("name", name)
                        jsonObject.addProperty("email", email)
                        jsonObject.addProperty("mobile_no", number)
                        jsonObject.addProperty("date", date)
                        jsonObject.addProperty("slot_id", slotID)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    scheduleDemo(name,email,number,date!!,slotID!!)
                }
            }
        }
    }

    private fun verifyMobileNumber(number: String) {
        val fragment = VerifyMobileFragment.newInstance(number,true)
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("slot_id")
        date = data?.extras?.getString("date",null)
        slotID = data?.extras?.getString("slot_id",null)
        selectedTimeSlotID = data?.extras?.getString("selected_time_slot",null)
    }

    private fun scheduleDemo(name: String, email: String, number: String, date: String, slotID: String) {
        APIClient.ApiAuthInterface().scheduleDemo(name,email,number,date!!,slotID!!)
                .enqueue(object : Callback<ResponseDataClass> {
                    override fun onResponse(
                            call: Call<ResponseDataClass>,
                            response: Response<ResponseDataClass>
                    ) {
                        if (response.isSuccessful) {
                            (activity as MainActivity).callSnackBar(response.body()!!.message)
                            verifyMobileNumber(mobileNumber!!)
                           // activity?.supportFragmentManager!!.popBackStack()
                        }
                    }

                    override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")

                    }
                })
    }

}