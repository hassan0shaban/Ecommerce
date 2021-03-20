package com.whiteside.cafe.ui.product

import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.cafe.model.Item
import com.whiteside.cafe.model.Product

class ProductPresenter {
    var listener: OnGetProductListener? = null
    fun setListener(listener: OnGetProductListener?) {
        this.listener = listener
    }

    private fun getProductFromFirebase(item: Item?) {
        val fStore = FirebaseFirestore.getInstance()
        fStore.collection("Categories")
            .document(item.getCategoryName())
            .collection("Products")
            .document(item.getProductId())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val ds = task.result
                    val product = ds.toObject(Product::class.java)
                    product.setProductId(ds.id)
                    listener.onGetProductSuccess(product)
                } else {
                    listener.onGetProductFailed(task.exception)
                }
            }
    }

    fun getProduct(item: Item?) {
        getProductFromFirebase(item)
    }

    fun getProductInfo(categoryName: String?, productId: String?) {
        val item = Item()
        item.categoryName = categoryName
        item.productId = productId
        getProduct(item)
    }
}