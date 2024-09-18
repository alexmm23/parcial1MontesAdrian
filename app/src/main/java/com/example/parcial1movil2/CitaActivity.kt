package com.example.parcial1movil2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale

class CitaActivity : AppCompatActivity() {
    private lateinit var textViewDate: TextView
    private lateinit var textViewTime: TextView
    private lateinit var spinnerTime: Spinner
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita)
        textViewDate = findViewById(R.id.textViewDate)
        textViewTime = findViewById(R.id.textViewTime)
        spinnerTime = findViewById(R.id.spinnerTime)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val btnSchedule: Button = findViewById(R.id.btnSchedule)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            textViewDate.text = "Fecha seleccionada: $selectedDate"
        }

        val timeOptions = generateTimeSlots()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTime.adapter = adapter

        spinnerTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedTime = parent.getItemAtPosition(position).toString()
                textViewTime.text = "Hora seleccionada: $selectedTime"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedTime = ""
            }
        }

        // Mostrar Toast con la fecha y hora seleccionadas
        btnSchedule.setOnClickListener {
            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                Toast.makeText(this, "Fecha: $selectedDate, Hora: $selectedTime", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Por favor selecciona una fecha y hora", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        for (hour in 0..23) {
            for (minute in listOf(0, 30)) {
                val time = String.format("%02d:%02d", hour, minute)
                timeSlots.add(time)
            }
        }
        return timeSlots
    }
}