package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.activity_photo.view.*
import studios.eaemenkk.cancanvas.R

class PhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        ivMenu.setOnClickListener { inflatePostMenu(ivMenu) }

        tvName.text = intent.extras?.get("name") as String
        tvNickname.text = intent.extras?.get("nickname") as String
        Picasso.get().load(intent.extras?.get("picture") as String).into(ivProfile)
        Picasso.get().load(intent.extras?.get("content") as String).into(pvPhoto)
        tvComments.text = String.format((intent.extras?.get("comments")).toString())
        tvLikes.text = String.format((intent.extras?.get("likes")).toString())
        ivBack.setOnClickListener { finish() }
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