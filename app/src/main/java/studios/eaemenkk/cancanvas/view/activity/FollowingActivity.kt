package studios.eaemenkk.cancanvas.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_feed.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.view.adapter.PostAdapter
import studios.eaemenkk.cancanvas.viewmodel.CommentViewModel
import studios.eaemenkk.cancanvas.viewmodel.PostViewModel

class FollowingActivity : AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private lateinit var adapter: PostAdapter
    private val commentViewModel: CommentViewModel by lazy {
        ViewModelProvider(this).get(CommentViewModel::class.java)
    }
    private val viewModel: PostViewModel by lazy {
        ViewModelProvider(this).get(PostViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        adapter = PostAdapter(this, commentViewModel)

        srlFeed.setOnRefreshListener { onRefresh() }
        val attrs = intArrayOf(R.attr.colorPrimary, R.attr.colorAccent)
        val themeId = packageManager.getActivityInfo(componentName, 0).theme
        val ta: TypedArray = obtainStyledAttributes(themeId, attrs)
        srlFeed.setColorSchemeColors(ta.getColor(0, Color.BLACK))
        srlFeed.setProgressBackgroundColorSchemeColor(ta.getColor(1, Color.BLACK))

        bnvFeed.selectedItemId = R.id.btFollowing
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent("CANCANVAS_FEED")
                        .addCategory("CANCANVAS_FEED")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btChat -> {

                }
                R.id.btProfile -> {
                    startActivity(Intent("CANCANVAS_PROFILE")
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

        configureRecyclerView()
        getFeed()
        ta.recycle()
    }

    private fun configureRecyclerView() {
        rvFeed.adapter = adapter
        viewModel.postAuctionList.observe(this, Observer { postAuctions ->
            srlFeed.isRefreshing = false
            clLoading.visibility = View.GONE
            isLoading = postAuctions.isNullOrEmpty()
            if(refresh) adapter.setPostAuctions(postAuctions)
            else adapter.addPostAuctions(postAuctions)
        })

        viewModel.error.observe(this, Observer { response ->
            if(!response.status && response.message != "") {
                srlFeed.isRefreshing = false
                clLoading.visibility = View.GONE
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        })
        rvFeed.layoutManager = layoutManager
        rvFeed.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        rvFeed.post { getNextPage() }
                    }
                }
            }
        })
    }

    private fun getFeed() {
        if(showLoadingIcon) clLoading.visibility = View.VISIBLE
        isLoading = true
        viewModel.getFeed(page)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showLoadingIcon = false
        getFeed()
    }

    fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        getFeed()
    }
}
