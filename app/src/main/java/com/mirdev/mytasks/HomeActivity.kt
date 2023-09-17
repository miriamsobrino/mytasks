package com.mirdev.mytasks

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mirdev.mytasks.adapter.TasksAdapter
import com.mirdev.mytasks.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataBaseRef : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    private lateinit var adapter : TasksAdapter

    private val tasksList: MutableList<Tasks> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);
        initUI()
        initListenters()
        fetchTasksFromFirebase()
    }



    private fun initUI() {
        auth= FirebaseAuth.getInstance()
        dataBaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())
        val rvTasks = binding.rvTasks
        rvTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TasksAdapter(tasksList,  onDeleteClickListener = { taskId ->
            deleteTask(taskId) }, onUpdateCompletionStatus = { taskId, isCompleted -> updateTaskCompletionStatus(taskId, isCompleted) }
        )
        rvTasks.adapter = adapter

    }


    private fun showDialog(){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_task)
        dialog.show()
        val btnSaveTask = dialog.findViewById<Button>(R.id.btnSaveTask)
        btnSaveTask.setOnClickListener {
           saveTask()
        }
    }
    private fun fetchTasksFromFirebase() {

        dataBaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                tasksList.clear()
                for (taskSnapshot in snapshot.children) {
                    //val taskName = taskSnapshot.child("name").getValue(String::class.java)
                    //val isCompleted = taskSnapshot.child("isCompleted").getValue(Boolean::class.java)

                    try {
                        val taskId = taskSnapshot.key ?: ""
                        val taskName = taskSnapshot.child("name").getValue(String::class.java) ?: ""
                        val isCompleted =
                            taskSnapshot.child("completed").getValue(Boolean::class.java) ?: false

                        val task = Tasks(taskId, taskName, isCompleted)
                        tasksList.add(task)
                    } catch (e: Exception) {
                        Log.e("FetchTasks", "Error fetching task: ${e.message}")
                    }
                }
                updateTasks()

            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores, si es necesario
            }
        })
    }

  private fun saveTask() {
        val name = dialog.findViewById<EditText>(R.id.etTask)
        val nameText = name.text.toString()

        if(nameText.isNotEmpty()){
            if (tasksList.size < 8){
            var taskId = dataBaseRef.push().key
            val task = Tasks(id= taskId!!, name = nameText, isCompleted = false)
            tasksList.add(task)
            updateTasks()
            dataBaseRef.child(taskId!!).setValue(task)

            dialog.dismiss()
            } else {
                Toast.makeText(this, "Maximum tasks reached!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Please add a task", Toast.LENGTH_SHORT).show()
        }

    }


    private fun updateTaskCompletionStatus(taskId: String, isCompleted: Boolean) {
        val taskReference = dataBaseRef.child(taskId)
        taskReference.child("completed").setValue(isCompleted)
    }

    private fun deleteTask(taskId: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString()).child(taskId)

        databaseReference.removeValue()

        // Elimina la tarea en la lista local
        val taskToRemove = tasksList.find { it.id == taskId }
        if (taskToRemove != null) {

                tasksList.remove(taskToRemove)
                updateTasks()
            checkAddButtonEnabledState()

        }
    }
    private fun checkAddButtonEnabledState() {
        // Habilita el botón si hay menos de 8 tareas, de lo contrario, deshabilítalo
        if (tasksList.size < 7) {
            binding.btnAdd.isEnabled = true
        } else {
            binding.btnAdd.isEnabled = false
        }
    }


    private fun initListenters() {


            binding.btnAdd.setOnClickListener {
                if (tasksList.size >= 7) {
                    // Mostrar el Toast
                    Toast.makeText(this, "Maximum tasks added!", Toast.LENGTH_SHORT).show()

                    // Deshabilitar el botón después de un retraso de 1 segundo (1000 ms)

            }else{
                    showDialog()
            }

                }


        }

    private fun updateTasks(){
        adapter.notifyDataSetChanged()
    }

}