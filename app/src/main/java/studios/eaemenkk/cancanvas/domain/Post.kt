package studios.eaemenkk.cancanvas.domain

data class Post (
    val id: String?,
    val author: String?,
    val description: String?,
    val content: String?,
    val timestamp: String?,
    val comments: CommentList?,
    val likeCount: Int?,
    val liked: Boolean?,
    val bidID: String?
)