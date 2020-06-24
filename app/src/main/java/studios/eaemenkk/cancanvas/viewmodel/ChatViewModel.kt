package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.Message
import studios.eaemenkk.cancanvas.interactor.ChatInteractor

class ChatViewModel(app: Application) : AndroidViewModel(app) {
    val interactor = ChatInteractor(app.applicationContext)

    val messageStatus = MutableLiveData<Boolean>()
    val botMessage = MutableLiveData<String>()
    val latestMessage = MutableLiveData<Message>()

    fun newChatMessage() {
        interactor.newChatMessage { message -> latestMessage.postValue(message) }
    }

    fun sendMessageToUser(receiver: String, message: String) {
        interactor.sendMessageToUser(receiver, message) { status -> messageStatus.postValue(status) }
    }

    fun sendMessageToBot(message: String) {
        interactor.sendMessageToBot(message) { msg -> botMessage.postValue(msg) }
    }
}