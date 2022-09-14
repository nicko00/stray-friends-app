package com.example.strayfriends.fragments

import android.app.Activity.RESULT_CANCELED
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strayfriends.HomeActivity
import com.example.strayfriends.MainActivity
import com.example.strayfriends.NavGraphDirections
import com.example.strayfriends.R
import com.example.strayfriends.databinding.FragmentLoginBinding
import com.example.strayfriends.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(){
    private lateinit var binding : FragmentLoginBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var authVM : AuthViewModel
    private lateinit var mAuth : FirebaseAuth
    private lateinit var firestoreDB : FirebaseFirestore
    private lateinit var gso : GoogleSignInOptions
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        authVM = ViewModelProvider(this).get(AuthViewModel::class.java)
        firestoreDB = Firebase.firestore
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("629872074206-62452rc09erkmn024pu0gi9pjk9drktc.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mAuth = FirebaseAuth.getInstance()

        setupListeners()

        return binding.root
    }

    private fun setupListeners(){
        binding.registerLink.setOnClickListener{
            goToRegisterFragment()
        }
        binding.loginEmailButton.setOnClickListener {
            var email = binding.emailLoginEdit.text.toString()
            var password = binding.passwordLoginEdit.text.toString()

            if (isCredValid(email, password)){
                authVM.handleLogin(email, password)
            } else{
                binding.emailLoginEdit.error = "Insira as credenciais corretamente"
                binding.passwordLoginEdit.error = "Insira as credenciais corretamente"
            }
        }
        binding.loginGoogleBtn.setOnClickListener {
            loginGoogle()
        }
    }

    private fun goToRegisterFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        )
    }

    private fun isCredValid(email: String, password: String): Boolean {
        return email.trim() != "" && password.trim() != "" && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun loginGoogle(){
        val signInIntent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != RESULT_CANCELED) {
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

    private fun goToHomeActivity(){
        val i : Intent = Intent(activity, HomeActivity::class.java)
        startActivity(i)
    }
}