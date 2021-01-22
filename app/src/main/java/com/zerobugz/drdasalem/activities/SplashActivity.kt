package com.zerobugz.drdasalem.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.utils.SharedPreference
import com.zerobugz.drdasalem.utils.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val year = Calendar.getInstance()[Calendar.YEAR]
        footerText.text = "© $year ZeroBugz"

        Handler().postDelayed({
            val sp = SharedPreference()
            if (sp.getBoolean(this@SplashActivity, "login")) {
                Utils.openActivity(this, MainActivity::class.java, true)
            } else {
                Utils.openActivity(this, LoginActivity::class.java, true)
            }
        }, 2000)
    }
}