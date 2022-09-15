package com.example.strayfriends.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.strayfriends.repository.FirestoreRepository

class FirestoreViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = FirestoreRepository(application)

    fun getProducts(){
        repository.getProducts()
    }
}