package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.repository.FeedRepository

class PostInteractor(private val context: Context) {
    private val repository = FeedRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getFeed(page: Int = 1, callback: (postAuctions: ArrayList<PostAuction>) -> Unit) {
        repository.trending(page, callback)
    }

    fun getLocalFeed(page: Int = 1, callback: (postAuctions: ArrayList<PostAuction>) -> Unit) {
        repository.getLocalFeed(page, callback)
    }
}