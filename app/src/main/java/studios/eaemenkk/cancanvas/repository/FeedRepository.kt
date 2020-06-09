package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.queries.TrendingQuery

class FeedRepository (context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun trending() {
        apolloClient.query(TrendingQuery())
            .enqueue(object : ApolloCall.Callback<TrendingQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    println("Apollo Error$e")
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<TrendingQuery.Data>) {
                    println("Apollo Launch site: ${response.data?.trending?.get(0)?.description}")
                }
            })
    }
}