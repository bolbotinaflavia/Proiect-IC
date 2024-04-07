package com.example.homemanagement.data.model

data class Room(
    val id: Int,
    val name: String,
    val photo: String?,
    val description: String?,
    val tasks: List<Task>,
    val components: List<Component>
)