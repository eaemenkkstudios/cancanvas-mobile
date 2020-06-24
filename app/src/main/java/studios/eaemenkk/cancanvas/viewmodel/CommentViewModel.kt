package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.Comment
import studios.eaemenkk.cancanvas.interactor.CommentInteractor
import studios.eaemenkk.cancanvas.utils.Utils

class CommentViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = CommentInteractor(app.applicationContext)
    private val utils = Utils(app.applicationContext)

    val commentList = MutableLiveData<ArrayList<Comment>>()
    val commentPosted = MutableLiveData<Boolean>()
    val likeStatus = MutableLiveData<Boolean>()

    fun getComments(postId: String, page: Int = 1) {
        interactor.getComments(postId, page) { comments ->
            comments.forEach { comment ->
                comment.author?.nickname = "@${comment.author?.nickname}"
                comment.timestamp = comment.timestamp?.let { utils.timestampToTimeInterval(it) }
            }
            commentList.postValue(comments)
        }
    }

    fun commentOnPost(postId: String, message: String) {
        interactor.commentOnPost(postId, message) { commentPosted.postValue(true) }
    }

    fun likePost(postId: String) {
        interactor.likePost(postId) { status -> likeStatus.postValue(status) }
    }
}