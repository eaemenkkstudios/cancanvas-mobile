package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.LoginResponse
import studios.eaemenkk.cancanvas.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor(app.applicationContext)

    val loginResponse = MutableLiveData<LoginResponse>()
    val signupResponse = MutableLiveData<Boolean>()

    fun login(email: String, pass: String) {
        interactor.login(email, pass) { response ->
            if(response != null) {
                loginResponse.value = response
            } else {
                loginResponse.value = null
            }
        }
    }

    fun signup(nick: String, email: String, password: String, confirmPassword: String) {
        interactor.signup(nick, email, password, confirmPassword) {result -> signupResponse.value = result}
    }

}