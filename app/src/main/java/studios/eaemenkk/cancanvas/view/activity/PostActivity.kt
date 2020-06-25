package studios.eaemenkk.cancanvas.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_post.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.utils.Utils
import studios.eaemenkk.cancanvas.viewmodel.PostViewModel


class PostActivity: AppCompatActivity() {
    private val PICK_PICTURE = 999
    private val viewModel: PostViewModel by lazy {
        ViewModelProvider(this).get(PostViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        fabConfirm.setOnClickListener { getImageFromGallery() }
        ivBack.setOnClickListener { finish() }
        viewModel.postCreatedStatus.observe(this, Observer { status ->
            if (status) {
                Toast.makeText(this, R.string.post_created, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, R.string.post_failed, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getImageFromGallery() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PICK_PICTURE)
        } else {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image))
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, PICK_PICTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PICK_PICTURE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery()
                } else {
                    Toast.makeText(this, getString(R.string.gallery_denied), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        if (requestCode == PICK_PICTURE && resultCode == RESULT_OK) {
            val utils = Utils(this)
            val upload = utils.createFileUploadFromUri(data?.data!!) ?: return
            val description = etComment.text.toString()
            val bidId = intent.extras?.getString("bidId", null)
            viewModel.createPost(upload, description, bidId)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}