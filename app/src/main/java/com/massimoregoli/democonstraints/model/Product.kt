package com.massimoregoli.democonstraints.model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
//    var bitmap: ImageBitmap? = null,
//    var isLoaded: Boolean
) : Comparable<Product> {
    override fun compareTo(other: Product): Int {
        return compareValuesBy(this, other, {it.category.lowercase()}, {it.title.lowercase()})
    }
}

class Products {
    var products: MutableList<Product> = mutableListOf()
}
