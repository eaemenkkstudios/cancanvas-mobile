package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.domain.RequestResponse
import studios.eaemenkk.cancanvas.interactor.PostInteractor
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class PostViewModel(private val app: Application) : AndroidViewModel(app) {
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
                auction.timestamp = auction.timestamp?.let { timestampToTimeInterval(it) }
            }
            postAuctionList.postValue(postAuctions)
        }
    }

    private fun timestampToTimeInterval(timestamp: String): String {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val timestampDiff = currentTimestamp - timestamp.toLong()

        val ago = app.applicationContext.getString(R.string.ago)
        val word: String
        var divider = 1

        if(timestampDiff < 60){
            word = app.applicationContext.getString(R.string.second)
        } else if(timestampDiff < 3600) {
            word = app.applicationContext.getString(R.string.minute)
            divider = 60
        }
        else if (timestampDiff < 86400) {
            word = app.applicationContext.getString(R.string.hour)
            divider = 3600
        }
        else if (timestampDiff < 604800) {
            word = app.applicationContext.getString(R.string.day)
            divider = 86400
        }
        else if (timestampDiff < 2419200) {
            word = app.applicationContext.getString(R.string.week)
            divider = 604800
        }
        else if (timestampDiff < 29030400) {
            word = app.applicationContext.getString(R.string.month)
            divider = 2419200
        }
        else {
            word = app.applicationContext.getString(R.string.year)
            divider = 29030400
        }

        val unit = floor(timestampDiff.toDouble()/divider).toInt()

        return if(unit <= 1){
            "$unit $word $ago"
        } else {
            "$unit ${word}s $ago"
        }

    }
}