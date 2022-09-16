package com.example.strayfriends.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.strayfriends.R
import com.example.strayfriends.databinding.FragmentCreateBinding
import com.example.strayfriends.viewmodel.FirestoreViewModel

class CreateFragment : Fragment(){
    private lateinit var binding : FragmentCreateBinding
    private lateinit var firestoreVM : FirestoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false)
        firestoreVM = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.createProductBtn.setOnClickListener {
            var productName = binding.createProductName.text.toString()
            var productType = binding.createProductType.text.toString()
            var productPrice = binding.createProductPrice.text.toString()
            var productDescription = binding.createProductDescription.text.toString()

            if (inputValid(productName, productType, productPrice)){
                firestoreVM.addProducts(
                    productName.trim(),
                    productPrice.trim(),
                    productDescription.trim(),
                    productType.trim()
                )
                binding.createProductName.setText("")
                binding.createProductType.setText("")
                binding.createProductPrice.setText("")
                binding.createProductDescription.setText("")
                Toast.makeText(context, "Anúncio enviado", Toast.LENGTH_SHORT).show()
                goToProductFragment()
            } else{
                binding.createProductName.error = "Preencha todos os campos"
                binding.createProductType.error = "Preencha todos os campos"
                binding.createProductPrice.error = "Preencha todos os campos"
            }
        }
    }

    private fun goToProductFragment() {
        findNavController().navigate(
            CreateFragmentDirections.actionCreateFragmentToProductsFragment()
        )
    }

    private fun inputValid(name : String, type : String, price : String) : Boolean{
        return name.trim() != "" && type.trim() != "" && price.trim() != ""
    }
}