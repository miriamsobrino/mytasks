package com.mirdev.mytasks.adapter

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirdev.mytasks.R
import com.mirdev.mytasks.Tasks

class TasksViewHolder(view: View, private val adapter: TasksAdapter) : RecyclerView.ViewHolder(view) {

    val tvTask = view.findViewById<TextView>(R.id.tvTask)
    val cbCompleted = view.findViewById<CheckBox>(R.id.cbCompleted)
    val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)

    init {
        ivDelete.setOnClickListener {
            onDeleteTask()
        }

        cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val task = adapter.tasks[position]
                // Actualiza el estado isCompleted de la tarea
                task.isCompleted = isChecked
                // Llama a la función onUpdateCompletionStatus para actualizar en Firebase
                adapter.onUpdateCompletionStatus(task.id, isChecked)
                // Actualiza el estado del tachado en la vista
                updateTextStrikeThrough(task.isCompleted)
            }
        }

    }



    private fun onDeleteTask() {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            // Llama a la función en el adaptador para borrar la tarea
            adapter.deleteTask(position)
        }
    }


   fun updateTextStrikeThrough(isCompleted: Boolean) {
        if (isCompleted) {
            tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun render(tasks: Tasks) {
        // Actualiza el estado del CheckBox y el tachado del texto
        cbCompleted.isChecked = tasks.isCompleted
        updateTextStrikeThrough(tasks.isCompleted)
        tvTask.text = tasks.name
    }
}

