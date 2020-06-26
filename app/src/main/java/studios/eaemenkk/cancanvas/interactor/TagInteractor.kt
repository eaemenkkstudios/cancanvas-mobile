package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.repository.TagRepository

class TagInteractor(private val context: Context) {
    private val repository = TagRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun getTags(callback: (tags: ArrayList<String>) -> Unit) {
        repository.getTags(callback)
    }
}