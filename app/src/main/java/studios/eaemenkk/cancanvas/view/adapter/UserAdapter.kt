package studios.eaemenkk.cancanvas.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_card.view.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.User

class UserAdapter(private val context: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var dataSet = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataSet[position]

        holder.itemView.setOnClickListener {
            val intent = Intent("CANCANVAS_PROFILE").addCategory("CANCANVAS_PROFILE")
                .putExtra("nickname", user.nickname)
            context.startActivity(intent)
        }
        holder.nickname.text = "@${user.nickname}"
        holder.name.text = user.name
        if (user.picture?.isNotEmpty()!!) Picasso.get().load(user.picture).into(holder.picture)
    }

    fun setUsers(users: ArrayList<User>) {
        dataSet = users
        notifyDataSetChanged()
    }

    fun addUsers(users: ArrayList<User>) {
        dataSet.addAll(users)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val picture: ImageView = itemView.ivProfile
        val nickname: TextView = itemView.tvNickname
        val name: TextView = itemView.tvName
        val menu: ImageView = itemView.ivMenu
    }
}