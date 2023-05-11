package com.massimoregoli.democonstraints.connection
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class APIRequest(context: Context) {
    private var request: RequestQueue

    companion object {
        private var apiRequest: APIRequest? = null

        const val URL = "https://dummyjson.com/products?limit=100"
        fun getAPI(context: Context): APIRequest {
            if (apiRequest == null) {
                apiRequest = APIRequest(context)
            }
            return apiRequest!!
        }
    }

    init {
        request = Volley.newRequestQueue(context)
    }

    fun getProducts(onSuccess: (JSONObject?) -> Unit,
                    onFail: (VolleyError?) -> Unit) {
        val jsonLoader = JsonObjectRequest(URL,
            onSuccess,
            onFail)
        request.add(jsonLoader)

    }

    fun getThumbnail(s: String,
                     onSuccess: (Bitmap) -> Unit,
                     onFail: (VolleyError) -> Unit) {
        val bitmapRequest = ImageRequest(s,
            onSuccess,
            250, 166,
            null, Bitmap.Config.ARGB_8888,
            onFail)
        request.add(bitmapRequest)
        Log.w("XXX", s)
    }
}