package studios.eaemenkk.cancanvas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE);
        val token = sharedPreferences.getString("token", "").toString()

        val request = Api()
        request.post("/logout", JSONObject(), token, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val sharedPreferencesEditor = sharedPreferences.edit()
                sharedPreferencesEditor.remove("token")
                sharedPreferencesEditor.apply()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        val intentMain = Intent(this, LoginActivity::class.java)
        startActivity(intentMain)
        finish()
    }
}
