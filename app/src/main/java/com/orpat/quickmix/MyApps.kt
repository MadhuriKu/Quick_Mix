package com.orpat.quickmix

import com.orpat.quickmix.utility.LocalStorage



import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen


val prefs: LocalStorage by lazy {
    MyApp.prefs!!
}

class MyApp : Application() {



    override fun onCreate() {


        prefs = LocalStorage(applicationContext)
        super.onCreate()
        instance = this

        AndroidThreeTen.init(this);


    }


    companion object {
        private val TAG = MyApp::class.java.simpleName
        @get:Synchronized var instance: MyApp? = null
            private set

        var prefs: LocalStorage? = null

        fun sharedContext(): Context? {
            return instance?.applicationContext
        }
    }
}