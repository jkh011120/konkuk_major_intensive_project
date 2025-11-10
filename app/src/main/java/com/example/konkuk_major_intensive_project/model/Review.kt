package com.example.konkuk_major_intensive_project.model

data class Review(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val content: String,
    val rating: Int
)