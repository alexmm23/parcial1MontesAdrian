package com.example.parcial1movil2

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var edtCurp: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtApellidos: EditText
    private lateinit var edtDomicilio: EditText
    private lateinit var edtIngreso: EditText
    private lateinit var radioPrestamo: RadioGroup
    private lateinit var btnSolicitar: Button
    private lateinit var btnLimpiar: Button
    private var selectedOption: Int = 0 //1 - Personal, 2 - Negocio, 3 - Hipotecario
    private var solicitud: Solicitud? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)

        createNotificationChannel()
        edtCurp = findViewById(R.id.txtCURP)
        edtNombre = findViewById(R.id.txtNombre)
        edtApellidos = findViewById(R.id.txtApellidos)
        edtDomicilio = findViewById(R.id.txtDomicilio)
        edtIngreso = findViewById(R.id.txtIngreso)
        radioPrestamo = findViewById(R.id.radioGroup)
        btnSolicitar = findViewById(R.id.btnSolicitar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        radioPrestamo.setOnCheckedChangeListener { group, checkedId ->
            selectedOption = when (checkedId) {
                R.id.radioPersonal -> 1
                R.id.radioNegocio -> 2
                R.id.radioVivienda -> 3
                else -> 0
            }
        }
        btnSolicitar.setOnClickListener {
            val curp = edtCurp.text.toString()
            val nombre = edtNombre.text.toString()
            val apellidos = edtApellidos.text.toString()
            val domicilio = edtDomicilio.text.toString()
            val ingreso = edtIngreso.text.toString()
            var aceptada: Boolean? = false
            if (curp.isNotEmpty() && nombre.isNotEmpty() && apellidos.isNotEmpty() && domicilio.isNotEmpty() && ingreso.isNotEmpty() && selectedOption != 0) {
                solicitud =
                    Solicitud(curp, nombre, apellidos, domicilio, ingreso.toFloat(), selectedOption)
                aceptada = solicitud?.validarIngreso()
            }else{
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (aceptada == true) {
                val intent = Intent(this, CitaActivity::class.java)
                val intent2 = Intent(this, PrestamosActivity::class.java)
                sendNotification("Solicitud aceptada", "Tu solicitud ha sido aceptada", intent, intent2)
            } else {
                Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            edtCurp.text.clear()
            edtNombre.text.clear()
            edtApellidos.text.clear()
            edtDomicilio.text.clear()
            edtIngreso.text.clear()
            radioPrestamo.clearCheck()
        }
    }

    fun createNotificationChannel() {
        val name = "CompraExitosaChannel"
        val descriptionText = "Canal de notificaciones de compra exitosa"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("compra_exitosa_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(title: String, message: String, intent: Intent, intent2: Intent) {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
            this, 0, intent2,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, "compra_exitosa_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_launcher_foreground, "Cita", pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Prestamos", pendingIntent2)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}