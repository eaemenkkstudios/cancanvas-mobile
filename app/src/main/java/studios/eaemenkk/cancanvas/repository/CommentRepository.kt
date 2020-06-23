package studios.eaemenkk.cancanvas.repository

import android.content.Context
import android.os.Handler
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.R
import studios.eaemenkk.cancanvas.domain.*
import studios.eaemenkk.cancanvas.mutations.CreateUserMutation
import studios.eaemenkk.cancanvas.queries.LoginQuery
import studios.eaemenkk.cancanvas.queries.TrendingQuery

class CommentRepository (val context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {

}