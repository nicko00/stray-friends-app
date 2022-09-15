package com.example.strayfriends.repository

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.strayfriends.HomeActivity
import com.example.strayfriends.MainActivity
import com.example.strayfriends.NavGraphDirections
import com.example.strayfriends.model.UserModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AuthenticationRepository (private var application : Application){

    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = mAuth.currentUser?.uid
    private var firestoreDb = Firebase.firestore
    private val account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(application)

    fun registerAcc(email : String, password : String, user : String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                task->
            if (task.isSuccessful){
                val userObj = hashMapOf("userName" to user)
                uid = mAuth.currentUser?.uid.toString()
                Toast.makeText(application, "Conta criada com sucesso", Toast.LENGTH_SHORT).show()
                firestoreDb.collection("users").document(uid!!).set(userObj)
            }
            else{
                Log.d("Firebase", "${task.exception}")
                if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Toast.makeText(application, "Senha muito fraca, tente usar uma mais segura", Toast.LENGTH_SHORT).show()
                    } catch (e : FirebaseAuthUserCollisionException)  {
                        Toast.makeText(application, "Usuário já existente, por favor, escolha outro", Toast.LENGTH_SHORT).show()
                    } catch (e : Exception) {
                        Toast.makeText(application, "Houve um erro, tente novamente mais tarde", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun loginAcc(email : String, password : String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                task->
            if (task.isSuccessful){
                Toast.makeText(application, "Bem vindo", Toast.LENGTH_SHORT).show()
                Log.d("Firebase", "deu bom")
                logUserToken()
                goToHomeActivity()
            }
            else{
                if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(application, "Credenciais Inválidas, tente novamente", Toast.LENGTH_SHORT).show()
                    } catch (e : FirebaseAuthInvalidUserException)  {
                        Toast.makeText(application, "Credenciais Inválidas, tente novamente", Toast.LENGTH_SHORT).show()
                    } catch (e : Exception) {
                        Toast.makeText(application, "Houve um erro, tente novamente mais tarde", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun getUserName(){
        var userName : String?
        val doc = firestoreDb.collection("users").document(uid!!)
        doc.get().addOnSuccessListener { document ->
            val user = document.toObject(UserModel::class.java)
            userName = user?.userName
            Log.d("Usename: ", "$userName")
        }
    }

    fun logOut(){
        mAuth.signOut()
        goToMainActivity()
        NavGraphDirections.actionGlobalLoginFragment()
        logUserToken()
    }

    fun isLogged() : Boolean {
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return user != null
    }

    private fun goToHomeActivity(){
        val i = Intent(application, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        application.startActivity(i)
    }

    private fun goToMainActivity(){
        val i = Intent(application, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        application.startActivity(i)
    }

    private fun logUserToken(){
        Log.d("Firebase", "${if(mAuth.currentUser != null) uid else account?.id}")
    }
}