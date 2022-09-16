package com.example.strayfriends.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.strayfriends.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = FirestoreRepository(application)

    fun addProducts(name : String, price : String, description : String, type : String){
        repository.addStoreData(name, price, description, type)
    }
}