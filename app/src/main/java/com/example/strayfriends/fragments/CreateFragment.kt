package com.example.strayfriends.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.strayfriends.R
import com.example.strayfriends.databinding.FragmentCreateBinding

class CreateFragment : Fragment(){
    private lateinit var binding : FragmentCreateBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false)

        return binding.root
    }
}