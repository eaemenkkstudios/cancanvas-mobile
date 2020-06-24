package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_search.*
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.mutations.CreatePostMutation
import studios.eaemenkk.cancanvas.queries.UsersByTagsQuery
import studios.eaemenkk.cancanvas.repository.BaseApollo
import studios.eaemenkk.cancanvas.viewmodel.UserViewModel

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val baseApollo = BaseApollo(this, getString(R.string.api_base_url), getString(R.string.api_subscription_url))
        val tags = ArrayList<String>()
        tags.add(etSearch.text.toString())

        baseApollo.apolloClient.query(UsersByTagsQuery(tags, Input.optional(1)))
            .enqueue(object : ApolloCall.Callback<UsersByTagsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    println("Apollo Error $e")
                }
                override fun onResponse(response: Response<UsersByTagsQuery.Data>) {
                    println(response.errors?.get(0)?.message)
                }
            })
    }
}