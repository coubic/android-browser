package com.coubic.browser

import android.content.Context
import android.content.Intent


object WebViewActivityFactory {

    const val EXTRA_TITLE = "title"
    const val EXTRA_SUBTITLE = "subtitle"
    const val EXTRA_URL = "url"
    const val EXTRA_ACCESS_TOKEN = "accessToken"

    fun createIntent(
        context: Context,
        url: String,
        title: String? = null,
        subtitle: String? = null,
        token: String? = null
    ): Intent {
        return Intent(context, WebViewActivityFactory::class.java).apply {
            putExtra(EXTRA_URL, url)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_SUBTITLE, subtitle)
            if (token != null) putExtra(EXTRA_ACCESS_TOKEN, token)
        }
    }
}