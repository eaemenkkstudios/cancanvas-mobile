package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.FileUpload
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.domain.CommentList
import studios.eaemenkk.cancanvas.domain.Post
import studios.eaemenkk.cancanvas.domain.User
import studios.eaemenkk.cancanvas.domain.UserWithPosts
import studios.eaemenkk.cancanvas.mutations.*
import studios.eaemenkk.cancanvas.queries.*

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

    fun getUser(nickname: String, callback: (user: User) -> Unit) {
        apolloClient.query(UserQuery(nickname)).enqueue(object: ApolloCall.Callback<UserQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UserQuery.Data>) {
                val userData = response.data?.user
                val user = User(
                    nickname = userData?.nickname,
                    name = userData?.name,
                    picture = userData?.picture,
                    bio = userData?.bio,
                    cover = userData?.cover,
                    followers = userData?.followers as ArrayList<String>?,
                    followersCount = userData?.followersCount,
                    following = userData?.following as ArrayList<String>?,
                    lat = userData?.lat,
                    lng = userData?.lng
                )
                callback(user)
            }

        })
    }

    fun getUserWithPosts(nickname: String, callback: (user: UserWithPosts) -> Unit) {
        apolloClient.query(UserWithPostsQuery(nickname)).enqueue(object: ApolloCall.Callback<UserWithPostsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UserWithPostsQuery.Data>) {
                val data = response.data
                val user = User(
                    nickname = data?.user?.nickname,
                    name = data?.user?.name,
                    picture = data?.user?.picture,
                    following = data?.user?.following as ArrayList<String>?,
                    lng = data?.user?.lng,
                    lat = data?.user?.lat,
                    followersCount = data?.user?.followersCount,
                    followers = data?.user?.followers as ArrayList<String>?,
                    cover = data?.user?.cover,
                    bio = data?.user?.bio
                )
                val posts = ArrayList<Post>()
                data?.userPosts?.forEach { post ->
                    posts.add(Post(
                        id = post.id,
                        liked = post.liked,
                        author = post.author,
                        timestamp = post.timestamp,
                        likeCount = post.likeCount,
                        comments = CommentList(list = null, count = post.comments.count),
                        content = post.content,
                        description = post.description,
                        bidID = post.bidID
                    ))
                }
                callback(UserWithPosts(user, posts))
            }

        })
    }

    fun getUsersByTags(tags: ArrayList<String>, callback: (users: ArrayList<User>) -> Unit) {
        apolloClient.query(UsersByTagsQuery(tags)).enqueue(object: ApolloCall.Callback<UsersByTagsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UsersByTagsQuery.Data>) {
                val users = ArrayList<User>()
                response.data?.usersByTags?.forEach { user ->
                    users.add(User(
                        nickname = user.nickname,
                        name = user.name,
                        picture = user.picture,
                        bio = user.bio,
                        cover = user.cover,
                        followers = user.followers as ArrayList<String>?,
                        followersCount = user.followersCount,
                        following = user.following as ArrayList<String>?,
                        lat = user.lat,
                        lng = user.lng
                    ))
                    callback(users)
                }
            }

        })
    }

    fun isFollowing(nickname: String, callback: (status: Boolean) -> Unit) {
        apolloClient.query(IsFollowingQuery(nickname)).enqueue(object: ApolloCall.Callback<IsFollowingQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<IsFollowingQuery.Data>) {
                response.data?.isFollowing?.let { callback(it) }
            }

        })
    }

    fun follow(nickname: String, callback: (status: Boolean) -> Unit) {
        apolloClient.mutate(FollowUserMutation(nickname)).enqueue(object: ApolloCall.Callback<FollowUserMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<FollowUserMutation.Data>) {
                response.data?.follow?.let { callback(it) }
            }

        })
    }

    fun unfollow(nickname: String, callback: (status: Boolean) -> Unit) {
        apolloClient.mutate(UnfollowUserMutation(nickname)).enqueue(object: ApolloCall.Callback<UnfollowUserMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UnfollowUserMutation.Data>) {
                response.data?.unfollow?.let { callback(it) }
            }

        })
    }

    fun updateUserPicture(picture: FileUpload, callback: (path: String) -> Unit) {
        apolloClient.mutate(UpdateUserPictureMutation(picture)).enqueue(object: ApolloCall.Callback<UpdateUserPictureMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UpdateUserPictureMutation.Data>) {
                response.data?.updateUserPicture?.let { callback(it) }
            }

        })
    }

    fun updateUserCover(cover: FileUpload, callback: (path: String) -> Unit) {
        apolloClient.mutate(UpdateUserCoverMutation(cover)).enqueue(object: ApolloCall.Callback<UpdateUserCoverMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UpdateUserCoverMutation.Data>) {
                response.data?.updateUserCover?.let { callback(it) }
            }

        })
    }

    fun updateUserBio(bio: String, callback: (status: Boolean) -> Unit) {
        apolloClient.mutate(UpdateUserBioMutation(bio)).enqueue(object: ApolloCall.Callback<UpdateUserBioMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UpdateUserBioMutation.Data>) {
                response.data?.updateUserBio?.let { callback(it) }
            }

        })
    }

    fun updateUserLocation(lat: Double, lng: Double, callback: (status: Boolean) -> Unit) {
        apolloClient.mutate(UpdateUserLocationMutation(lat, lng)).enqueue(object: ApolloCall.Callback<UpdateUserLocationMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                println("Apollo Error $e")
            }

            override fun onResponse(response: Response<UpdateUserLocationMutation.Data>) {
                response.data?.updateUserLocation?.let { callback(it) }
            }

        })
    }
}