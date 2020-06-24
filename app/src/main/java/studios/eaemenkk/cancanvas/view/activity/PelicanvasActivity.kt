package studios.eaemenkk.cancanvas.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_pelicanvas.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.ChatViewModel

class PelicanvasActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelicanvas)
        tvAnswer.visibility = View.INVISIBLE
        viewModel.botMessage.observe(this, Observer { m ->
            tvAnswer.visibility = View.VISIBLE
            tvAnswer.text = m
        })
        ivBack.setOnClickListener { finish() }
        fabConfirm.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val message = etComment2.text.toString()
        etComment2.text.clear()
        viewModel.sendMessageToBot(message)
    }
}
