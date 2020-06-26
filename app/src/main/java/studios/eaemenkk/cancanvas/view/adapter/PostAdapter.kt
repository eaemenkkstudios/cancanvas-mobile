package studios.eaemenkk.cancanvas.view.adapter

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.auction_card.view.*
import kotlinx.android.synthetic.main.auction_card.view.ivProfile
import kotlinx.android.synthetic.main.auction_card.view.tvName
import kotlinx.android.synthetic.main.auction_card.view.tvNickname
import kotlinx.android.synthetic.main.auction_card.view.tvTime
import kotlinx.android.synthetic.main.post_card.*
import kotlinx.android.synthetic.main.post_card.view.*
import kotlinx.android.synthetic.main.post_card.view.ivMenu
import kotlinx.android.synthetic.main.post_card.view.llProfile
import kotlinx.android.synthetic.main.post_card.view.tvDescription
import kotlinx.android.synthetic.main.post_card.view.tvLikes
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.PostAuction
import studios.eaemenkk.cancanvas.viewmodel.CommentViewModel


private const val AD_INTERVAL = 8

class PostAdapter(private val context: Context, private val viewModel: CommentViewModel): RecyclerView.Adapter<PostAdapter.AuctionPostViewHolder>() {
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
                holder.nickname.text = "@${postAuction.host?.nickname}"
                holder.name.text = postAuction.host?.name
                if (postAuction.host?.picture != "") {
                    Picasso.get().load(postAuction.host?.picture).into(holder.picture)
                }

                holder.time.text = postAuction.timestamp
                holder.description.text = postAuction.description
                holder.menu.setOnClickListener { inflateMenu(holder.menu, R.menu.auction_menu) }
                holder.profile.setOnClickListener {
                    val intent = Intent("CANCANVAS_PROFILE").addCategory("CANCANVAS_PROFILE")
                        .putExtra("nickname", postAuction.host?.nickname)
                    context.startActivity(intent)
                }
            }
            is PostViewHolder -> {
                holder.likes.text = postAuction.likes.toString()
                holder.comments.text = postAuction.comments?.count.toString()
                holder.nickname.text = "@${postAuction.author?.nickname}"
                holder.name.text = postAuction.author?.name
                if (postAuction.author?.picture != "") {
                    Picasso.get().load(postAuction.author?.picture).into(holder.picture)
                }
                holder.time.text = postAuction.timestamp
                Picasso.get().load(postAuction.content).into(holder.content)
                holder.description.text = postAuction.description

//                val attrs = intArrayOf(R.attr.colorPrimary, R.attr.colorAccent)
//                val themeId = context.packageManager.getActivityInfo(componentName, 0).theme
//                val ta: TypedArray = obtainStyledAttributes(themeId, attrs)
                holder.like.addValueCallback(
                        KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                { PorterDuffColorFilter(context.getColor(
                    if(postAuction.liked!!)
                        R.color.colorPrimaryLight
                    else
                        R.color.colorSecondaryLight),
                    PorterDuff.Mode.SRC_ATOP) }
                )

                if(postAuction.liked!!) holder.like.playAnimation()

                holder.like.setOnClickListener {
//                   val attrs = intArrayOf(R.attr.colorPrimary, R.attr.colorAccent)
//                   val themeId = context.packageManager.getActivityInfo(componentName, 0).theme
//                   val ta: TypedArray = obtainStyledAttributes(themeId, attrs)

                    viewModel.likePost(postAuction.id)
                    holder.like.addValueCallback(
                        KeyPath("**"),
                        LottieProperty.COLOR_FILTER,
                        { PorterDuffColorFilter(context.getColor(
                            if(!holder.like.isAnimating)
                                R.color.colorSecondaryLight
                            else
                                R.color.colorPrimaryLight),
                            PorterDuff.Mode.SRC_ATOP) }
                    )

                    holder.likes.text = (Integer.parseInt(holder.likes.text as String) + if(!holder.like.isAnimating) 1 else -1).toString()

                    if(holder.like.isAnimating) {
                        holder.like.progress = 0F
                        holder.like.pauseAnimation()
                    } else
                        holder.like.playAnimation()

                    holder.like.isClickable = false
                    Handler().postDelayed({
                        holder.like.isClickable = true
                    }, 1100)
                }
                holder.menu.setOnClickListener { inflateMenu(holder.menu, R.menu.post_menu) }
                holder.content.setOnClickListener {
                    val intent = Intent("CANCANVAS_PHOTO").addCategory("CANCANVAS_PHOTO")
                        .putExtra("id", postAuction.id)
                        .putExtra("name", postAuction.author?.name)
                        .putExtra("nickname", postAuction.author?.nickname)
                        .putExtra("picture", postAuction.author?.picture)
                        .putExtra("content", postAuction.content)
                        .putExtra("comments", postAuction.comments?.count)
                        .putExtra("likes", Integer.parseInt(holder.likes.text as String))
                        .putExtra("liked", holder.like.isAnimating)
                    context.startActivity(intent)
                }
                holder.viewComments.setOnClickListener {
                    val intent = Intent("CANCANVAS_COMMENTS").addCategory("CANCANVAS_COMMENTS")
                        .putExtra("id", postAuction.id)
                    context.startActivity(intent)
                }
                holder.profile.setOnClickListener {
                    val intent = Intent("CANCANVAS_PROFILE").addCategory("CANCANVAS_PROFILE")
                        .putExtra("nickname", postAuction.author?.nickname)
                    context.startActivity(intent)
                }
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
        val bids: TextView = itemView.tvBids
        val time: TextView = itemView.tvTime
        val description: TextView = itemView.tvDescription
        val menu: ImageView = itemView.ivMenu
        val profile: LinearLayout = itemView.llProfile
    }

    class PostViewHolder(itemView: View): AuctionPostViewHolder(itemView) {
        val picture: ImageView = itemView.ivProfile
        val nickname: TextView = itemView.tvNickname
        val name: TextView = itemView.tvName
        val time: TextView = itemView.tvTime
        val comments: TextView = itemView.tvComments
        val viewComments: ImageView = itemView.ivComment
        val like: LottieAnimationView = itemView.ivLike
        val likes: TextView = itemView.tvLikes
        val content: ImageView = itemView.ivPost
        val description: TextView = itemView.tvDescription
        val menu: ImageView = itemView.ivMenu
        val profile: LinearLayout = itemView.llProfile
    }

    private fun inflateMenu(view: View, menu: Int) {
        val popup = PopupMenu(context, view)
        val inflater = popup.menuInflater
        inflater.inflate(menu, popup.menu)
        popup.show()
    }
}