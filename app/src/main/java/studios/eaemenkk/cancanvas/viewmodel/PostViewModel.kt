package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.PostInteractor
import studios.eaemenkk.cancanvas.utils.Utils
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class PostViewModel(app: Application) : AndroidViewModel(app) {
    private val postInteractor = PostInteractor(app.applicationContext)
    private val utils = Utils(app.applicationContext)

    val postAuctionList = MutableLiveData<ArrayList<PostAuction>>()
    val error = MutableLiveData<RequestResponse>()

    fun getFeed(page: Int = 1) {
        postInteractor.getFeed(page) {postAuctions ->
            postAuctions.forEach { auction -> auction.timestamp = auction.timestamp?.let { utils.timestampToTimeInterval(it) } }
            postAuctionList.postValue(postAuctions)
        }
    }
}