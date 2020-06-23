package studios.eaemenkk.cancanvas.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_card.view.ivLike
import kotlinx.android.synthetic.main.comment_card.view.ivProfile
import kotlinx.android.synthetic.main.comment_card.view.tvLikes
import kotlinx.android.synthetic.main.comment_card.view.tvName
import kotlinx.android.synthetic.main.comment_card.view.tvNickname
import kotlinx.android.synthetic.main.comment_card.view.tvTime
import kotlinx.android.synthetic.main.post_card.view.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.Comment

class CommentAdapter(private val context: Context): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var dataSet = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_card, parent, false)
        return CommentViewHolder(view)
    }

    class CommentViewHolder(itemView: View) : PostAdapter.AuctionPostViewHolder(itemView) {
        val picture: ImageView = itemView.ivProfile
        val nickname: TextView = itemView.tvNickname
        val name: TextView = itemView.tvName
        val time: TextView = itemView.tvTime
        val like: ImageView = itemView.ivLike
        val likes: TextView = itemView.tvLikes
        val content: ImageView = itemView.ivPost
        val menu: ImageView = itemView.ivMenu
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun addComment(index: Int, comment: Comment?) {
        if(comment != null) {
            dataSet.add(comment)
            notifyDataSetChanged()
        }
    }

    fun addComments(comments: ArrayList<Comment>?) {
        comments?.forEachIndexed { index, comment -> addComment(index, comment) }
    }

    fun setComment(comments: ArrayList<Comment>?) {
        if(comments != null) {
            dataSet = ArrayList()
            comments.forEachIndexed { index, comment -> addComment(index, comment)
            }
        }
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val comment = dataSet[position]

        holder.name.text = comment.author?.name
        holder.nickname.text = comment.author?.nickname
        Picasso.get().load(comment.author?.picture).into(holder.picture)
        holder.likes.text = comment.likes.toString()
        holder.time.text = comment.timestamp
    }
}