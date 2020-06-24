package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.domain.*
import studios.eaemenkk.cancanvas.queries.FeedQuery
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
                            author = FeedUser(
                                nickname = t.author.nickname,
                                name = t.author.name,
                                picture = t.author.picture
                            ),
                            description = t.description,
                            content =  t.content,
                            timestamp = t.timestamp,
                            comments = CommentList(null, t.comments.count),
                            likes = t.likes,
                            liked = t.liked,
                            bids = null,
                            deadline = null,
                            host = null,
                            offer = null,
                            picture = null
                        ))
                    }
                    response.data?.auctions?.forEach { a ->
                        val bids = ArrayList<Bid>()
                        a.bids.forEach { b ->
                            bids.add(Bid(
                                id = b.id,
                                deadline = b.deadline,
                                timestamp = b.timestamp,
                                issuer = b.issuer,
                                price = b.price,
                                selected = b.selected
                            ))
                        }
                        postAuctions.add(PostAuction(
                            type = "auction",
                            id = a.id,
                            description = a.description,
                            timestamp = a.timestamp,
                            deadline = a.deadline,
                            bids = bids,
                            host = FeedUser(
                                name = a.host.name,
                                nickname = a.host.nickname,
                                picture = a.host.picture
                            ),
                            offer = a.offer,
                            author = null,
                            content =  null,
                            comments = null,
                            likes = null,
                            picture = null,
                            liked = null
                        ))
                    }
                    callback(postAuctions)
                }
            })
    }

    fun getLocalFeed(page: Int = 1, callback: (ArrayList<PostAuction>) -> Unit) {
        apolloClient.query(FeedQuery(Input.optional(page))).enqueue(object: ApolloCall.Callback<FeedQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error$e")
            }

            override fun onResponse(response: Response<FeedQuery.Data>) {
                val postAuctions = ArrayList<PostAuction>()
                response.data?.feed?.forEach { t ->
                    postAuctions.add(PostAuction(
                        type = "post",
                        id = t.id,
                        author = FeedUser(
                            nickname = t.author.nickname,
                            name = t.author.name,
                            picture = t.author.picture
                        ),
                        description = t.description,
                        content =  t.content,
                        timestamp = t.timestamp,
                        comments = CommentList(null, t.comments.count),
                        likes = t.likes,
                        liked = t.liked,
                        bids = null,
                        deadline = null,
                        host = null,
                        offer = null,
                        picture = null
                    ))
                }
                callback(postAuctions)
            }

        })
    }
}