package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.AuthViewModel


class LoginActivity : AppCompatActivity()  {

    private var token: String? = null
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        videoView.setOnPreparedListener { mp -> mp.isLooping = true }
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.login_video))
        videoView.start()

        loginBtn.setOnClickListener{ login() }
        loginSignupBtn.setOnClickListener{ signUp() }
    }

    private fun login() {
        val email = loginEmail.text.toString()
        val pass = loginPassword.text.toString()
        viewModel.login(email, pass)

        /*
            AO RECEBER TOKEN RESPOSTA DO LOGIN
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE).edit();
            sharedPreferences.putString("token", token)
            sharedPreferences.apply()
            mainPage()
            finish()
        */

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