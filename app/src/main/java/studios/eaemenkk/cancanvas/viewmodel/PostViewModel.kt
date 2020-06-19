package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.PostInteractor

class PostViewModel(app: Application) : AndroidViewModel(app) {
    private val postInteractor = PostInteractor(app.applicationContext)

    val postAuctionList = MutableLiveData<ArrayList<PostAuction>>()
    val error = MutableLiveData<RequestResponse>()

    fun getFeed(page: Int = 1) {
        postInteractor.getFeed(page) {postAuctions ->
            postAuctions.forEach { auction ->
                if (auction.author != null) {
                    auction.author?.nickname = "@${auction.author?.nickname}"
                }
                if (auction.host != null) {
                    auction.host?.nickname = "@${auction.host?.nickname}"
                }
            }
            postAuctionList.postValue(postAuctions)
        }
    }

    fun getPostUserInfo(username: String) {

    }
}