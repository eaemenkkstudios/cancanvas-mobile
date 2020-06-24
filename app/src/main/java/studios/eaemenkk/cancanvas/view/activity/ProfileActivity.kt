package studios.eaemenkk.cancanvas.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.ivProfile
import kotlinx.android.synthetic.main.activity_profile.tvName
import kotlinx.android.synthetic.main.activity_profile.tvNickname
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        transparent_image.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    svProfile.requestDisallowInterceptTouchEvent(true)
                    false
                }

                MotionEvent.ACTION_UP -> {
                    svProfile.requestDisallowInterceptTouchEvent(false)
                    true;
                }

                MotionEvent.ACTION_MOVE-> {
                    svProfile.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }

        getUserInfo()
    }

    private fun getUserInfo() {
        val nickname = intent.extras?.getString("nickname", null)

        if(nickname == null) {
            viewModel.selfData.observe(this, Observer { self ->
                tvNickname.text = self.nickname
                tvName.text = self.name
                tvBio.text = self.bio
                if (self.picture?.isNotEmpty()!!) Picasso.get().load(self.picture).into(ivProfile)
                if (self.cover?.isNotEmpty()!!) Picasso.get().load(self.cover).into(ivCover)
            })
            viewModel.getSelf()

            ivEditBio.visibility = View.VISIBLE
            ivEditCover.visibility = View.VISIBLE
            ivEditProfile.visibility = View.VISIBLE
            ivGradient.visibility = View.VISIBLE

            cvChat.visibility = View.GONE
            cvFollow.visibility = View.GONE
        } else {
            viewModel.userData.observe(this, Observer { user ->
                tvNickname.text = user.nickname
                tvName.text = user.name
                tvBio.text = user.bio
                if (user.picture?.isNotEmpty()!!) Picasso.get().load(user.picture).into(ivProfile)
                if (user.cover?.isNotEmpty()!!) Picasso.get().load(user.cover).into(ivCover)
            })
            viewModel.getUser(nickname)
        }

    }
}