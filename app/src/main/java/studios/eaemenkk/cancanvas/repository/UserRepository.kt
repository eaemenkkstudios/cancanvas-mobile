package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.queries.SelfQuery

class UserRepository(context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {
    fun getSelf(callback: (user: User) -> Unit) {
        apolloClient.query(SelfQuery()).enqueue(object: ApolloCall.Callback<SelfQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<SelfQuery.Data>) {
                val selfData = response.data?.self
                val self = User(
                    nickname = selfData?.nickname,
                    name = selfData?.name,
                    picture = selfData?.picture,
                    bio = selfData?.bio,
                    cover = selfData?.cover,
                    followers = selfData?.followers as ArrayList<String>?,
                    followersCount = selfData?.followersCount,
                    following = selfData?.following as ArrayList<String>?,
                    lat = selfData?.lat,
                    lng = selfData?.lng
                )
                callback(self)
            }

        })
    }
}