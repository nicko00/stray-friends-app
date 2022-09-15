package com.example.strayfriends.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.strayfriends.R
import com.example.strayfriends.adapter.ProductsAdapter
import com.example.strayfriends.databinding.FragmentProductsBinding
import com.example.strayfriends.model.Product
import com.example.strayfriends.repository.FirestoreRepository
import com.example.strayfriends.viewmodel.AuthViewModel
import com.example.strayfriends.viewmodel.FirestoreViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductsFragment : Fragment(){
    private lateinit var binding : FragmentProductsBinding
    private lateinit var productsAdapter : ProductsAdapter
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var dbVM : FirestoreViewModel
    private var page = 1
    private val firestoreDB : FirebaseFirestore = Firebase.firestore
    private lateinit var listProduct : List<Product>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        dbVM = ViewModelProvider(this)[FirestoreViewModel::class.java]

        val scrollView = binding.scrollView

        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.isSmoothScrollingEnabled=true

        setupAdapter()
        getProduct()

        return binding.root
    }

    private fun setupAdapter() {
        val productsRecycler = binding.productsRecyclerView
        productsAdapter = ProductsAdapter()

        productsRecycler.layoutManager  = LinearLayoutManager(context)
        productsRecycler.adapter = productsAdapter
    }

    fun getProduct() {
        firestoreDB.collection("products").get().addOnCompleteListener { task->
            if (task.isSuccessful){
                val document = task.result
                if(!document.isEmpty){
                    listProduct = document.toObjects(Product::class.java)
                    productsAdapter.submitList(listProduct)
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun getPage() {
        binding.productsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = productsAdapter.itemCount
                if ((visibleItemCount + pastVisibleItem) >= total) {
                    binding.productsRecyclerView.removeOnScrollListener(this)
                    page++
                    getProduct()
                }

                super.onScrolled(recyclerView, dx, dy)

            }

        })
    }
}