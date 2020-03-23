package studios.eaemenkk.cancanvas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE);
        val token = sharedPreferences.getString("token", null)
        if(!token.isNullOrEmpty()) Toast.makeText(this, "É", Toast.LENGTH_LONG).show()
        var client = OkHttpClient()
        var request = Api(client)

        request.GET("http://192.168.0.10:8080/hello", object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    val json = JSONObject(responseData)
                    println(json)
                    Toast.makeText(this@LoginActivity, responseData, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        loginBtn.setOnClickListener{ signIn() }
        loginSignupBtn.setOnClickListener{ signUp() }
    }

    private fun signIn() {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        if(email.isEmpty()) return Toast.makeText(
            this,
            "Por favor digite seu email.",
            Toast.LENGTH_LONG
        ).show()
        if(password.isEmpty()) {
            return Toast.makeText(
                this,
                "Por favor digite sua senha.",
                Toast.LENGTH_LONG
            ).show()
        } else if(password.length < 6) {
            return Toast.makeText(
                this,
                "O tamanho mínimo da senha é de 6 caracteres.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun signUp() {
        val intentMain = Intent(this, SignUpActivity::class.java)
        startActivity(intentMain)
    }
}