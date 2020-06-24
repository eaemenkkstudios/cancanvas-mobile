package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.Comment
import studios.eaemenkk.cancanvas.repository.CommentRepository
import java.lang.Exception

class CommentInteractor(private val context: Context) {
    private val repository = CommentRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getComments(postId: String, page: Int = 1, callback: (comments: ArrayList<Comment>) -> Unit) {
        repository.getComments(postId, page, callback)
    }

    fun commentOnPost(postId: String, message: String, callback: (commentId: String) -> Unit) {
        if (message.isEmpty()) throw Exception("")
        repository.commentOnPost(postId, message, callback)
    }
}