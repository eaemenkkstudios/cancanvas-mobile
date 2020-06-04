package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import studios.eaemenkk.cancanvas.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE).edit()
        sharedPreferences.remove("token")
        sharedPreferences.apply()
        val intentMain = Intent(this, LoginActivity::class.java)
        startActivity(intentMain)
        finish()
    }
}
