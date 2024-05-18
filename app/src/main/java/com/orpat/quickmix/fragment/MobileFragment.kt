package com.orpat.quickmix.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.LoginDataClass
import kotlinx.android.synthetic.main.fragment_mobile.*
import kotlinx.android.synthetic.main.fragment_request_live_demo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [MobileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MobileFragment : Fragment() {

    lateinit var mobileNo:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mobile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        submit_mobile_btn.setOnClickListener {
            val number = mobile_ET.text.toString().trim()
            when {
                     number.isEmpty() || number.length <= 9 -> {
                     (activity as MainActivity).callSnackBar("Mobile cannot be empty")
                }
                else -> {
                    mobileNo = number
                    loginUser(mobileNo)
                }
            }
        }

        mobile_ET.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val number = mobile_ET.text.toString().trim()
                when {
                    number.isEmpty() || number.length <= 9 -> {
                        (activity as MainActivity).callSnackBar("Mobile cannot be empty")
                    }
                    else -> {
                        mobileNo = number
                        loginUser(mobileNo)
                    }
                }

                true
            }
            false
        })


    }

    private fun loginUser(number:String) {
        APIClient.ApiAuthInterface().loginUser(number)
            .enqueue(object : Callback<LoginDataClass> {
                override fun onResponse(
                    call: Call<LoginDataClass>,
                    response: Response<LoginDataClass>
                ) {
                    val imm: InputMethodManager =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(mobile_ET?.windowToken, 0)

                    if (response.isSuccessful) {
                            //New User
                            //take to OTP screen
                            (activity as MainActivity).callSnackBar(response.body()!!.data.message)
                            callOTP()

                    }
                }

                override fun onFailure(call: Call<LoginDataClass>, t: Throwable) {
                    println("ERROR"+t.message)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                }
            })
    }



    private fun callOTP() {
        val fragment = VerifyMobileFragment.newInstance(mobileNo,false)
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()
    }


}