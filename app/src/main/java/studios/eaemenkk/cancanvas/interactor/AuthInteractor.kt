package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.LoginResponse
import studios.eaemenkk.cancanvas.domain.LoginUser
import studios.eaemenkk.cancanvas.domain.NewUser
import studios.eaemenkk.cancanvas.repository.AuthRepository
import java.lang.Exception

class AuthInteractor(context: Context) {
    private val authRepository = AuthRepository(context, context.getString(R.string.api_base_url))

    fun login(email: String, pass: String, callback: (result: LoginResponse?) -> Unit) {
        if(email.isEmpty()) throw Exception("Por favor, informe seu email.")
        if(pass.isEmpty()) throw Exception("Por favor, informe sua senha.")
        else if (pass.length < 6) throw Exception("O tamanho mínimo da senha é de 6 caracteres.")

        val user = LoginUser(email, pass)
        authRepository.login(user, callback)
    }

    fun signup(nick: String, email: String, password: String, confirmPassword: String, callback: (result: Boolean) -> Unit) {
        if(nick.isEmpty()) throw Exception("Por favor, informe seu nome de usuário.")
        if(email.isEmpty()) throw Exception("Por favor, informe seu email.")
        if(password.isEmpty()) throw Exception("Por favor, informe sua senha.")
        else if (password.length < 6) throw Exception("O tamanho mínimo da senha é de 6 caracteres.")
        if(confirmPassword.isEmpty()) throw Exception("Por favor, confirme sua senha.")
        if(password != confirmPassword) throw Exception("As senhas digitadas não conferem.")

        val user = NewUser(nick, email, password)
        authRepository.signup(user, callback)
    }
}