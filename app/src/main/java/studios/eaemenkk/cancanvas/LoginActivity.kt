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
        if(!token.isNullOrEmpty()) {
            mainPage()
            finish()
        }
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

        var client = OkHttpClient()
        var request = Api(client)
        val body = JSONObject()
        body.put("email", email)
        body.put("pass", password)
        request.post("/signin", body, "",  object: Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()
                        val responseData = JSONObject(response.body?.string())
                        val token = responseData.get("token").toString()
                        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE).edit();
                        sharedPreferences.putString("token", token)
                        sharedPreferences.commit()
                        mainPage()
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuário e/ou senha incorretos!", Toast.LENGTH_LONG).show()
                    }


                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    private fun mainPage() {
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }


    private fun signUp() {
        val intentMain = Intent(this, SignUpActivity::class.java)
        startActivity(intentMain)
    }
}