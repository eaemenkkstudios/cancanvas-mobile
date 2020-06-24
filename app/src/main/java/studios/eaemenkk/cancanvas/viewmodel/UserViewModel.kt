package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.interactor.UserInteractor

class UserViewModel(app: Application) : AndroidViewModel(app) {
    val interactor = UserInteractor(app.applicationContext)

    val userData = MutableLiveData<User>()
    val selfData = MutableLiveData<User>()
    val users = MutableLiveData<ArrayList<User>>()

    fun getSelf() {
        interactor.getSelf { self -> selfData.postValue(self) }
    }

    fun getUser(nickname: String) {
        interactor.getUser(nickname) { user -> userData.postValue(user) }
    }

    fun getUsersByTags(tags: ArrayList<String>) {
        interactor.getUsersByTags(tags) { u -> users.postValue(u) }
    }
}