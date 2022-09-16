package com.example.strayfriends.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.strayfriends.R
import com.example.strayfriends.databinding.FragmentDetailsBinding
import com.example.strayfriends.model.Product

class DetailsFragment : Fragment() {
    private lateinit var binding : FragmentDetailsBinding
    private lateinit var args : DetailsFragmentArgs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        args = DetailsFragmentArgs.fromBundle(requireArguments())

        setupUI(args.product)
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.detailsBackBtn.setOnClickListener {
            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToProductsFragment()
            )
        }
        binding.detailsBuyBtn.setOnClickListener {
            Toast.makeText(context, "Contate o vendedor pelo e-mail: ${args.product.userEmail}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUI(prod : Product) {
        binding.productDetailsName.text = prod.name
        binding.productDetailsOwner.text = "Vendedor: ${prod.owner}"
        binding.productDetailsType.text = prod.type
        binding.productDetailsType2.text = "Classifação: ${prod.type}"
        binding.productDetailsDescription.text = "Descrição: ${prod.description}"
        binding.productDetailsPrice.text = "R$${prod.price}"
    }
}