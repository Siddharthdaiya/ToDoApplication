package com.example.kotlintodopractice.utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.Utils.TodoData
import com.example.todoapplication.databinding.TodoitemBinding

class TodoAdapter(private val ctx: Context,private val list: MutableList<TodoData>) :
    RecyclerView.Adapter<TodoAdapter.TaskViewHolder>() {

    private  val TAG = "TaskAdapter"
    private var listener:TaskAdapterInterface? = null
    fun setListener(listener:TaskAdapterInterface){
        this.listener = listener
    }
  inner class TaskViewHolder(val binding: TodoitemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
     val binding=
         TodoitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.setText(this.task)
//                when (Const.level) {
//                    "medium" -> {
//                        binding.itemLayout.setBackgroundColor(ContextCompat.getColor(ctx,R.color.medium))
//                    }
//                    "high" -> {
//                        binding.itemLayout.setBackgroundColor(ContextCompat.getColor(ctx,R.color.high))
//                    }
//                    "low" -> {
//                        binding.itemLayout.setBackgroundColor(ContextCompat.getColor(ctx,R.color.low))
//                    }
//                }

                Log.d(TAG, "onBindViewHolder: "+this)
                binding.editTask.setOnClickListener {
                    listener?.onEditItemClicked(this , position)
                }

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteItemClicked(this , position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterInterface{
        fun onDeleteItemClicked(toDoData: TodoData , position : Int)
        fun onEditItemClicked(toDoData: TodoData , position: Int)
    }

}