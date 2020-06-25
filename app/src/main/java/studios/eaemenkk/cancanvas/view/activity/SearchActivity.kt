package studios.eaemenkk.cancanvas.view.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.apollographql.apollo.ApolloCall
//import com.apollographql.apollo.api.Input
//import com.apollographql.apollo.api.Response
//import com.apollographql.apollo.exception.ApolloException
//import kotlinx.android.synthetic.main.activity_feed.*
//import kotlinx.android.synthetic.main.activity_post.*
//import kotlinx.android.synthetic.main.activity_search.*
//import kotlinx.android.synthetic.main.activity_search.rvFeed
//import kotlinx.android.synthetic.main.activity_search.srlFeed
//import studios.eaemenkk.cancanvas.R
//import studios.eaemenkk.cancanvas.mutations.CreatePostMutation
//import studios.eaemenkk.cancanvas.queries.UsersByTagsQuery
//import studios.eaemenkk.cancanvas.repository.BaseApollo
//import studios.eaemenkk.cancanvas.view.adapter.PostAdapter
//import studios.eaemenkk.cancanvas.viewmodel.PostViewModel
//import studios.eaemenkk.cancanvas.viewmodel.UserViewModel
//
//class SearchActivity : AppCompatActivity() {
//    private var page = 1
//    private var refresh = true
//    private var isLoading = false
//    private var showLoadingIcon = true
//    private val layoutManager = LinearLayoutManager(this)
//    private lateinit var adapter: UserAdapter
//    private val viewModel: UserViewModel by lazy {
//        ViewModelProvider(this).get(UserViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search)
//
//        val baseApollo = BaseApollo(this, getString(R.string.api_base_url), getString(R.string.api_subscription_url))
//        val tags = ArrayList<String>()
//        tags.add(etSearch.text.toString())
//
//        baseApollo.apolloClient.query(UsersByTagsQuery(tags, Input.optional(1)))
//            .enqueue(object : ApolloCall.Callback<UsersByTagsQuery.Data>() {
//                override fun onFailure(e: ApolloException) {
//                    println("Apollo Error $e")
//                }
//                override fun onResponse(response: Response<UsersByTagsQuery.Data>) {
//                    println(response.errors?.get(0)?.message)
//                }
//            })
//    }
//
//    private fun configureRecyclerView() {
//        rvFeed.adapter = adapter
//        viewModel.users.observe(this, Observer { users ->
//            srlFeed.isRefreshing = false
//            clLoading.visibility = View.GONE
//            isLoading = users.isNullOrEmpty()
//            if(refresh) adapter.setPostAuctions(users)
//            else adapter.addPostAuctions(users)
//        })
//
//        rvFeed.layoutManager = layoutManager
//        rvFeed.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if(dy > 0) {
//                    val visibleItemCount = layoutManager.childCount
//                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
//                    val total = adapter.itemCount
//
//                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
//                        rvFeed.post { getNextPage() }
//                    }
//                }
//            }
//        })
//    }
//
//    private fun getFeed() {
//        if(showLoadingIcon) clLoading.visibility = View.VISIBLE
//        isLoading = true
//        viewModel.getFeed(page)
//    }
//
//    override fun onBackPressed() {
//        finish()
//        overridePendingTransition(0, 0)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        overridePendingTransition(0, 0)
//    }
//
//    private fun onRefresh() {
//        page = 1
//        refresh = true
//        showLoadingIcon = false
//        getFeed()
//    }
//
//    fun getNextPage() {
//        page++
//        refresh = false
//        showLoadingIcon = true
//        getFeed()
//    }
//}