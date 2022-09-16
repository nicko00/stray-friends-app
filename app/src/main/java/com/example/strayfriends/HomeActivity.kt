package com.example.strayfriends

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.strayfriends.databinding.ActivityHomeBinding
import com.example.strayfriends.model.UserModel
import com.example.strayfriends.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var authVM : AuthViewModel
    private lateinit var firestoreDB : FirebaseFirestore
    private lateinit var userName : String
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var documentFS : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        authVM = ViewModelProvider(this).get(AuthViewModel::class.java)
        val uid = mAuth.currentUser?.uid
        val account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        val userAcc = account?.id
        documentFS = (uid ?: userAcc) as String
        firestoreDB = Firebase.firestore

        supportActionBar?.hide()

        val navHost = supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
        navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.productsFragment, R.id.createFragment, R.id.searchFragment))
        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        updateUI()
        setupListeners()
    }

    private fun updateUI() {
        val doc = firestoreDB.collection("users").document(documentFS.toString())
        doc.get().addOnSuccessListener { document ->
            userName = document.getString("userName").toString()
            Log.d("data", "$userName")

            binding.userNameTextView.text = userName
        }
    }

    private fun setupListeners() {
        binding.signOutBtn.setOnClickListener {
            authVM.handleLogout()
        }
    }
}