package com.coubic.browser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_SUBTITLE = "subtitle"
        const val EXTRA_URL = "url"
        const val EXTRA_ACCESS_TOKEN = "accessToken"
        const val EXTRA_MULTIPLE_WINDOWS = "multipleWindows"

        fun createIntent(
            context: Context,
            url: String,
            title: String? = null,
            subtitle: String? = null,
            token: String? = null,
            multipleWindows: Boolean = false
        ): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_SUBTITLE, subtitle)
                if (token != null) putExtra(EXTRA_ACCESS_TOKEN, token)
                putExtra(EXTRA_MULTIPLE_WINDOWS, multipleWindows)
            }
        }
    }

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
            intent.getStringExtra(EXTRA_ACCESS_TOKEN),
            intent.getBooleanExtra(EXTRA_MULTIPLE_WINDOWS, false)
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
    private fun setUpWebView(url: String, accessToken: String?, multipleWindows: Boolean) {
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
            setSupportMultipleWindows(multipleWindows)
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                view?:return false
                resultMsg?:return false

                val newWindowWebView = WebView(this@WebViewActivity)
                view.addView(newWindowWebView)

                val transport     = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWindowWebView
                resultMsg.sendToTarget()

                newWindowWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val uri = request?.url
                        uri?:return false

                        val browserIntent  = Intent(Intent.ACTION_VIEW)
                        browserIntent.data = uri
                        startActivity(browserIntent)

                        return true
                    }
                }
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                findViewById<FrameLayout>(R.id.loading).visibility = View.VISIBLE
                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url
                uri?:return false

                when(uri.scheme) {
                    "tel" -> {
                        handleTelScheme(uri)
                        return true
                    }
                }

                return super.shouldOverrideUrlLoading(view, request)
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

    private fun handleTelScheme(uri: Uri) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(uri.toString().replace("tel:",""))
        builder.setPositiveButton(R.string.call_tel) { _, _ ->
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            // do nothing
        }
        builder.show()
    }
}
