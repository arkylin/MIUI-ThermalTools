package com.thermal.tools

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.thermal.tools.pages.*
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import java.io.File
import kotlin.system.exitProcess

class MainActivity : MIUIActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var activity: MIUIActivity
        private val handler by lazy { Handler(Looper.getMainLooper()) }
        fun showToast(string: String) {
//            Log.d("BlockMIUI", "Show Toast: $string")
            handler.post {
                Toast.makeText(activity, string, Toast.LENGTH_LONG).show()
            }
        }
    }

    init {
        activity = this
        //注册页面
        registerPage(MainPage::class.java)
        registerPage(OneKeyMode::class.java)
        registerPage(BatchMode::class.java)
        registerPage(LocalThermalView::class.java)
        //
        registerPage(About::class.java)
        registerPage(Setting::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setSP(getPreferences(0))
        super.onCreate(savedInstanceState)
    }
}