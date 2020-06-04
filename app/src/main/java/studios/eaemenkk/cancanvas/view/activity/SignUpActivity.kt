package studios.eaemenkk.cancanvas.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import studios.eaemenkk.cancanvas.R

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

        loadingIcon.visibility = View.VISIBLE

    }
}