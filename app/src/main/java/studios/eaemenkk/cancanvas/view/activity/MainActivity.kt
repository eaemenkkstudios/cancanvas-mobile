package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.view.adapter.PostAdapter
import studios.eaemenkk.cancanvas.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = PostAdapter(this)
    private val viewModel: PostViewModel by lazy {
        ViewModelProvider(this).get(PostViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        srlFeed.setOnRefreshListener { onRefresh() }
        configureRecyclerView()
        getFeed()
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
