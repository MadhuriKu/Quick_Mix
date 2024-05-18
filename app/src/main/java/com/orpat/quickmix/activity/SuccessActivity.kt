package com.orpat.quickmix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orpat.quickmix.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val bundle = intent.extras
        val orderID = bundle?.getString("ORDER_ID")


        ty_desc.text =(getString(R.string.ty_string,orderID))
        ty_confirm_btn.setOnClickListener {
            startActivity(Intent(this@SuccessActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
}