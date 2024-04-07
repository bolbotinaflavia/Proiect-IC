package com.example.homemanagement.data.model

data class Component(
    val id: Int,
    val name: String,
    val photo: String?,
    val room: Room,
    val elements: List<Element>
)
