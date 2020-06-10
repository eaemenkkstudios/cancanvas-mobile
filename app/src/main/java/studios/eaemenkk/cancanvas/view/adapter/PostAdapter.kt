package studios.eaemenkk.cancanvas.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.auction_card.view.*
import kotlinx.android.synthetic.main.auction_card.view.ivProfile
import kotlinx.android.synthetic.main.auction_card.view.tvName
import kotlinx.android.synthetic.main.auction_card.view.tvNickname
import kotlinx.android.synthetic.main.auction_card.view.tvTime
import kotlinx.android.synthetic.main.post_card.view.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction

private const val AD_INTERVAL = 8

class PostAdapter(private val context: Context): RecyclerView.Adapter<PostAdapter.AuctionPostViewHolder>() {
    private var dataSet = ArrayList<PostAuction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionPostViewHolder {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
                PostViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.auction_card, parent, false)
                AuctionViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.auction_card, parent, false)
                AdViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AuctionPostViewHolder, position: Int) {
        val postAuction = dataSet[position]
        if(holder !is AdViewHolder) {
            /* holder.itemView.setOnClickListener {
                val intent = Intent("OVERTRACKER_PLAYER_INFO")
                    .addCategory("OVERTRACKER_PLAYER_INFO")
                intent.putExtra("playerId", postAuction.id)
                context.startActivity(intent)
            } */
        }

        when (holder) {
            is AuctionViewHolder -> {
                holder.bids.text = postAuction.bids?.size.toString()
                holder.nickname.text = postAuction.host
                holder.time.text = postAuction.timestamp
            }
            is PostViewHolder -> {
                holder.likes.text = postAuction.likeCount.toString()
                holder.comments.text = postAuction.comments?.count.toString()
                holder.nickname.text = postAuction.author
                holder.time.text = postAuction.timestamp
            }
            is AdViewHolder -> {
               /* val adLoader = AdLoader.Builder(context, context.getString(R.string.ad_native_id))
                    .forUnifiedNativeAd { unifiedNativeAd ->
                        holder.adView.headlineView = holder.headlineView
                        holder.adView.bodyView = holder.bodyView
                        holder.adView.callToActionView = holder.callToActionView
                        holder.adView.iconView = holder.iconView
                        holder.adView.priceView = holder.priceView
                        holder.adView.starRatingView = holder.starRatingView
                        holder.adView.storeView = holder.storeView
                        holder.adView.advertiserView = holder.advertiserView
                        holder.adView.mediaView = holder.mediaView
                        populateNativeAdView(unifiedNativeAd, holder.adView)
                    }
                    .withAdListener(object: AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            removeCard(position)
                        }
                    })
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()
                adLoader.loadAd(AdRequest.Builder().build()) */
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].type) {
            "post" -> 0
            "auction" -> 1
            else -> 2
        }
    }

    private fun removeCard(index: Int) {
        dataSet.removeAt(index)
        notifyDataSetChanged()
    }

    private fun addPostAuction(index: Int, postAuction: PostAuction?) {
        if(postAuction != null) {
            dataSet.add(postAuction)
            if(index % AD_INTERVAL == AD_INTERVAL - 1) {
                dataSet.add(PostAuction(
                    "ad",
                    "",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ))
            }
            notifyDataSetChanged()
        }
    }

    fun addPostAuctions(postAuctions: ArrayList<PostAuction>?) {
        postAuctions?.forEachIndexed { index, postAuction -> addPostAuction(index, postAuction) }
    }

    fun setPostAuctions(postAuction: ArrayList<PostAuction>?) {
        if(postAuction != null) {
            dataSet = ArrayList()
            postAuction.forEachIndexed { index, card -> addPostAuction(index, card)
            }
        }
    }

    private fun populateNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = "${nativeAd.callToAction} "

        val icon = nativeAd.icon
        if (icon == null) {
            adView.iconView.visibility = View.INVISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }
        if (nativeAd.mediaContent == null) {
            adView.mediaView.visibility = View.GONE
        } else {
            (adView.mediaView as MediaView).setMediaContent(nativeAd.mediaContent)
            adView.mediaView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }

    abstract class AuctionPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class AdViewHolder(itemView: View): AuctionPostViewHolder(itemView) {
        /* val adView: UnifiedNativeAdView = itemView.ad_view
        val headlineView: TextView = itemView.ad_headline
        val bodyView: TextView = itemView.ad_body
        val callToActionView: Button = itemView.ad_call_to_action
        val iconView: ImageView = itemView.ad_icon
        val priceView: TextView = itemView.ad_price
        val starRatingView: RatingBar = itemView.ad_stars
        val storeView: TextView = itemView.ad_store
        val advertiserView: TextView = itemView.ad_advertiser
        val mediaView: MediaView = itemView.ad_media */
    }

    class AuctionViewHolder(itemView: View): AuctionPostViewHolder(itemView) {
        val picture: ImageView = itemView.ivProfile
        val nickname: TextView = itemView.tvNickname
        val name: TextView = itemView.tvName
        val follow: Switch = itemView.sFollow
        val bids: TextView = itemView.tvBids
        val time: TextView = itemView.tvTime
    }

    class PostViewHolder(itemView: View): AuctionPostViewHolder(itemView) {
        val picture: ImageView = itemView.ivProfile
        val nickname: TextView = itemView.tvNickname
        val name: TextView = itemView.tvName
        val time: TextView = itemView.tvTime
        val comments: TextView = itemView.tvComments
        val likes: TextView = itemView.tvLikes
    }
}