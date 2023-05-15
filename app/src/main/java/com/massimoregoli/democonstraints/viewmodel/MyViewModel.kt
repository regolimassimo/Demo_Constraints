package com.massimoregoli.democonstraints.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.massimoregoli.democonstraints.connection.APIRequest
import com.massimoregoli.democonstraints.model.Product
import com.massimoregoli.democonstraints.model.Products
import org.json.JSONObject


class MyViewModel(private var app: Application): AndroidViewModel(app) {

    val productList = MutableLiveData<MutableList<Product>>()

    private fun getData() {
        val queue = APIRequest.getAPI(app)
        queue.getProducts({
            val l = unpackProduct(it)
            l.sort()
            productList.postValue(l)
        }, {
            Log.w("XXX", "VolleyError")
        })
    }

    init {
        getData()
    }

    private fun unpackProduct(it: JSONObject?): MutableList<Product> {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        val ret = gson.fromJson(json, Products::class.java)
        return ret.products
    }

    fun getThumbnail(index: Int, onSuccess: () -> Unit) {
        val queue = APIRequest.getAPI(app)
        if (!productList.value?.get(index)!!.isLoaded) {
            productList.value?.get(index)!!.isLoaded = true
            queue.getThumbnail(productList.value?.get(index)!!.thumbnail,
                {
                    productList.value?.get(index)!!.bitmap = it.asImageBitmap()
                    onSuccess()
                },
                {
                    Log.w("XXX", "VolleyError")
                })
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ToDoViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(application) as T
    }
}