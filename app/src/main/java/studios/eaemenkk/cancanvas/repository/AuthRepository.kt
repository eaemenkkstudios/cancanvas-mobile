package studios.eaemenkk.cancanvas.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Handler
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.LoginQuery
import studios.eaemenkk.cancanvas.NewChatMessageSubscription
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.RequestResponse

class AuthRepository (val context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {

    fun login(nickname: String, password: String, callback: (result: RequestResponse) -> Unit) {

        apolloClient.query(LoginQuery(nickname, password))
            .enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    println("Apollo Error$e")
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<LoginQuery.Data>) {
                    if(response.data?.login != null) {
                        Handler(context.mainLooper).run {
                            val sharedPreferences = context.getSharedPreferences(
                                context.packageName,
                                Context.MODE_PRIVATE
                            ).edit()
                            sharedPreferences.putString("session", response.data?.login)
                            sharedPreferences.apply()
                            callback(RequestResponse(true, ""))
                        }
                    }
                }
            })
    }

    fun signup(callback: (result: Boolean) -> Unit) {

    }
}