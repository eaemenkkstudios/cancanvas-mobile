package studios.eaemenkk.cancanvas.repository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import studios.eaemenkk.cancanvas.domain.LoginResponse
import studios.eaemenkk.cancanvas.domain.LoginUser
import studios.eaemenkk.cancanvas.domain.NewUser

interface AuthService {

    @POST("/login")
    fun login(
        @Body user: LoginUser
    ): Call<LoginResponse>

    @POST("/signup")
    fun signup(
        @Body user: NewUser
    ): Call<Void>
}

class AuthRepository (context: Context, baseUrl: String): BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(AuthService::class.java)

    fun login(user: LoginUser, callback: (result: LoginResponse?) -> Unit) {
        service.login(user).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val res = response.body()
                callback(res)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun signup(user: NewUser, callback: (result: Boolean) -> Unit) {
        service.signup(user).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() <= 201) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}