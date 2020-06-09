package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.repository.ChatRepository
import studios.eaemenkk.cancanvas.view.adapter.PostAdapter

class MainActivity : AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = PostAdapter(this)
    private val viewModel: PostViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ChatRepository(this, getString(R.string.api_base_url), getString(R.string.api_subscription_url)).newChatMessage()
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
