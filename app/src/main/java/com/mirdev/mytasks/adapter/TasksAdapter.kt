package com.mirdev.mytasks.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mirdev.mytasks.R
import com.mirdev.mytasks.Tasks

class TasksAdapter( val tasks : MutableList<Tasks>, private val onDeleteClickListener: (String) -> Unit,  val onUpdateCompletionStatus :  (String, Boolean) -> Unit) : RecyclerView.Adapter<TasksViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TasksViewHolder(view, this)
    }

    override fun getItemCount()= minOf(tasks.size, 8)


    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task=  tasks[position]

        holder.render(task)



        holder.ivDelete.setOnClickListener {
            onDeleteClickListener(task.id)
        }

    }


    fun deleteTask(position: Int) {
        // Realizar la lógica para eliminar la tarea en la posición 'position'
        tasks.removeAt(position)
        notifyDataSetChanged() // Notificar al RecyclerView que los datos han cambiado
    }
}
