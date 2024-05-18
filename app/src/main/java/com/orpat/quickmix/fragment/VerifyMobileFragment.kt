package com.orpat.quickmix.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.api.APIClient.gson
import com.orpat.quickmix.model.ResponseDataClass
import com.orpat.quickmix.model.VerifyOtpDataClass
import com.orpat.quickmix.prefs
import kotlinx.android.synthetic.main.fragment_verify_mobile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_mobileno = "param1"
private const val IS_SCHEDULE_OTP = "is_schedule_otp"


class VerifyMobileFragment : Fragment() {
    private var mobileNo: String? = null
    private var isScheduleOtp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mobileNo = it.getString(ARG_mobileno)
            isScheduleOtp = it.getBoolean(IS_SCHEDULE_OTP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_mobile, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(mobile_no: String, is_schedule_otp: Boolean) =
            VerifyMobileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_mobileno, mobile_no)
                    putBoolean(IS_SCHEDULE_OTP,is_schedule_otp)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        verify_main_desc.text = "Please verify the code sent to your mobile number $mobileNo"
        val otp = otp_view.text.toString()

        otp_view.setOtpCompletionListener {
            val otp = otp_view.text.toString()
            if(otp.length == 4){
                val imm: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(otp_view?.windowToken, 0)
                if(isScheduleOtp){
                    mobileNo?.let {
                        scheduleDemoVerifyOtp(it,otp)
                    }
                }else{
                    mobileNo?.let{
                        verifyOtp(it,otp)
                    }
                }
            }
        }


        verifyOTP.setOnClickListener {
            val otp = otp_view.text.toString()
            when{
                (otp.isEmpty() || otp.length != 4) -> {
                    (activity as MainActivity).callSnackBar("OTP cannot be empty")
                }
                else -> {
                    if(isScheduleOtp){
                        mobileNo?.let {
                            scheduleDemoVerifyOtp(it,otp)
                        }
                    }else{
                        mobileNo?.let {
                            verifyOtp(it,otp)
                        }
                    }
                }
            }
        }
    }


    // After scheduling Demo OTP
    private fun scheduleDemoVerifyOtp(mobile_no: String, otp: String) {
        APIClient.ApiAuthInterface().verifyOtpToScheduleDemo(mobile_no, otp)
                .enqueue(object : Callback<ResponseDataClass> {
                    override fun onResponse(
                            call: Call<ResponseDataClass>,
                            response: Response<ResponseDataClass>
                    ) {
                        if (response.isSuccessful) {
//                            response.body()?.data?.isDemoRegistered?.let {
//                                activity!!.supportFragmentManager.popBackStack("tag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            }
//                            (activity as MainActivity).callSnackBar(response.body()!!.message)

                            if(response.body()!!.message == "Wrong OTP entered"){
                                (activity as MainActivity).callSnackBar( response.body()!!.data.message)
                            }else{
                                activity!!.supportFragmentManager.popBackStack("tag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")

                    }
                })
    }

    // Login Confirmation OTP
    private fun verifyOtp(moblieno: String, otp: String) {
        APIClient.ApiAuthInterface().verifyOtp(moblieno, otp)
                .enqueue(object : Callback<VerifyOtpDataClass> {
                    override fun onResponse(
                            call: Call<VerifyOtpDataClass>,
                            response: Response<VerifyOtpDataClass>
                    ) {
                        if (response.isSuccessful) {
                            //(activity as MainActivity).callSnackBar( response.body()!!.data.message)
                            val data = response.body()!!.data.data
                            if(data.isUserRegistered){
                               //  Save data
                                data.getCustomer?.let {
                                        prefs.token = it.token
                                        prefs.isUserRegistered = data.isUserRegistered
                                        val userString: String = gson.toJson(data.getCustomer)
                                        prefs.userObject = userString

                                        // Go to Address
                                        callFragment(AddressFragment())
                                    } ?: run {
                                        (activity as MainActivity).callSnackBar(response.body()!!.data.message)
                                    }
                           }else{
                              // if(!data.getCustomer?.token.equals(null)){
                               if(response.body()!!.message == "Wrong OTP entered"){
                                   (activity as MainActivity).callSnackBar( response.body()!!.data.message)
                               }else{
                                   // Go to Registration
                                   callFragment(RegistrationFragment.newInstance(moblieno))
                               }
                           }
                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpDataClass>, t: Throwable) {
                        println("ERROR"+t.message)
                        (activity as MainActivity).callSnackBar("Something went wrong!")
                    }
                })
    }

    private fun callFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.
        replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }

}