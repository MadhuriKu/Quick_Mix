package com.orpat.quickmix.activity


import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.MediaController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.orpat.quickmix.BuildConfig
import com.orpat.quickmix.R
import com.orpat.quickmix.model.OnBoardingViewModel
import com.orpat.quickmix.utility.Constants
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {


    private val model : OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val appVersion = BuildConfig.VERSION_NAME
        model.checkForceUpdate(appVersion)
        model.forceMessage.observe(this@SplashActivity) {
            println("it.data?.force_update"+it.message)
            if (it.data.force_update == false) {
                val i = Intent(this@SplashActivity, ForceUpdateActivity::class.java)
                i.putExtra("play_url", Constants.PLAYSTORE_URL)
                startActivity(i)
            } else {
                try {
                    playVideo()
                }catch (e:Exception){
                    splash_image.visibility = View.VISIBLE
                    imageSplashScreen()
                }
               // imageSplashScreen()
            }
        }
        /*try {
            playVideo()
        }catch (e:Exception){
            splash_image.visibility = View.VISIBLE
            imageSplashScreen()
        }*/
    }

    private fun imageSplashScreen() {
        Handler().postDelayed({
            showLandingScreen()
        }, 2000)
    }


    private fun showLandingScreen() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun playVideo() {
        val mediaCtrl = MediaController(this)
        mediaCtrl.visibility = View.GONE
        val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
        videoView.setVideoURI(video)
        videoView.setZOrderOnTop(false)
        videoView.setMediaController(mediaCtrl)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //set this BEFORE start playback
            videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        }
      //  videoView.requestFocus()
       // videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        videoView.start()

       imageSplashScreen()
    }
}

