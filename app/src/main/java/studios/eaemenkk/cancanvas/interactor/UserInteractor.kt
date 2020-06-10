package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.repository.UserRepository

class UserInteractor(private val context: Context) {
    private val repository = UserRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getPostUserInfo(username: String) {
        repository.getPostUserInfo(username)
    }
}