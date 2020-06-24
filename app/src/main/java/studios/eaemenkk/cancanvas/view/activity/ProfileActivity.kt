package studios.eaemenkk.cancanvas.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.bnvFeed
import kotlinx.android.synthetic.main.activity_profile.clLoading
import kotlinx.android.synthetic.main.activity_profile.ivProfile
import kotlinx.android.synthetic.main.activity_profile.tvName
import kotlinx.android.synthetic.main.activity_profile.tvNickname
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel
import java.lang.Exception

class ProfileActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val layoutManager = LinearLayoutManager(this)

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

    private fun getUserInfo(googleMap: GoogleMap) {
        val nickname = intent.extras?.getString("nickname", null)

        if(nickname == null) {
            viewModel.selfData.observe(this, Observer { self ->
                tvNickname.text = "@${self.nickname}"
                tvName.text = self.name
                tvBio.text = self.bio
                if (self.picture?.isNotEmpty()!!) Picasso.get().load(self.picture).into(ivProfile)
                if (self.cover?.isNotEmpty()!!) Picasso.get().load(self.cover).into(ivCover)
                clLoading.visibility = View.GONE
                try {
                    val coordinates = LatLng(self.lat!!, self.lng!!)
                    googleMap.addMarker(MarkerOptions().position(coordinates).title(getString(R.string.artist_location)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
                } catch (e: Exception) {
                    println("quiabo " + e.message)
                    Toast.makeText(this, getString(R.string.load_map_failed), Toast.LENGTH_LONG).show()
                }
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
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
                } catch (e: Exception) {
                    println("quiabo " + e.message)
                    Toast.makeText(this, getString(R.string.load_map_failed), Toast.LENGTH_LONG).show()
                }
            })
            viewModel.getUser(nickname)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        getUserInfo(googleMap)
    }
}