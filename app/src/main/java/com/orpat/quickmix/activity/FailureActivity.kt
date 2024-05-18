package com.orpat.quickmix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orpat.quickmix.R
import kotlinx.android.synthetic.main.activity_failure.*
import kotlinx.android.synthetic.main.activity_success.*

class FailureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_failure)
        ty_f_confirm_btn.setOnClickListener {
            startActivity(Intent(this@FailureActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
}