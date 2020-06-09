package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.repository.AuthRepository
import java.lang.Exception

class AuthInteractor(context: Context) {
    private val authRepository = AuthRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun login(nickname: String, password: String, callback: (result: RequestResponse) -> Unit) {
        if(nickname.isEmpty()) throw Exception("Por favor, informe seu email.")
        if(password.isEmpty()) throw Exception("Por favor, informe sua senha.")
        else if (password.length < 6) throw Exception("O tamanho mínimo da senha é de 6 caracteres.")

        authRepository.login(nickname, password, callback)
    }

    fun signup(nickname: String, email: String, password: String, confirmPassword: String, callback: (result: Boolean) -> Unit) {
        if(nickname.isEmpty()) throw Exception("Por favor, informe seu nome de usuário.")
        if(email.isEmpty()) throw Exception("Por favor, informe seu email.")
        if(password.isEmpty()) throw Exception("Por favor, informe sua senha.")
        else if (password.length < 6) throw Exception("O tamanho mínimo da senha é de 6 caracteres.")
        if(confirmPassword.isEmpty()) throw Exception("Por favor, confirme sua senha.")
        if(password != confirmPassword) throw Exception("As senhas digitadas não conferem.")

    }
}