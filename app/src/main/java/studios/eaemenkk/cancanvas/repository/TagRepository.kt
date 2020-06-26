package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.queries.TagsQuery

class TagRepository(context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun getTags(callback: (tags: ArrayList<String>) -> Unit) {
        apolloClient.query(TagsQuery()).enqueue(object: ApolloCall.Callback<TagsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<TagsQuery.Data>) {
                callback(response.data?.tags as ArrayList<String>)
            }

        })
    }
}