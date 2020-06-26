package studios.eaemenkk.cancanvas.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.rvFeed
import kotlinx.android.synthetic.main.activity_search.srlFeed
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.view.adapter.UserAdapter
import studios.eaemenkk.cancanvas.view.fragments.TagSelectionFragment
import studios.eaemenkk.cancanvas.viewmodel.TagViewModel
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class SearchActivity : AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private lateinit var adapter: UserAdapter
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val tagViewModel: TagViewModel by lazy {
        ViewModelProvider(this).get(TagViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        tagViewModel.getTags()
        tagViewModel.tagList.observe(this, Observer { tags ->
            tags.forEach { tag -> (tsTags as TagSelectionFragment).addTag(tag) }
        })
        configureRecyclerView()
        (tsTags as TagSelectionFragment).selectedTags.observe(this, Observer { tags -> getUsers(tags) })
    }

    private fun configureRecyclerView() {
        adapter = UserAdapter(this)
        rvFeed.adapter = adapter
        viewModel.users.observe(this, Observer { users ->
            srlFeed.isRefreshing = false
            clLoading.visibility = View.GONE
            isLoading = users.isNullOrEmpty()

            if(refresh) adapter.setUsers(users)
            else adapter.addUsers(users)
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

    private fun getUsers(tags: ArrayList<String>) {
        if(showLoadingIcon) clLoading.visibility = View.VISIBLE
        isLoading = true
        viewModel.getUsersByTags(tags, page)
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
        (tsTags as TagSelectionFragment).selectedTags.value?.let { getUsers(it) }
    }

    fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        (tsTags as TagSelectionFragment).selectedTags.value?.let { getUsers(it) }
    }
}