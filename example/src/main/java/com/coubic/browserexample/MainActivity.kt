package com.coubic.browserexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coubic.browser.WebViewActivity
import com.coubic.browserexample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        setSupportActionBar(binding.toolbar)

        binding.openBrowser.setOnClickListener {
            startActivity(
                WebViewActivity.createIntent(
                    this,
                    "https://github.com/coubic/android-browser",
                    "Coubic Custom Browser"
                )
            )
        }
    }

}
