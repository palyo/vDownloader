package com.video.downloader.ui

import android.content.Intent
import android.os.Handler
import com.video.downloader.base.BaseDarkActivity
import com.video.downloader.databinding.ActivitySplashBinding

class SplashActivity : BaseDarkActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun initListeners() {
        Handler(mainLooper).postDelayed({
            Intent(this@SplashActivity, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(this)
                finish()
            }
        }, 2500)
    }

    override fun initView() {

    }
}