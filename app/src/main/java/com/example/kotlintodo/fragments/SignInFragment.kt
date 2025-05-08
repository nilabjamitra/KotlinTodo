package com.example.kotlintodo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.kotlintodo.R
import com.example.kotlintodo.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()



        init(view)
        setupClickListeners()
    }

    private fun init(view: View) {
        navController = view.findNavController()
        auth = FirebaseAuth.getInstance()
    }

    private fun setupClickListeners() {
        // Handle register redirect
        binding.registerRedirect.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        // Handle login button click
        binding.nxtBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showToast()
                false
            }

            password.isEmpty() -> {
                showToast()
                false
            }

            password.length < 6 -> {
                showToast()
                false
            }

            else -> true
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handlePostLoginNavigation()

                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)

                } else {
                    Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.progressBar.visibility = View.GONE
            }
    }
    private fun handlePostLoginNavigation() {
        val pendingIntent = activity?.intent?.getParcelableExtra<Intent>("pendingIntent")

        pendingIntent?.let {
            startActivity(it)
            activity?.finish()
        } ?: run {
        }
    }


    private fun showToast() {
        Toast.makeText(requireContext(), "Empty fields are not allowed", Toast.LENGTH_SHORT)
            .show()
    }

}
