package studios.eaemenkk.cancanvas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signupBtn.setOnClickListener{ signup() }
    }

    private fun signup() {
        val username = signupUsername.text.toString()
        val email = signupEmail.text.toString()
        val password = signupPassword.text.toString()
        val confirmPassword = signupConfirmPassword.text.toString()

        if(username.isEmpty()) {
            return Toast.makeText(this, "Por favor digite seu nome de usuário.", Toast.LENGTH_LONG).show()
        }
        if(email.isEmpty()) {
            return Toast.makeText(this, "Por favor digite seu email.", Toast.LENGTH_LONG).show()
        }
        if(password.isEmpty()) {
            return Toast.makeText(this, "Por favor digite sua senha", Toast.LENGTH_LONG).show()
        } else if (password.length < 6) {
            return Toast.makeText(this, "O tamanho mínimo da senha é de 6 caracteres.", Toast.LENGTH_LONG).show()
        }
        if(password != confirmPassword) {
            return Toast.makeText(this, "As senhas digitadas não conferem.", Toast.LENGTH_LONG).show()
        }

        var client = OkHttpClient()
        var request = Api(client)
        val body = JSONObject()
        body.put("nick", username)
        body.put("email", email)
        body.put("pass", password)

        request.post("/signup", body, false, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Uma conta com este email já existe!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Conta criada com sucesso!", Toast.LENGTH_LONG).show()
                        finish()
                    }


                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Falha ao criar conta!", Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}