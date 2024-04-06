package data

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val userId: Int,
    val name: String,
    val age: Int
)
