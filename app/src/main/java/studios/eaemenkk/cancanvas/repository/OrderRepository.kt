package studios.eaemenkk.cancanvas.repository

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import studios.eaemenkk.cancanvas.queries.BidPaymentLinkQuery

class OrderRepository(context: Context, baseUrl: String, subscriptionUrl: String): BaseApollo(context, baseUrl, subscriptionUrl) {

    fun getOrderLink(auctionId: String, bidId: String, callback: (link: String) -> Unit) {
        apolloClient.query(BidPaymentLinkQuery(auctionId, bidId)).enqueue(object: ApolloCall.Callback<BidPaymentLinkQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                println(e)
            }

            override fun onResponse(response: Response<BidPaymentLinkQuery.Data>) {
                response.data?.bidPaymentLink?.let { callback(it) }
            }

        })
    }
}