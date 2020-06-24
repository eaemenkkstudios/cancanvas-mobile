package studios.eaemenkk.cancanvas.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comments.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.view.adapter.CommentAdapter
import studios.eaemenkk.cancanvas.viewmodel.CommentViewModel
import java.lang.Exception

class CommentsActivity : AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private lateinit var id: String
    private lateinit var adapter: CommentAdapter
    private val layoutManager = LinearLayoutManager(this)
    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this).get(CommentViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        id = intent.extras?.get("id") as String
        adapter = CommentAdapter(this)
        ivBack.setOnClickListener { finish() }
        fabComment.setOnClickListener { postComment() }
        viewModel.commentList.observe(this, Observer { comments ->
            isLoading = comments.isNullOrEmpty()
           if (refresh) adapter.setComments(comments)
           else adapter.addComments(comments)
        })

        viewModel.commentPosted.observe(this, Observer { posted ->
            if (posted) {
                onRefresh()
            }
        })

        configureRecyclerView()
        getComments()
    }

    private fun configureRecyclerView() {
        rvComments.adapter = adapter
        rvComments.layoutManager = layoutManager
        rvComments.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        rvComments.post { getNextPage() }
                    }
                }
            }
        })
    }

    private fun postComment() {
        val message = etComment.text.toString()
        etComment.text.clear()
        try {
            viewModel.commentOnPost(id, message)
        } catch (e: Exception) {}
    }

    private fun getComments() {
        // if(showLoadingIcon) clLoading.visibility = View.VISIBLE
        isLoading = true
        viewModel.getComments(id, page)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showLoadingIcon = false
        getComments()
    }

    fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        getComments()
    }

}