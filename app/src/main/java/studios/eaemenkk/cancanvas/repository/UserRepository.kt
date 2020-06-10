package studios.eaemenkk.cancanvas.repository

import android.content.Context

class UserRepository(context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun getPostUserInfo(username: String) {

    }
}