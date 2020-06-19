package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import studios.eaemenkk.cancanvas.R

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        if (!isTaskRoot) {
            finish()
            return
        }
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            val token = sharedPreferences.getString("session", null)
            if(token.isNullOrEmpty()) {
                val intent = Intent("CANCANVAS_LOGIN").addCategory("CANCANVAS_LOGIN")
                startActivity(intent)
            } else {
                val intent = Intent("CANCANVAS_MAIN").addCategory("CANCANVAS_MAIN")
                startActivity(intent)
            }

            finish()
        },4000)
    }
}