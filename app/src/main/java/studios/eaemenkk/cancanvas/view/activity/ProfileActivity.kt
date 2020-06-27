package studios.eaemenkk.cancanvas.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.utils.Utils
import studios.eaemenkk.cancanvas.view.fragments.TagSelectionFragment
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class ProfileActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val layoutManager = LinearLayoutManager(this)
    private val PICK_PROFILE_IMAGE = 999
    private val PICK_COVER_IMAGE = 998
    private val EDIT_BIO = 997
    private var nickname: String? = null
    private lateinit var bio: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        clLoading.visibility = View.VISIBLE
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        nickname = intent.extras?.getString("nickname", null)
        if (nickname != null) bnvFeed.visibility = View.GONE
        else {
            bnvFeed.selectedItemId = R.id.btProfile
            bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.btGlobal -> {
                        startActivity(
                            Intent("CANCANVAS_FEED")
                                .addCategory("CANCANVAS_FEED")
                                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                        overridePendingTransition(0, 0)
                    }
                    R.id.btFollowing -> {
                        startActivity(
                            Intent("CANCANVAS_FOLLOWING")
                                .addCategory("CANCANVAS_FOLLOWING")
                                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                        overridePendingTransition(0, 0)
                    }
                    R.id.btChat -> {
                        startActivity(Intent("CANCANVAS_PELICANVAS")
                            .addCategory("CANCANVAS_PELICANVAS")
                            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                        overridePendingTransition(0, 0)
                    }
                    R.id.btProfile -> {
                        startActivity(
                            Intent("CANCANVAS_PROFILE")
                                .addCategory("CANCANVAS_PROFILE")
                                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                        overridePendingTransition(0, 0)
                    }
                    else -> {
                        val smoothScroller: RecyclerView.SmoothScroller = object: LinearSmoothScroller(this) {
                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_START
                            }
                        }
                        smoothScroller.targetPosition = 0
                        layoutManager.startSmoothScroll(smoothScroller)
                    }
                }
                false
            }
        }

        viewModel.userPicturePath.observe(this, Observer { path ->
            if (path.isNotEmpty()) Picasso.get().load(path).into(ivProfile)
        })
        viewModel.userCoverPath.observe(this, Observer { path ->
            if (path.isNotEmpty()) Picasso.get().load(path).into(ivCover)
        })
    }

    private fun getUserInfo(googleMap: GoogleMap) {
        viewModel.userTags.observe(this, Observer { tags -> tags.forEach { tag -> (tsTags as TagSelectionFragment).addTag(tag) } })
        if(nickname == null) {
            viewModel.selfData.observe(this, Observer { self ->
                viewModel.getUserTags(self.nickname!!)
                tvNickname.text = "@${self.nickname}"
                tvName.text = self.name
                tvBio.text = self.bio
                bio = self.bio.toString()
                if (self.picture?.isNotEmpty()!!) Picasso.get().load(self.picture).into(ivProfile)
                if (self.cover?.isNotEmpty()!!) Picasso.get().load(self.cover).into(ivCover)
                clLoading.visibility = View.GONE
                try {
                    val coordinates = LatLng(self.lat!!, self.lng!!)
                    googleMap.addMarker(MarkerOptions().position(coordinates).title(getString(R.string.artist_location)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
                } catch (e: Exception) {
                    Toast.makeText(this, getString(R.string.load_map_failed), Toast.LENGTH_LONG).show()
                }
            })
            viewModel.getSelf()

            ivEditBio.visibility = View.VISIBLE
            ivEditBio.setOnClickListener {
                startActivityForResult(Intent("CANCANVAS_EDIT_BIO")
                    .addCategory("CANCANVAS_EDIT_BIO")
                    .putExtra("bio", bio), EDIT_BIO)
//                startActivity(Intent("CANCANVAS_EDIT_BIO")
//                    .addCategory("CANCANVAS_EDIT_BIO")
//                    .putExtra("bio", bio))
            }
            ivEditCover.visibility = View.VISIBLE
            ivEditCover.setOnClickListener { getImageFromGallery(PICK_COVER_IMAGE) }
            ivEditProfile.visibility = View.VISIBLE
            ivEditProfile.setOnClickListener { getImageFromGallery(PICK_PROFILE_IMAGE) }
            ivGradient.visibility = View.VISIBLE

            cvChat.visibility = View.GONE
            cvFollow.visibility = View.GONE
        } else {
            viewModel.userData.observe(this, Observer { user ->
                tvNickname.text = "@${user.nickname}"
                tvName.text = user.name
                tvBio.text = user.bio
                if (user.picture?.isNotEmpty()!!) Picasso.get().load(user.picture).into(ivProfile)
                if (user.cover?.isNotEmpty()!!) Picasso.get().load(user.cover).into(ivCover)
                clLoading.visibility = View.GONE
                clLoading.visibility = View.GONE
                try {
                    val coordinates = LatLng(user.lat!!, user.lng!!)
                    googleMap.addMarker(MarkerOptions().position(coordinates).title(getString(R.string.artist_location)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
                } catch (e: Exception) {
                    Toast.makeText(this, getString(R.string.load_map_failed), Toast.LENGTH_LONG).show()
                }
            })
            viewModel.followStatus.observe(this, Observer { status ->
                if (status) {
                    ivFollow.visibility = View.VISIBLE
                    ivFollow.setImageDrawable(getDrawable(R.drawable.unfollow))
                    ivFollow.setOnClickListener { viewModel.unfollow(nickname!!) }
                }
            })
            viewModel.unfollowStatus.observe(this, Observer { status ->
                if (status) {
                    ivFollow.visibility = View.VISIBLE
                    ivFollow.setImageDrawable(getDrawable(R.drawable.star))
                    ivFollow.setOnClickListener { viewModel.follow(nickname!!) }
                }
            })
            viewModel.getUser(nickname!!)
            viewModel.isFollowing(nickname!!)
            viewModel.getUserTags(nickname!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_BIO) {
                if (data != null) {
                    bio = data.extras?.getString("bio").toString()
                    tvBio.text = bio
                }
            } else {
                val utils = Utils(this)
                val upload = utils.createFileUploadFromUri(data?.data!!) ?: return
                when (requestCode) {
                    PICK_PROFILE_IMAGE -> viewModel.updateUserPicture(upload)
                    PICK_COVER_IMAGE -> viewModel.updateUserCover(upload)
                }
            }
        }
    }

    private fun getImageFromGallery(code: Int) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), code)
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

            startActivityForResult(chooserIntent, code)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PICK_PROFILE_IMAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery(PICK_PROFILE_IMAGE)
                } else {
                    Toast.makeText(this, getString(R.string.gallery_denied), Toast.LENGTH_LONG).show()
                }
            }
            PICK_COVER_IMAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery(PICK_COVER_IMAGE)
                } else {
                    Toast.makeText(this, getString(R.string.gallery_denied), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        getUserInfo(googleMap)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }
}