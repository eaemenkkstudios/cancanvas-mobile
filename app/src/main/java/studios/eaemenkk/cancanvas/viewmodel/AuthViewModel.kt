package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor(app.applicationContext)

    val loginResponse = MutableLiveData<RequestResponse>()
    val signupResponse = MutableLiveData<RequestResponse>()

    fun login(email: String, pass: String) {
        interactor.login(email, pass) { response ->
            loginResponse.postValue(response)
        }
    }

    fun signup(nick: String, name: String, email: String, password: String, confirmPassword: String) {
        interactor.signup(nick, name, email, password, confirmPassword) {result -> signupResponse.postValue(result)}
    }

}