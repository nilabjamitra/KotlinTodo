package com.example.kotlintodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kotlintodo.R
import com.example.kotlintodo.databinding.FragmentAddTodoPopupBinding

class AddTodoPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopupBinding
    private var onTodoAddedListener: OnTodoAddedListener? = null

    fun interface OnTodoAddedListener {
        fun onTodoAdded(title: String, description: String,todoId: String?)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    fun setOnTodoAddedListener(listener: OnTodoAddedListener) {
        this.onTodoAddedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupButtons()
    }

    private fun setupDialog() {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(true)
    }

    private fun setupButtons() {
        // Save Button
        binding.btnSave.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()

            if (validateInput(title)) {
                onTodoAddedListener?.onTodoAdded(title, description,null)
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validateInput(title: String): Boolean {
        return if (title.isEmpty()) {
            binding.titleInputLayout.error = "Title cannot be empty"
            false
        } else {
            binding.titleInputLayout.error = null
            true
        }

    }
}