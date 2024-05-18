package com.orpat.quickmix.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.orpat.quickmix.BuildConfig
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.utility.Constants
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.appbar_close.*


//CCAVANUE
//Category - HDFC Credit Card:
//
//Success card Details -
//Card No: 4012 0010 3714 1112
//CVV: 123
//Expr. Date: 12/2022
//Name-Test
//
//OTP - 123456
//
//UAT Account ID : 394956

class WebViewActivity : AppCompatActivity() {
    var link:String? = ""
    var url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val bundle = intent.extras
        val s_url = bundle!!.getString("PAYMENT_LINK")
        url = s_url!!
        link = Uri.parse(s_url).toString()
        startWebView(link!!)

        wb.setInitialScale(1)
        wb.getSettings().setLoadWithOverviewMode(true)
        wb.getSettings().setUseWideViewPort(true)

        wb.getSettings().setJavaScriptEnabled(true)

        wb.getSettings().setAllowFileAccess(true)
        wb.getSettings().setAllowContentAccess(true)
        wb.setScrollbarFadingEnabled(false)


//        wb.setWebChromeClient(object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                if (newProgress == 100) {
//                    mProgressBar.setVisibility(View.GONE)
//                } else {
//                    mProgressBar.setVisibility(View.VISIBLE)
//                    mProgressBar.setProgress(newProgress)
//                }
//            }
//        })

        close_btn.setOnClickListener {
            finish()
        }
    }

    private fun startWebView(url: String) {
        wb.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            //Show loader on url load
            override fun onLoadResource(view: WebView, url: String) {}
            override fun onPageFinished(view: WebView, url: String) {
                println("WEBVIEW onPageFinished" + url)
                try {
                    if (url.contains(BuildConfig.BASE_URL+Constants.ORDER_FAILED_URL)) {
                        startActivity(Intent(this@WebViewActivity, FailureActivity::class.java))
                        finishMethod()
                    } else if (url.contains(BuildConfig.BASE_URL+Constants.PLACE_ORDER_URL)) {
                        println("WEBVIEW place-order" + url)
                        val finalUrl = url.split("=").toTypedArray()
                        val orderID = finalUrl.get(1)

                        val intent =Intent(this@WebViewActivity, SuccessActivity::class.java)
                        intent.putExtra("ORDER_ID",orderID)
                        startActivity(intent)

                        wb.clearHistory()
//                        val broadcastIntent = Intent()
//                        broadcastIntent.action = "com.package.ACTION_CLASS_CABILY_MONEY_REFRESH"
//                        sendBroadcast(broadcastIntent)
                        finishMethod()
                    } else if (url.contains(BuildConfig.BASE_URL+Constants.PAYMENT_CANCEL_URL)) {
                        startActivity(Intent(this@WebViewActivity, FailureActivity::class.java))
                        finishMethod()
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        })

        //Load url in webView
        wb.loadUrl(url)
    }

    private fun finishMethod() {
        finishAffinity()
    }
}