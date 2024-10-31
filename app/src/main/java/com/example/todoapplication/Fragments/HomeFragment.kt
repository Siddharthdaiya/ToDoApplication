package com.example.todoapplication.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.utils.adapter.TodoAdapter
import com.example.todoapplication.Utils.TodoData
import com.example.todoapplication.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), AddTodoPopUpFragment.DialogNextBtnClickListner,
    TodoAdapter.TaskAdapterInterface {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddTodoPopUpFragment? = null
    private lateinit var adapter: TodoAdapter
    private lateinit var toDoItemList: MutableList<TodoData>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()


    }

    @SuppressLint("SuspiciousIndentation")
    private fun registerEvents() {
        binding.addBtnHome.setOnClickListener {
            if (popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddTodoPopUpFragment()
            popUpFragment!!.setListner(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddTodoPopUpFragment.TAG
            )
        }
    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoItemList = mutableListOf()
        adapter = TodoAdapter(requireContext(), toDoItemList)
        adapter.setListener(this)
        binding.mainRecyclerView.adapter = adapter

        sharedPreferences = requireContext().getSharedPreferences(
            requireContext().packageName,
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
//        Const.level = sharedPreferences.getString("level", "medium").toString()
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        TodoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        toDoItemList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Todo saved successfully", Toast.LENGTH_SHORT).show()
                todoEt.text = null
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(todoData: TodoData, todoEt: TextInputEditText) {

        val map = HashMap<String, Any>()
        map[todoData.taskId] = todoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
            todoEt.text = null
            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteItemClicked(toDoData: TodoData, position: Int) {

        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onEditItemClicked(toDoData: TodoData, position: Int) {
        if (popUpFragment != null) {
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

            popUpFragment = AddTodoPopUpFragment.newInstance(toDoData.taskId, toDoData.task)
            popUpFragment!!.setListner(this)
            popUpFragment!!.show(childFragmentManager, AddTodoPopUpFragment.TAG)
        }

    }


}