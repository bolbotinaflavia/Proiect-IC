package com.example.homemanagement.data.model

data class Task(
    val id: Int,
    val name: String,
    val date: String,
    val room: Room,
    val description: String?
)
