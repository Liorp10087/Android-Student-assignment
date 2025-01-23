package com.example.student_assignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_assignment.model.Model
import com.example.student_assignment.model.Student

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onItemClick(student: Student?)
}

class StudentsRecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_recycler_view)

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.students_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentsRecyclerAdapter(Model.shared.students)
        recyclerView.adapter = adapter

        // Add button logic
        val addStudentButton: Button = findViewById(R.id.add_student_button)
        addStudentButton.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        // Handle RecyclerView item clicks (optional, if needed for more functionality)
        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("TAG", "Clicked on item at position: $position")
            }

            override fun onItemClick(student: Student?) {
                Log.d("TAG", "Clicked on student: ${student?.name}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged() // Refresh RecyclerView when returning to this activity
    }

    // ViewHolder class for the RecyclerView items
    class StudentViewHolder(itemView: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {

        private var nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        private var idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        private var studentCheckBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)
        private var student: Student? = null

        init {
            studentCheckBox.setOnClickListener {
                student?.isChecked = studentCheckBox.isChecked
            }
            itemView.setOnClickListener {
                listener?.onItemClick(student)
            }
        }

        fun bind(student: Student?) {
            this.student = student
            nameTextView.text = student?.name
            idTextView.text = student?.id
            studentCheckBox.isChecked = student?.isChecked ?: false
        }
    }

    // Adapter class for RecyclerView
    class StudentsRecyclerAdapter(private val students: MutableList<Student>) :
        RecyclerView.Adapter<StudentViewHolder>() {

        var listener: OnItemClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.student_list_row, parent, false)
            return StudentViewHolder(itemView, listener)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            holder.bind(students[position])
        }

        override fun getItemCount(): Int = students.size
    }
}
