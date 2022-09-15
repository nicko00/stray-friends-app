package com.example.strayfriends.repository

import android.app.Application
import android.util.Log
import com.example.strayfriends.adapter.ProductsAdapter
import com.example.strayfriends.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class FirestoreRepository (application : Application){
    private val firestoreDB : FirebaseFirestore = Firebase.firestore
    private lateinit var listProduct : List<Product>

    fun getProducts() {

    }
}