package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.PostInteractor

class PostViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = PostInteractor(app.applicationContext)

    val postAuctionList = MutableLiveData<ArrayList<PostAuction>>()
    val error = MutableLiveData<RequestResponse>()

    fun getFeed(page: Int = 1) {
        interactor.getFeed(page) {postAuctions ->
            postAuctionList.postValue(postAuctions)
        }
    }
}