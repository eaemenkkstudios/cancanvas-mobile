package studios.eaemenkk.cancanvas

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class Api (private val client: OkHttpClient) {
    fun get(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(baseURL + url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun post(url: String, parameters: JSONObject, header: Boolean,  callback: Callback): Call {

        val jsonString = parameters.toString()
        val body = jsonString.toRequestBody(JSON)

        val request = Request.Builder()
            .url(baseURL + url)
            .header("Content-Type", "application/json")
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