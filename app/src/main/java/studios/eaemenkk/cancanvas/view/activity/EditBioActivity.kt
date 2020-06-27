package studios.eaemenkk.cancanvas.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_bid.fabConfirm
import kotlinx.android.synthetic.main.activity_edit_bio.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class EditBioActivity : AppCompatActivity() {
    private var bio: String? = null
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bio)
        bio = intent.extras?.getString("bio", null)
        if (bio == null) {
            finish()
            return
        }
        etComment.setText(bio)
        fabConfirm.setOnClickListener { updateBio() }
        ivBack.setOnClickListener { finish() }
        viewModel.userBioStatus.observe(this, Observer { status ->
            if (status) {
                Toast.makeText(this, getString(R.string.bio_updated), Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, Intent().putExtra("bio", bio))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.bio_update_failed), Toast.LENGTH_SHORT).show()
                fabConfirm.isClickable = true
            }
        })
    }

    private fun updateBio() {
        bio = etComment.text.toString()
        viewModel.updateUserBio(bio!!)
        fabConfirm.isClickable = false
    }
}