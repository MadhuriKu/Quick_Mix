package com.orpat.quickmix.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.orpat.quickmix.R
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.wb


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class WebViewFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                WebViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wb.setInitialScale(1)
        wb.getSettings().setLoadWithOverviewMode(true)
        wb.getSettings().setUseWideViewPort(true)

        wb.getSettings().setJavaScriptEnabled(true)

        wb.getSettings().setAllowFileAccess(true)
        wb.getSettings().setAllowContentAccess(true)
        wb.setScrollbarFadingEnabled(false)
        wb.settings.domStorageEnabled = true

        param1?.let{
            startWebView(it)
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

        })

        //Load url in webView
        wb.loadUrl(url)
    }
}