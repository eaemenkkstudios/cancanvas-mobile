package studios.eaemenkk.cancanvas.repository

import android.content.Context
import android.os.Handler
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.mutations.CreateUserMutation
import studios.eaemenkk.cancanvas.queries.LoginQuery

class AuthRepository (val context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {

    fun login(nickname: String, password: String, callback: (result: RequestResponse) -> Unit) {

        apolloClient.query(LoginQuery(nickname, password))
            .enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    println("Apollo Error $e")
                }

                override fun onResponse(response: Response<LoginQuery.Data>) {
                    println(response.errors?.get(0)?.message)
                    if(response.data?.login != null) {
                        Handler(context.mainLooper).run {
                            val sharedPreferences = context.getSharedPreferences(
                                context.packageName,
                                Context.MODE_PRIVATE
                            ).edit()
                            sharedPreferences.putString("session", response.data?.login)
                            sharedPreferences.apply()
                            callback(RequestResponse(true, context.getString(R.string.login_succeeded)))
                        }
                    } else {
                        callback(RequestResponse(false, response.errors?.get(0)?.message.toString()))
                    }
                }
            })
    }

    fun signup(nickname: String, email: String, name: String, password: String, callback: (result: RequestResponse) -> Unit) {
        apolloClient.mutate(CreateUserMutation(nickname, name, email, password)).enqueue(object: ApolloCall.Callback<CreateUserMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<CreateUserMutation.Data>) {
                if (response.data?.createUser != null) {
                    callback(RequestResponse(true, ""))
                } else {
                    callback(RequestResponse(false, response.errors?.get(0)?.message.toString()))
                }
            }

        })
    }
}