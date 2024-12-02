package com.example.android.firstresponse

data class FirstAidKit(
    val imageResId: Int,        // Resource ID for the first aid kit image
    val title: String,          // Title of the first aid kit item
    val descriptionResId: Int,  // Resource ID for the description text
    val shopeeUrl: String,      // Shopee URL for purchasing
    val lazadaUrl: String,      // Lazada URL for purchasing
    val temuUrl: String         // Temu URL for purchasing
)
