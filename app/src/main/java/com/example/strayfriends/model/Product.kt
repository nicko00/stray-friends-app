package com.example.strayfriends.model

import java.io.Serializable
import com.google.gson.annotations.SerializedName


data class Product (
    val id : Long = 0,
    val userId : String = "",
    val name : String = "",
    val price : String = "",
    val description : String = "",
    val type : String = "",
    val owner : String = "",
)