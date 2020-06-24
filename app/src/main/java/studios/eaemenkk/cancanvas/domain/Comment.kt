package studios.eaemenkk.cancanvas.domain

data class CommentList (
    val list: List<Comment>?,
    val count: Int?
)

data class Comment (
    val id: String?,
    val author: FeedUser?,
    val text: String?,
    val likes: Int?,
    var timestamp: String?
)