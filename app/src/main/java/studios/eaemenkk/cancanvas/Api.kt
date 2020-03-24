package studios.eaemenkk.cancanvas

import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class Api (private val client: OkHttpClient) {
    fun get(url: String, token: String = "", callback: Callback): Call {

        val headers = Headers.Builder()
        if(token.isNotEmpty()) {
            headers.add("token", token)
        }

        val request = Request.Builder()
            .url(baseURL + url)
            .headers(headers.build())
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun post(url: String, parameters: JSONObject, token: String = "",  callback: Callback): Call {

        val jsonString = parameters.toString()
        val body = jsonString.toRequestBody(JSON)
        val headers = Headers.Builder()
        headers.add("Content-Type", "application/json")
        if(token.isNotEmpty()) {
            headers.add("token", token)
        }

        val request = Request.Builder()
            .url(baseURL + url)
            .headers(headers.build())
            .post(body)
            .build()


        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    private val baseURL = "http://192.168.0.10:8080"

    companion object {
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }
}