package studios.eaemenkk.cancanvas.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_signup.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.AuthViewModel
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btnSignup.setOnClickListener{ signup() }

        viewModel.signupResponse.observe(this, Observer { result ->
            loadingIcon.visibility = View.GONE
            if (result.status) {
                Toast.makeText(this, getString(R.string.account_created), Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun signup() {
        val nickname = etUsername.text.toString()
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        try {
            loadingIcon.visibility = View.VISIBLE
            viewModel.signup(nickname, name, email, password, confirmPassword)
        } catch (e: Exception) {
            loadingIcon.visibility = View.GONE
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}