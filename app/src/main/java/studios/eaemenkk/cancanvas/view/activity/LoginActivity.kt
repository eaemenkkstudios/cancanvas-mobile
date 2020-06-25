package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.AuthViewModel
import java.lang.Exception


class LoginActivity : AppCompatActivity()  {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            ivVideoFrame.visibility = View.INVISIBLE
        }
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.login_video))
        videoView.start()

        btnLogin.setOnClickListener{ login() }
        btnSignup.setOnClickListener{ signUp() }

        viewModel.loginResponse.observe(this, Observer { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            if(result.status) {
                val intent = Intent("CANCANVAS_FEED").addCategory("CANCANVAS_FEED")
                startActivity(intent)
                finish()
            }
        })
    }

    private fun login() {
        val nickname = etEmail.text.toString()
        val password = etPassword.text.toString()
        try {
            viewModel.login(nickname, password)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        val intent = Intent("CANCANVAS_SIGN_UP").addCategory("CANCANVAS_SIGN_UP")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        videoView.start()
    }

}