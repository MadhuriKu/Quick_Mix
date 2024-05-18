package com.orpat.quickmix.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.gson.JsonObject
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.prefs
import com.orpat.quickmix.utility.Utils
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnBoardingViewModel :ViewModel(){

    val forceMessage = MutableLiveData<ForceUpdateModel>()
    //api sign-in
    fun checkForceUpdate(appVersion: String) {
        val jsonObject = JsonObject()

        try {
            jsonObject.addProperty("version", appVersion)
            jsonObject.addProperty("platform", "android")
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        APIClient.ApiAuthTokenInterface().forceUpdate()
            .enqueue(object : Callback<ForceUpdateModel> {
                override fun onResponse(
                    call: Call<ForceUpdateModel>,
                    response: Response<ForceUpdateModel>
                ) {
                    forceMessage.postValue(
                        response.body()!!
                    )
                    println("response.body()"+ response.body()!!.status)
                }

                override fun onFailure(call: Call<ForceUpdateModel>, t: Throwable) {

                }
            })
    }

}