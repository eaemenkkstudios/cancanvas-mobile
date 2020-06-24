package studios.eaemenkk.cancanvas.domain

data class UserWithPosts (
    val user: User,
    val posts: ArrayList<Post>
)