package studios.eaemenkk.cancanvas.domain

class User {
}

data class NewUser(val nick: String, val email: String, val password: String)

data class LoginUser(val email: String, val pass: String)

data class LoginResponse(val token: String)