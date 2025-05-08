package com.example.kotlintodo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.kotlintodo.R
import com.example.kotlintodo.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSplashBinding
    private lateinit var checkAuthState: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSplashBinding.inflate(layoutInflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.myLooper()!!).postDelayed({

            checkAuthState()

        }, 2000)
    }
    private fun checkAuthState() {
        val pendingIntent = activity?.intent?.getParcelableExtra<Intent>("pendingIntent")
        val currentUser = auth.currentUser

        if (currentUser != null) {
            handlePendingIntent(pendingIntent)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }

    private fun handlePendingIntent(pendingIntent: Intent?) {
        pendingIntent?.let {
            // Handle deep link or specific notification action
            startActivity(it)
            activity?.finish()
        } ?: run {
            // Default navigation
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
        if (!AuthValidator.validateUser(auth, requireContext()))
        {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            // User not authenticated, flow will redirect automatically
            return

        }
    }
}
