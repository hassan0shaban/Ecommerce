package com.whiteside.cafe.api.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface CategoryRepository {

    fun getProducts(categoryName: String): Task<QuerySnapshot>
}