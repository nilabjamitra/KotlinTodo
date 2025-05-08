package com.example.kotlintodo.fragments

import android.content.Context
import android.content.Intent
import com.example.kotlintodo.MainActivity
import com.google.firebase.auth.FirebaseAuth


object AuthValidator {
    fun validateUser(auth: FirebaseAuth, context: Context): Boolean {
        return auth.currentUser?.let { true } ?: run {
            startLoginFlow(context)
            false
        }
    }

    private fun startLoginFlow(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("redirect_to", "login")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
