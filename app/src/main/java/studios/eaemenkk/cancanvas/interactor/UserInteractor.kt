package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import com.apollographql.apollo.api.FileUpload
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.domain.UserWithPosts
import studios.eaemenkk.cancanvas.repository.UserRepository

class UserInteractor(private val context: Context) {
    private val repository = UserRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getSelf(callback: (user: User) -> Unit) {
        repository.getSelf(callback)
    }

    fun getUser(nickname: String, callback: (user: User) -> Unit) {
        repository.getUser(nickname, callback)
    }

    fun getUserWithPosts(nickname: String, callback: (user: UserWithPosts) -> Unit) {
        repository.getUserWithPosts(nickname, callback)
    }

    fun getUserTags(nickname: String, callback: (tags: ArrayList<String>) -> Unit) {
        repository.getUserTags(nickname, callback)
    }

    fun getUsersByTags(tags: ArrayList<String>, page: Int, callback: (users: ArrayList<User>) -> Unit) {
        repository.getUsersByTags(tags, page, callback)
    }

    fun isFollowing(nickname: String, callback: (status: Boolean) -> Unit) {
        repository.isFollowing(nickname, callback)
    }

    fun follow(nickname: String, callback: (status: Boolean) -> Unit) {
        repository.follow(nickname, callback)
    }

    fun unfollow(nickname: String, callback: (status: Boolean) -> Unit) {
        repository.unfollow(nickname, callback)
    }

    fun updateUserPicture(picture: FileUpload, callback: (path: String) -> Unit) {
        repository.updateUserPicture(picture, callback)
    }

    fun updateUserCover(cover: FileUpload, callback: (path: String) -> Unit) {
        repository.updateUserCover(cover, callback)
    }

    fun updateUserBio(bio: String, callback: (status: Boolean) -> Unit) {
        repository.updateUserBio(bio, callback)
    }

    fun updateUserLocation(lat: Double, lng: Double, callback: (status: Boolean) -> Unit) {
        repository.updateUserLocation(lat, lng, callback)
    }
}