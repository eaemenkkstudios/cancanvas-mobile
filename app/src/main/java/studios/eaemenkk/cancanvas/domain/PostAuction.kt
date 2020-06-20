package studios.eaemenkk.cancanvas.domain

data class PostAuction (
    val type: String,
    val id: String,
    var author: FeedUser?,
    val description: String?,
    val content: String?,
    var timestamp: String?,
    val comments: CommentList?,
    val likeCount: Int?,
    val likes: List<String>?,
    var host: FeedUser?,
    val offer: Double?,
    val bids: List<Bid>?,
    val deadline: String?,
    val picture: String?,
    val name: String?
)

data class FeedUser (
    var nickname: String?,
    val name: String?,
    val picture: String?
)

data class Bid (
    val id: String?,
    val price: Double?,
    val issuer: String?,
    val deadline: String?,
    val timestamp: String?,
    val selected: Boolean?
)

data class CommentList (
    val list: List<Comment>?,
    val count: Int?
)

data class Comment (
    val id: String?,
    val author: String?,
    val text: String?,
    val likeCount: Int?,
    val likes: List<String>?,
    val timestamp: String?
)