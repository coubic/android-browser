package com.coubic.browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coubic.browser.WebViewActivityFactory.EXTRA_ACCESS_TOKEN
import com.coubic.browser.WebViewActivityFactory.EXTRA_SUBTITLE
import com.coubic.browser.WebViewActivityFactory.EXTRA_TITLE
import com.coubic.browser.WebViewActivityFactory.EXTRA_URL


class WebViewActivity : AppCompatActivity() {

    private var enableSwipeRefresh = false

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_activity)

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
        findViewById<Toolbar>(R.id.toolbar).apply {
            if (title != null) this.title = title
            if (subtitle != null) this.subtitle = subtitle
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setUpSwipeRefresh() {
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).apply {
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

        findViewById<FrameLayout>(R.id.container).addView(webView)

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
                findViewById<FrameLayout>(R.id.loading).visibility = View.VISIBLE
                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).visibility = View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).visibility = View.VISIBLE
                findViewById<FrameLayout>(R.id.loading).visibility = View.GONE
                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).isRefreshing = false

                if (intent.getStringExtra(EXTRA_TITLE) == null) {
                    findViewById<Toolbar>(R.id.toolbar).title = view.title
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