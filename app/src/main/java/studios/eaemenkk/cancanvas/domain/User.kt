package studios.eaemenkk.cancanvas.domain

data class User (
    var nickname: String?,
    val name: String?,
    val bio: String?,
    val picture: String?,
    val cover: String?,
    val followers: ArrayList<String>?,
    val followersCount: Int?,
    val following: ArrayList<String>?,
    val lat: Double?,
    val lng: Double?
)