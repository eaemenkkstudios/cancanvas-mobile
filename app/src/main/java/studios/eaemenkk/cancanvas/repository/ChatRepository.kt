package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.NewChatMessageSubscription

class ChatRepository(context: Context, baseUrl: String, subscriptionUrl: String):BaseApollo(context, baseUrl, subscriptionUrl) {
    public fun newChatMessage() {
        val onlineUsersSubscriptionQuery = NewChatMessageSubscription()
        val onlineUsersSubscription = apolloClient.subscribe(onlineUsersSubscriptionQuery)
        onlineUsersSubscription?.execute(object: ApolloSubscriptionCall.Callback<NewChatMessageSubscription.Data> {
            override fun onFailure(e: ApolloException) {
                println("Failed" )
            }
            override fun onResponse(response: Response<NewChatMessageSubscription.Data>) {
                val message = response.data
                println("Response: ${response.data}" )
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
}