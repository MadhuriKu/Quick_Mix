package com.orpat.quickmix.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.orpat.quickmix.R
import com.orpat.quickmix.utility.Constants


class ForceUpdateActivity :  AppCompatActivity() {
    lateinit var updateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_force_update)

        updateBtn = findViewById(R.id.updateBtn)

        var play_url = intent.getStringExtra("play_url")
        if(play_url.isNullOrBlank())
            play_url = Constants.PLAYSTORE_URL

        play_url.let{

            updateBtn.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse(play_url)
                startActivity(intent)
            }
        }
    }

}