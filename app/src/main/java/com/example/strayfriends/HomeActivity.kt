package com.example.strayfriends

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.strayfriends.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        Log.d("---------uid: ", "${mAuth.currentUser?.uid}")
    }
}