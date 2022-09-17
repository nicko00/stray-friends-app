package com.example.strayfriends.fragments

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.strayfriends.HomeActivity
import com.example.strayfriends.NavGraphDirections
import com.example.strayfriends.R
import com.example.strayfriends.databinding.FragmentRegisterBinding
import com.example.strayfriends.repository.AuthenticationRepository
import com.example.strayfriends.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment () : Fragment() {
    private lateinit var binding : FragmentRegisterBinding
    private lateinit var authVM : AuthViewModel
    private lateinit var repository : AuthenticationRepository
    private lateinit var firestoreDB : FirebaseFirestore
    private lateinit var gso : GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestoreDB = Firebase.firestore
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        authVM = ViewModelProvider(this).get(AuthViewModel::class.java)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("629872074206-62452rc09erkmn024pu0gi9pjk9drktc.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.loginLink.setOnClickListener {
            goToLoginFragment()
        }
        binding.registerEmailButton.setOnClickListener {
            var email = binding.emailRegisterEdit.text.toString().trim()
            var password = binding.passwordRegisterEdit.text.toString().trim()
            var user = binding.userNameRegisterEdit.text.toString()

            if (isCredValid(email, password, user)){
                authVM.handleRegister(email, password, user)
            } else{
                binding.emailRegisterEdit.error = "Insira as credenciais corretamente"
                binding.passwordRegisterEdit.error = "Insira as credenciais corretamente"
                binding.userNameRegisterEdit.error = "Insira as credenciais corretamente"
            }
        }

        binding.registerGoogleBtn.setOnClickListener {
            loginGoogle()
        }

        firestoreDB.collection("users").addSnapshotListener{ snapshot, e ->
            if(e != null) Log.e("Firestore", "Falha no snapshot")

            if (snapshot != null && !snapshot.isEmpty && !snapshot.metadata.isFromCache && snapshot.metadata.hasPendingWrites()){
                goToLoginFragment()
                Log.d("Firestore", "$snapshot -- ${snapshot.metadata}")
            }
        }
    }

    private fun isCredValid(email: String, password: String, user : String): Boolean {
        return email.trim() != "" && password.trim() != "" && user.trim() != "" && user.trim().length >= 5 && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun goToLoginFragment(){
        findNavController().navigate(
            NavGraphDirections.actionGlobalLoginFragment()
        )
    }

    private fun loginGoogle(){
        val signInIntent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != Activity.RESULT_CANCELED) {
            if (requestCode == 1000 && data != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account: GoogleSignInAccount? =
                        GoogleSignIn.getLastSignedInAccount(requireActivity())

                    if (account != null) {
                        val user = hashMapOf("userName" to account.displayName.toString())
                        firestoreDB.collection("users").document(account.id.toString()).set(user)
                    }
                    goToHomeActivity()
                    Toast.makeText(activity, "Bem vindo", Toast.LENGTH_LONG).show()
                } catch (exc: ApiException) {
                    Toast.makeText(
                        activity,
                        "Algo deu errado, tente novamente mais tarde",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun goToHomeActivity() {
        val i : Intent = Intent(activity, HomeActivity::class.java)
        startActivity(i)
    }
}