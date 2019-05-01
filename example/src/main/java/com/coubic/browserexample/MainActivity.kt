package com.coubic.browserexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coubic.browser.WebViewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        setSupportActionBar(binding.toolbar)

        binding.openBrowser.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra("url", "https://github.com/coubic/android-browser")
                putExtra("title", "Coubic Custom Browser")
            }
            startActivity(intent)
//            startActivity(
//                WebViewActivity.createIntent(
//                    this,
//                    "https://github.com/coubic/android-browser",
//                    "Coubic Custom Browser"
//                )
//            )
        }
    }

}
