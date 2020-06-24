package studios.eaemenkk.cancanvas.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getUserInfo()

        ivEditBio.setOnClickListener {  }
    }

    private fun getUserInfo() {
        viewModel.getSelf()
        viewModel.selfData.observe(this, Observer { self ->
            tvNickname.text = self.nickname
            tvName.text = self.name
            tvBio.text = self.bio
            if (self.picture?.isNotEmpty()!!) Picasso.get().load(self.picture).into(ivProfile)
            if (self.cover?.isNotEmpty()!!) Picasso.get().load(self.cover).into(ivCover)
        })
    }
}