package com.example.strayfriends.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.strayfriends.R
import com.example.strayfriends.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel (application : Application) : AndroidViewModel(application) {
    private val repository = AuthenticationRepository(application)

    fun handleRegister(email : String, password : String, user : String) {
        repository.registerAcc(email, password, user)
    }

    fun handleLogin(email : String, password : String) {
        repository.loginAcc(email, password)
    }

    fun ifLogged() : Boolean {
        return repository.isLogged()
    }

    fun handleLogout() {
        repository.logOut()
    }
}