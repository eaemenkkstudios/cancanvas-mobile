package studios.eaemenkk.cancanvas.interactor

import android.content.Context
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.Message
import studios.eaemenkk.cancanvas.repository.ChatRepository

class ChatInteractor(private val context: Context) {
    val repository = ChatRepository(context, context.getString(R.string.api_base_url), context.getString(R.string.api_subscription_url))

    fun newChatMessage(callback: (message: Message) -> Unit) {
        repository.newChatMessage(callback)
    }

    fun sendMessageToUser(receiver: String, message: String, callback: (status: Boolean) -> Unit) {
        repository.sendMessageToUser(receiver, message, callback)
    }

    fun sendMessageToBot(message: String, callback: (message: String) -> Unit) {
        repository.sendMessageToBot(message, callback)
    }
}