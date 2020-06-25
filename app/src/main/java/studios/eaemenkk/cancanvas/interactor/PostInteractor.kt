package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import com.apollographql.apollo.api.FileUpload
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.repository.PostRepository

class PostInteractor(private val context: Context) {
    private val repository = PostRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getFeed(page: Int = 1, callback: (postAuctions: ArrayList<PostAuction>) -> Unit) {
        repository.trending(page, callback)
    }

    fun getLocalFeed(page: Int = 1, callback: (postAuctions: ArrayList<PostAuction>) -> Unit) {
        repository.getLocalFeed(page, callback)
    }

    fun createPost(content: FileUpload, description: String?, bidId: String?, callback: (path: String) -> Unit) {
        repository.createPost(content, description, bidId, callback)
    }
}