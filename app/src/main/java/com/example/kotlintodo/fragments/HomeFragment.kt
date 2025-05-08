package com.example.kotlintodo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodo.R
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintodo.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupRecyclerView()
        loadTodos()
        registerEvents()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
    }


    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(
            todos = todoList,
            onEditClick = { todo -> showEditDialog(todo) },
            onDeleteClick = { todo -> showDeleteConfirmation(todo) }
        )

        binding.recyclerView.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
    private fun showEditDialog(todo: Todo) {
        val dialog = AddTodoPopupFragment().apply {
            arguments = Bundle().apply {
                putParcelable("EDIT_TODO", todo)
            }
            setOnTodoAddedListener { title, description, _ ->
                updateExistingTodo(todo, title, description)
            }
        }
        dialog.show(parentFragmentManager, "EditTodoDialog")
    }

    private fun showDeleteConfirmation(todo: Todo) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Todo")
            .setMessage("Are you sure you want to delete '${todo.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                deleteTodo(todo)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteTodo(todo: Todo) {
        val userId = auth.currentUser?.uid ?: return
        databaseRef.child("users").child(userId).child("todos").child(todo.id)
            .removeValue()
            .addOnSuccessListener {
                todoList.removeAll { it.id == todo.id }
                todoAdapter.notifyDataSetChanged()
                showSuccess("Todo deleted successfully")
            }
            .addOnFailureListener { e ->
                showError("Failed to delete todo: ${e.message}")
            }
    }

    private fun createNewTodo(title: String, description: String) {
        val userId = auth.currentUser?.uid ?: return
        val newTodo = Todo(
            title = title,
            description = description,
            timestamp = System.currentTimeMillis()
        )

        databaseRef.child("users").child(userId).child("todos").push()
            .setValue(newTodo)
            .addOnSuccessListener {
                ("Todo created successfully")
            }
            .addOnFailureListener { e ->
                showError("Failed to create todo: ${e.message}")
            }
    }

    private fun updateExistingTodo(oldTodo: Todo, title: String, description: String) {
        val updatedTodo = oldTodo.copy(
            title = title,
            description = description,
            timestamp = System.currentTimeMillis()
        )

        databaseRef.child("users").child(auth.currentUser?.uid ?: return)
            .child("todos").child(oldTodo.id)
            .setValue(updatedTodo)
            .addOnSuccessListener {
                val index = todoList.indexOfFirst { it.id == oldTodo.id }
                if (index != -1) {
                    todoList[index] = updatedTodo
                    todoAdapter.notifyItemChanged(index)
                }
                showSuccess("Todo updated successfully")
            }
            .addOnFailureListener { e ->
                showError("Failed to update todo: ${e.message}")
            }
    }

    private fun registerEvents() {
        binding.fabAdd.setOnClickListener {
            AddTodoPopupFragment().apply {
                setOnTodoAddedListener { title, description, todoId ->
                    if (todoId != null) {
                        // Handle edit (though not needed here)
                    } else {
                        // Handle new todo creation
                        createNewTodo(title, description)
                    }
                }
            }.show(parentFragmentManager, "AddTodoDialog")
        }

//        binding.btnLogout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
//        }
    }

    private fun loadTodos() {
        val userId = auth.currentUser?.uid ?: return
        databaseRef.child("users").child(userId).child("todos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded) return

                    todoList.clear()
                    for (childSnapshot in snapshot.children) {
                        val todo = childSnapshot.getValue(Todo::class.java)
                        todo?.let {
                            todoList.add(it.copy(id = childSnapshot.key ?: ""))
                        }
                    }
                    todoAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isAdded) {
                        activity?.runOnUiThread {
                            Snackbar.make(
                                binding.root, // Use binding.root instead of requireView()
                                "Failed to load todos: ${error.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }
    private fun saveFcmTokenToDatabase(token: String) {
        val userId = auth.currentUser?.uid ?: return
        databaseRef.child("users").child(userId).child("fcmToken")
            .setValue(token)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("FCM", "Token saved to database")
                }
            }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Device token: $token")
            }
        }
    }
    private fun showSuccess(message: String) {

        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }

    private fun showError(message: String) {

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()

    }

}