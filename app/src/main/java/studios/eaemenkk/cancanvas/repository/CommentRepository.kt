package studios.eaemenkk.cancanvas.repository

import android.content.Context
import android.os.Handler
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.*
import studios.eaemenkk.cancanvas.mutations.CommentMutation
import studios.eaemenkk.cancanvas.mutations.CreateUserMutation
import studios.eaemenkk.cancanvas.queries.CommentsQuery
import studios.eaemenkk.cancanvas.queries.LoginQuery
import studios.eaemenkk.cancanvas.queries.TrendingQuery

class CommentRepository (val context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun getComments(postId: String, page: Int = 1, callback: (comments: ArrayList<Comment>) -> Unit) {
        apolloClient.query(CommentsQuery(postId, Input.optional(page))).enqueue(object: ApolloCall.Callback<CommentsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error$e")
            }

            override fun onResponse(response: Response<CommentsQuery.Data>) {
                val comments = ArrayList<Comment>()
                response.data?.comments?.forEach { comment ->
                    comments.add(Comment(
                        id = comment.id,
                        text = comment.text,
                        likes = comment.likes,
                        timestamp = comment.timestamp,
                        author = FeedUser(
                            nickname = comment.author.nickname,
                            name = comment.author.name,
                            picture = comment.author.picture
                        )
                    ))
                }
                callback(comments)
            }

        })
    }

    fun commentOnPost(postId: String, message: String, callback: (commentId: String) -> Unit) {
        apolloClient.mutate(CommentMutation(postId, message)).enqueue(object: ApolloCall.Callback<CommentMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error$e")
            }

            override fun onResponse(response: Response<CommentMutation.Data>) {
                response.data?.commentOnPost?.let { callback(it) }
            }

        })
    }
}