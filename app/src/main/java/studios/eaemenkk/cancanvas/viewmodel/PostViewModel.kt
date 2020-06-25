package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.FileUpload
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.PostInteractor
import studios.eaemenkk.cancanvas.utils.Utils
import kotlin.collections.ArrayList

class PostViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = PostInteractor(app.applicationContext)
    private val utils = Utils(app.applicationContext)

    val postAuctionList = MutableLiveData<ArrayList<PostAuction>>()
    val postAuctionLocalList = MutableLiveData<ArrayList<PostAuction>>()
    val postCreatedStatus = MutableLiveData<Boolean>()
    val error = MutableLiveData<RequestResponse>()

    fun getFeed(page: Int = 1) {
        interactor.getFeed(page) { postAuctions ->
            postAuctions.forEach { auction -> auction.timestamp = auction.timestamp?.let { utils.timestampToTimeInterval(it) } }
            postAuctionList.postValue(postAuctions)
        }
    }

    fun getLocalFeed(page: Int = 1) {
        interactor.getLocalFeed(page) { postAuctions ->
            postAuctions.forEach { auction -> auction.timestamp = auction.timestamp?.let { utils.timestampToTimeInterval(it) } }
            postAuctionLocalList.postValue(postAuctions)
        }
    }

    fun createPost(content: FileUpload, description: String?, bidId: String?) {
        interactor.createPost(content, description, bidId) { path -> postCreatedStatus.postValue(path.isNotEmpty()) }
    }
}