package studios.eaemenkk.cancanvas.repository;

import android.content.Context
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.subscription.SubscriptionConnectionParams
import com.apollographql.apollo.subscription.SubscriptionConnectionParamsProvider
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import studios.eaemenkk.cancanvas.R
import java.util.concurrent.TimeUnit

open class BaseApollo(private val context: Context, baseUrl: String, subscriptionUrl: String) {
        val apolloClient: ApolloClient
        init {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                val httpClient: OkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(AuthInterceptor(context))
                        .addInterceptor(logging)
                        .pingInterval(2, TimeUnit.SECONDS)
                        .build()
                apolloClient = ApolloClient.builder()
                        .serverUrl(baseUrl)
                        .okHttpClient(httpClient)
                        .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory(subscriptionUrl, httpClient))
                        .subscriptionConnectionParams {
                                val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                                val session = sharedPreferences.getString("session", null)
                                SubscriptionConnectionParams(mapOf("Authorization" to session))
                        }
                        .build()
        }

}

class AuthInterceptor(private val context: Context): Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                var request = chain.request()
                val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                val session = sharedPreferences.getString("session", null)
                if(session.isNullOrEmpty()) return chain.proceed(request)
                request = request
                        .newBuilder()
                        .addHeader("Authorization", session)
                        .build()
                return chain.proceed(request)
        }
}