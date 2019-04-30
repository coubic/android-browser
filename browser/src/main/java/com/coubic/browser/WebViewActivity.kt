package com.coubic.browser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coubic.browser.databinding.WebViewActivityBinding


class WebViewActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_SUBTITLE = "subtitle"
        private const val EXTRA_URL = "url"
        private const val EXTRA_ACCESS_TOKEN = "accessToken"

        @JvmStatic
        fun createIntent(
            context: Context,
            url: String,
            title: String? = null,
            subtitle: String? = null,
            token: String? = null
        ): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_SUBTITLE, subtitle)
                if (token != null) putExtra(EXTRA_ACCESS_TOKEN, token)
            }
        }
    }

    private var enableSwipeRefresh = false

    private lateinit var binding: WebViewActivityBinding

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.web_view_activity)

        setUpToolbar(
            intent.getStringExtra(EXTRA_TITLE),
            intent.getStringExtra(EXTRA_SUBTITLE)
        )

        setUpWebView(
            intent.getStringExtra(EXTRA_URL),
            intent.getStringExtra(EXTRA_ACCESS_TOKEN)
        )

        setUpSwipeRefresh()
    }

    private fun setUpToolbar(title: String?, subtitle: String?) {
        binding.toolbar.apply {
            if (title != null) this.title = title
            if (subtitle != null) this.subtitle = subtitle
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefresh.apply {
            setColorSchemeColors(getColorFromAttr(R.attr.colorPrimary))
            setOnRefreshListener { webView.reload() }
            setOnChildScrollUpCallback { _, _ -> webView.scrollY != 0 || !enableSwipeRefresh }
        }
    }

    private fun getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
    private fun setUpWebView(url: String, accessToken: String?) {
        webView = object : WebView(this) {
            override fun onOverScrolled(
                scrollX: Int,
                scrollY: Int,
                clampedX: Boolean,
                clampedY: Boolean
            ) {
                super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
                enableSwipeRefresh = true
            }
        }

        binding.container.addView(webView)

        webView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> enableSwipeRefresh = false
            }
            false
        }

        webView.settings.run {
            javaScriptEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(true)
        }

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                binding.loading.visibility = View.VISIBLE
                binding.swipeRefresh.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.swipeRefresh.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false

                if (intent.getStringExtra(EXTRA_TITLE) == null) {
                    binding.toolbar.title = view.title
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // TODO Handle error
            }
        }

        val headers = if (accessToken != null) mapOf("Access-Token" to accessToken) else null
        webView.loadUrl(url, headers)
    }
}