package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.repository.AuthRepository
import java.lang.Exception

class AuthInteractor(private val context: Context) {
    private val authRepository = AuthRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun login(nickname: String, password: String, callback: (result: RequestResponse) -> Unit) {
        if(nickname.isEmpty()) throw Exception(context.getString(R.string.inform_email))
        if(password.isEmpty()) throw Exception(context.getString(R.string.inform_password))
        else if (password.length < 6) throw Exception(context.getString(R.string.password_min_length))

        authRepository.login(nickname, password, callback)
    }

    fun signup(nickname: String, name: String, email: String, password: String, confirmPassword: String, callback: (result: RequestResponse) -> Unit) {
        if(nickname.isEmpty()) throw Exception(context.getString(R.string.inform_nickname))
        if(name.isEmpty()) throw Exception(context.getString(R.string.inform_name))
        if(email.isEmpty()) throw Exception(context.getString(R.string.inform_email))
        if(password.isEmpty()) throw Exception(context.getString(R.string.inform_password))
        else if (password.length < 6) throw Exception(context.getString(R.string.password_min_length))
        if(confirmPassword.isEmpty()) throw Exception(context.getString(R.string.inform_confirm_password))
        if(password != confirmPassword) throw Exception(context.getString(R.string.passwords_dont_match))

        authRepository.signup(nickname, email, name, password, callback)
    }
}