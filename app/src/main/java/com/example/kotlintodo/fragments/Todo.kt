package com.example.kotlintodo.fragments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = 0
)


    : Parcelable {
    constructor() : this("", "", "", 0)
}