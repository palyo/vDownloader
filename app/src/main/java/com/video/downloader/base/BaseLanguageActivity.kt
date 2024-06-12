package com.video.downloader.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.video.downloader.R

abstract class BaseLanguageActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity() {
    var binding: B? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindows()
        applyStatusBar(R.color.colorPrimary)
        binding = bindingFactory(layoutInflater)
        setContentView(binding?.root)
        initView()
        initListeners()
    }

    fun initWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.removeControlBar()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = resources.getBoolean(R.bool.isLightModeAccent)
            isAppearanceLightNavigationBars = resources.getBoolean(R.bool.isLightModeAccent)
        }
    }

    fun Window.showControlBar() {
        val windowInsetsController =
            WindowCompat.getInsetsController(this, decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    fun Window.applyFullScreen() {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    fun applyStatusBar(color: Int) {
        window?.apply {
            statusBarColor = ContextCompat.getColor(this@BaseLanguageActivity, color)
        }
    }

    abstract fun initListeners()
    abstract fun initView()

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.removeControlBar()
            WindowInsetsControllerCompat(window, window.decorView).apply {
                isAppearanceLightStatusBars = resources.getBoolean(R.bool.isLightModeAccent)
                isAppearanceLightNavigationBars = resources.getBoolean(R.bool.isLightModeAccent)
            }
        }
    }

    fun Window.removeControlBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController =
                WindowCompat.getInsetsController(this, decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}