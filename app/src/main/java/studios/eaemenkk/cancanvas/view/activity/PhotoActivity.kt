package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.activity_photo.view.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.CommentViewModel

class PhotoActivity : AppCompatActivity() {
    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this).get(CommentViewModel::class.java)
    }
    private lateinit var id: String
    private var likes: Int = 0
    private var liked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_photo)
        ivMenu.setOnClickListener { inflatePostMenu(ivMenu) }

        tvName.text = intent.extras?.get("name") as String
        tvNickname.text = intent.extras?.get("nickname") as String
        Picasso.get().load(intent.extras?.get("picture") as String).into(ivProfile)
        Picasso.get().load(intent.extras?.get("content") as String).into(pvPhoto)
        tvComments.text = String.format((intent.extras?.get("comments")).toString())
        liked = intent.extras?.get("liked") as Boolean
        likes = intent.extras?.get("likes") as Int
        tvLikes.text = likes.toString()
        id = intent.extras?.get("id") as String
        ivLike.setOnClickListener {
            viewModel.likePost(id)
            ivLike.isClickable = false
            ivLike.playAnimation()
            Handler().postDelayed({
                ivLike.progress = 0F
                ivLike.pauseAnimation()
                ivLike.isClickable = true
            }, 1100)
        }
        viewModel.likeStatus.observe(this, Observer { status ->
            if (status) {
                likes += if(!liked) 1 else -1
                liked = !liked
                tvLikes.text = likes.toString()
            }
        })
        ivBack.setOnClickListener { finish() }
        pvPhoto.setOnClickListener {
            flPopup.visibility = if(flPopup.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        ivComment.setOnClickListener {
            val intent = Intent("CANCANVAS_COMMENTS").addCategory("CANCANVAS_COMMENTS")
                .putExtra("id", String.format((intent.extras?.get("id")).toString()))
            startActivity(intent)
        }
    }

    private fun inflatePostMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.post_menu, popup.menu)
        popup.show()
    }
}