package com.orpat.quickmix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.api.APIClient.gson
import com.orpat.quickmix.model.GetCustomer
import com.orpat.quickmix.model.RegisterDataClass
import com.orpat.quickmix.prefs
import kotlinx.android.synthetic.main.fragment_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_mobileno = "param1"


class RegistrationFragment : Fragment() {

    var mobileNo:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mobileNo = it.getString(ARG_mobileno)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mobile_no: String) =
                RegistrationFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_mobileno, mobile_no)
                    }
                }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        register_contact_ET.isEnabled = false
        register_contact_ET.isClickable = false
        register_contact_ET.setText(mobileNo)

        register_submit.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+.+[a-z]"
            val name = register_name_ET.text.toString().trim()
            val email = register_email_ET.text.toString().trim()

            when {
                name.isEmpty() -> {
                    (activity as MainActivity).callSnackBar("Name cannot be empty")
                }
                email.isEmpty() || !email.matches(emailPattern.toRegex()) -> {
                    (activity as MainActivity).callSnackBar("Email cannot be empty")

                }

               
                else -> {
                    registerUser(mobileNo!!, email, name)
                }
            }
        }
    }

    private fun registerUser(mobileNo: String, email: String, name: String) {
        APIClient.ApiAuthInterface().registerUser(mobileNo, email, name)
            .enqueue(object : Callback<RegisterDataClass> {
                override fun onResponse(
                    call: Call<RegisterDataClass>,
                    response: Response<RegisterDataClass>
                ) {
                    if (response.isSuccessful) {
                        (activity as MainActivity).callSnackBar(response.message())
                        val data = response.body()!!.data
                        prefs.isUserRegistered = true
                        prefs.token = data.getCustomer.token
                        val userString: String = gson.toJson(data.getCustomer)
                        prefs.userObject = userString

                        callAddressFragment()
                    }
                }

                override fun onFailure(call: Call<RegisterDataClass>, t: Throwable) {
                    println("ERROR" + t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }
    private fun callAddressFragment() {
        val fragment = AddressFragment()
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag, fragment)?.addToBackStack(
            "tag"
        )?.commit()

    }

    override fun onPause() {
        super.onPause()
    }
}