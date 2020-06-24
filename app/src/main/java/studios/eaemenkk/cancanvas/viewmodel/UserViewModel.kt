package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.interactor.UserInteractor

class UserViewModel(app: Application) : AndroidViewModel(app) {
    val interactor = UserInteractor(app.applicationContext)

    val selfData = MutableLiveData<User>()

    fun getSelf() {
        interactor.getSelf { self ->
            self.nickname = "@${self.nickname}"
            selfData.postValue(self)
        }
    }
}