package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.repository.ChatRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fAB.setOnClickListener{ ChatRepository(this, getString(R.string.api_base_url), getString(R.string.api_subscription_url)).newChatMessage()};
    }

    private fun chat() {

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
