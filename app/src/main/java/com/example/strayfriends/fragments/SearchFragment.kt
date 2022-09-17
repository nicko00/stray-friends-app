package com.example.strayfriends.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.strayfriends.R
import com.example.strayfriends.adapter.ProductsAdapter
import com.example.strayfriends.databinding.FragmentSearchBinding
import com.example.strayfriends.listener.ProductOnClickListener
import com.example.strayfriends.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment(), ProductOnClickListener {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter : ProductsAdapter
    private lateinit var listProduct : MutableList<Product>
    private val firestoreDB : FirebaseFirestore = Firebase.firestore
    private var page = 1
    var count = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        setupAdapter()
        setupListeners()

        return binding.root
    }

    private fun setupAdapter() {
        val productsRecycler = binding.recyclerViewSearch
        adapter = ProductsAdapter(this)

        productsRecycler.layoutManager  = LinearLayoutManager(context)
        productsRecycler.adapter = adapter
    }

    private fun setupListeners() {
        binding.searchProductInput.doAfterTextChanged { text->
            var textSearch = text.toString()
            if (textSearch != ""){
                populateRecyclerSearch(textSearch)
                binding.progressBarSearch.visibility = View.VISIBLE
                binding.waitingTextSearch.text = ""
            } else{
                binding.progressBarSearch.visibility = View.GONE
                binding.waitingTextSearch.text = "Insira o nome ou o tipo de produto que deseja comprar"
                adapter.deleteList()
            }
        }
        binding.floatingActionButton.setOnClickListener {
            populateRecyclerFilter()
        }
    }

    private fun populateRecyclerSearch(text : String) {
        firestoreDB.collection("products")
            .get().addOnCompleteListener { task->
            if (task.isSuccessful){
                adapter.deleteList()
                val document = task.result
                if (!document.isEmpty) {
                    if (document != null) {
                        for (documents in document) {
                            var name = documents.getString("name")
                            var type = documents.getString("type")
                            if (name != null && type != null) {
                                if (name.contains(text, ignoreCase = true) || type.contains(text, ignoreCase = true)) {
                                    adapter.submitProduct(documents.toObject(Product::class.java))
                                    getPage(text)
                                } else {
                                    showNoProductsMsg()
                                }
                            }
                        }
                    }
                    binding.progressBarSearch.visibility = View.INVISIBLE
                } else {
                    showNoProductsMsg()
                }
            } else{
                showNoProductsMsg()
            }
        }
    }

    private fun getPage(text : String) {
        binding.recyclerViewSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount
                if ((visibleItemCount + pastVisibleItem) >= total) {
                    binding.recyclerViewSearch.removeOnScrollListener(this)
                    page++
                    populateRecyclerSearch(text)
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun populateRecyclerFilter(){
        Toast.makeText(context, "Em breve", Toast.LENGTH_SHORT).show()
    }

    private fun showNoProductsMsg(){
        binding.waitingTextSearch.text = "Nenhum produto encontrado, tente novamente"
        binding.progressBarSearch.visibility = View.GONE
    }

    override fun onItemClick(product: Product) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(product)
        )
    }
}