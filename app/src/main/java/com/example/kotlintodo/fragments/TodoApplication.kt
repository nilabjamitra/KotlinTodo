package com.example.kotlintodo

import android.app.Application
import com.google.firebase.FirebaseApp

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}