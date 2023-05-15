package com.massimoregoli.democonstraints.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.massimoregoli.democonstraints.connection.APIRequest
import com.massimoregoli.democonstraints.model.Product
import com.massimoregoli.democonstraints.model.Products
import org.json.JSONObject


class MyViewModel(private var app: Application) : AndroidViewModel(app) {

    val productList = MutableLiveData<MutableList<Product>>()

    fun getData(onError: (String) -> Unit) {
        val queue = APIRequest.getAPI(app)
        queue.getProducts({
            val l = unpackProduct(it)
            l.sort()
            productList.postValue(l)
        }, {
            Log.w("XXX", "VolleyError")
            if (it?.message != null)
                onError(it.message!!)
            else
                onError("Network Error")
        })
    }


    private fun unpackProduct(it: JSONObject?): MutableList<Product> {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        val ret = gson.fromJson(json, Products::class.java)
        return ret.products
    }
}

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(application) as T
    }
}