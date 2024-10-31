package com.example.todoapplication.Fragments

import android.app.DialogFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoapplication.Utils.TodoData
import com.example.todoapplication.databinding.FragmentAddTodoPopUpBinding
import com.google.android.material.textfield.TextInputEditText


class AddTodoPopUpFragment : androidx.fragment.app.DialogFragment() {
    private lateinit var binding: FragmentAddTodoPopUpBinding
    private lateinit var listner: DialogNextBtnClickListner
    private var toDoData: TodoData? = null

    fun setListner(listner: HomeFragment) {
        this.listner = listner
    }

    companion object {
        const val TAG = "DialogFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) =
            AddTodoPopUpFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTodoPopUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            toDoData =
                TodoData(
                    arguments?.getString("taskId").toString(),
                    arguments?.getString("task").toString()
                )
            binding.todoEt.setText(toDoData?.task)
        }

        registerEvents()

    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {
                if (toDoData==null){
                    listner.onSaveTask(todoTask, binding.todoEt)
                }else{
                    toDoData?.task=todoTask
                    listner.onUpdateTask(toDoData!!,binding.todoEt)
                }
            } else {
                Toast.makeText(context, "Please type some Task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListner {
        fun onSaveTask(todo: String, todoEt: TextInputEditText)
        fun onUpdateTask(todoData: TodoData, todoEt: TextInputEditText)

    }
}