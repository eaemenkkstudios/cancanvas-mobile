package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.domain.Bid
import studios.eaemenkk.cancanvas.domain.CommentList
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.queries.TrendingQuery

class FeedRepository (context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun trending(page: Int = 1, callback: (ArrayList<PostAuction>) -> Unit) {
        apolloClient.query(TrendingQuery(Input.optional(page)))
            .enqueue(object : ApolloCall.Callback<TrendingQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    println("Apollo Error$e")
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<TrendingQuery.Data>) {
                    val postAuctions = ArrayList<PostAuction>()
                    response.data?.trending?.forEach { t ->
                        postAuctions.add(PostAuction(
                            type = "post",
                            id = t.id,
                            author = t.author,
                            description = t.description,
                            content =  t.content,
                            timestamp = t.timestamp.toString(),
                            comments = CommentList(null, t.comments.count),
                            likeCount = t.likeCount,
                            bids = null,
                            deadline = null,
                            host = null,
                            likes = null,
                            offer = null,
                            name = null,
                            picture = null
                        ))
                    }
                    response.data?.auctions?.forEach { a ->
                        val bids = ArrayList<Bid>()
                        a.bids.forEach { b ->
                            bids.add(Bid(
                                id = b.id,
                                deadline = b.deadline,
                                timestamp = b.timestamp.toString(),
                                issuer = b.issuer,
                                price = b.price,
                                selected = b.selected
                            ))
                        }
                        postAuctions.add(PostAuction(
                            type = "auction",
                            id = a.id,
                            description = a.description,
                            timestamp = a.timestamp.toString(),
                            deadline = a.deadline.toString(),
                            bids = bids,
                            host = a.host,
                            offer = a.offer,
                            author = null,
                            content =  null,
                            comments = null,
                            likeCount = null,
                            likes = null,
                            name = null,
                            picture = null
                        ))
                    }
                    callback(postAuctions)
                }
            })
    }
}