package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.domain.Message
import studios.eaemenkk.cancanvas.mutations.SendMessageToBotMutation
import studios.eaemenkk.cancanvas.mutations.SendMessageToUserMutation
import studios.eaemenkk.cancanvas.subscriptions.NewChatMessageSubscription

class ChatRepository(context: Context, baseUrl: String, subscriptionUrl: String):BaseApollo(context, baseUrl, subscriptionUrl) {
    fun newChatMessage(callback: (message: Message) -> Unit) {
        val onlineUsersSubscriptionQuery = NewChatMessageSubscription()
        val onlineUsersSubscription = apolloClient.subscribe(onlineUsersSubscriptionQuery)
        onlineUsersSubscription?.execute(object: ApolloSubscriptionCall.Callback<NewChatMessageSubscription.Data> {
            override fun onFailure(e: ApolloException) {
                println("Failed" )
            }
            override fun onResponse(response: Response<NewChatMessageSubscription.Data>) {
                val data = response.data
                val message = Message(
                    chatID = data?.newChatMessage?.chatID,
                    timestamp = data?.newChatMessage?.timestamp,
                    message = data?.newChatMessage?.message,
                    sender = data?.newChatMessage?.sender
                )
                callback(message)
                println(response.errors?.size)
                println(response.errors?.get(0)?.message)
                println("Response: ${response.data}")
            }
            override fun onConnected() {
                println("Connected" )
            }
            override fun onTerminated() {
                println("Terminated" )
            }
            override fun onCompleted() {
                println("Completed" )
            }
        })
    }

    fun sendMessageToUser(receiver: String, message: String, callback: (status: Boolean) -> Unit) {
        apolloClient.mutate(SendMessageToUserMutation(receiver = receiver, msg = message)).enqueue(object: ApolloCall.Callback<SendMessageToUserMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error$e")
            }

            override fun onResponse(response: Response<SendMessageToUserMutation.Data>) {
                response.data?.sendMessage?.let { callback(it) }
            }

        })
    }

    fun sendMessageToBot(message: String, callback: (message: String) -> Unit) {
        apolloClient.mutate(SendMessageToBotMutation(message)).enqueue(object: ApolloCall.Callback<SendMessageToBotMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error$e")
            }

            override fun onResponse(response: Response<SendMessageToBotMutation.Data>) {
                response.data?.sendMessageToDialogflow?.let { callback(it) }
            }

        })
    }
}