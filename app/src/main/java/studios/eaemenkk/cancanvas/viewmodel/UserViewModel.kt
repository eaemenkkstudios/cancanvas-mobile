package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.FileUpload
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.interactor.UserInteractor

class UserViewModel(app: Application) : AndroidViewModel(app) {
    val interactor = UserInteractor(app.applicationContext)

    val userData = MutableLiveData<User>()
    val selfData = MutableLiveData<User>()
    val users = MutableLiveData<ArrayList<User>>()
    val followStatus = MutableLiveData<Boolean>()
    val unfollowStatus = MutableLiveData<Boolean>()
    val userBioStatus = MutableLiveData<Boolean>()
    val userPicturePath = MutableLiveData<String>()
    val userCoverPath = MutableLiveData<String>()

    fun getSelf() {
        interactor.getSelf { self -> selfData.postValue(self) }
    }

    fun getUser(nickname: String) {
        interactor.getUser(nickname) { user -> userData.postValue(user) }
    }

    fun getUsersByTags(tags: ArrayList<String>) {
        interactor.getUsersByTags(tags) { u -> users.postValue(u) }
    }

    fun isFollowing(nickname: String) {
        interactor.isFollowing(nickname) { status ->
            if (status) followStatus.postValue(true)
            else unfollowStatus.postValue(true)
        }
    }

    fun follow(nickname: String) {
        interactor.follow(nickname) { status -> followStatus.postValue(status) }
    }

    fun unfollow(nickname: String) {
        interactor.unfollow(nickname) { status -> unfollowStatus.postValue(status) }
    }

    fun updateUserPicture(picture: FileUpload) {
        interactor.updateUserPicture(picture) { path -> userPicturePath.postValue(path) }
    }

    fun updateUserCover(cover: FileUpload) {
        interactor.updateUserCover(cover) { path -> userCoverPath.postValue(path) }
    }

    fun updateUserBio(bio: String) {
        interactor.updateUserBio(bio) {status ->  userBioStatus.postValue(status) }
    }
}