package com.example.strayfriends.repository

import android.app.Application
import android.util.Log
import com.example.strayfriends.adapter.ProductsAdapter
import com.example.strayfriends.model.Product
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class FirestoreRepository (application : Application){
    private val firestoreDB : FirebaseFirestore = Firebase.firestore
    private lateinit var product : Product
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val uid = mAuth.currentUser?.email
    private val account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(application)
    private val userAcc = account?.email
    private val documentFS = (uid ?: userAcc) as String
    private val documentId = (mAuth.currentUser?.uid ?: account?.id)

    private fun addProduct (name : String, id : Long, email : String, owner : String, price : String, description : String, type : String ) {
        val productObj = Product((id+1), email, name, price, description, type, owner)
        firestoreDB.collection("products").document("${id+1}").set(productObj).addOnCompleteListener { task->
            if (task.isSuccessful){
                Log.d("SUCESSOOOOOOOOOOOOOOO", "OOOOOOOOOOOOOOOOOOOOOOOO")
            } else {
                Log.d("fracasso", "no sex")
            }
        }
    }

    fun addStoreData(name : String, price : String, description : String, type : String){
        val doc = firestoreDB.collection("products")
        doc.get().addOnSuccessListener { documents ->
            var count : Long = 0
            val email = documentFS
            for(document in documents) {
                count++
            }
            firestoreDB.collection("users").document("$documentId").get().addOnSuccessListener { documents2->
                val userName = documents2.getString("userName")

                if (userName != null) {
                    addProduct(name, count, email, userName, price, description, type)
                }
            }
        }
    }

}