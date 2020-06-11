/*

Gerardo Daniel Naranjo Gallegos, A01209499
01/abril/2020

Nota: Recordar dar los permisos en el Manifest

 */

package com.example.t02_batterystatusdetecter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // El receptor del mensaje de broadcast del intent de la batería. Se declara aquí para poderlo usar en los métodos onCreate y onDestroy.
    private lateinit var receptor : BroadcastReceiver

    // Las variables asociadas con los campos de texto del layout para escribir en ellos la información.
    private lateinit var porcentajeBateria : TextView
    private lateinit var estadoBateria     : TextView
    private lateinit var imagenBateria     : ImageView

    // Estas dos variables se usarán para guardar el estado anterior de las variables y para verificar que no sean nulas.
    private var nivel_anterior  : Int = 0
    private var estado_anterior : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Asociamos las variables con los elementos del layout:
        porcentajeBateria = findViewById(R.id.porcentaje)
        estadoBateria     = findViewById(R.id.estado)
        imagenBateria     = findViewById(R.id.imagenBateria)

        // Declaramos nuestro filtro y le añadimos las acciones que deseamos monitorear:
        val filtro = IntentFilter()
        filtro.addAction(Intent.ACTION_BATTERY_CHANGED)
        filtro.addAction(Intent.ACTION_POWER_CONNECTED)
        filtro.addAction(Intent.ACTION_POWER_DISCONNECTED)

        // Definimos el receptor, definiendo la función a realizar cuando reciba algún mensaje broadcast de las acciones definidas en el filtro:
        receptor = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                // Declaramos las variables a utilizar, una para el porcentaje de batería y otra para el método de carga:
                var nivel   : Int?   = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, nivel_anterior)
                var estado  : Int?   = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, estado_anterior)
                var mensaje : String = ""

                // Declaramos para el caso del porcentaje de la batería:
                if ( nivel != null && nivel != nivel_anterior ) {
                    nivel_anterior = nivel!!
                    porcentajeBateria.setText(nivel_anterior.toString())
                    if ( nivel_anterior < 25 ) {
                        imagenBateria.setImageResource(R.drawable.rojo)
                    }
                    else if ( nivel_anterior < 50 ) {
                        imagenBateria.setImageResource(R.drawable.naranja)
                    }
                    else if ( nivel_anterior < 75 ) {
                        imagenBateria.setImageResource(R.drawable.amarillo)
                    }
                    else {
                        imagenBateria.setImageResource(R.drawable.verde)
                    }
                }

                // Declaramos para el caso del método de carga del dispositivo, comprobando qué método de carga se usa:
                if ( estado != null && estado != estado_anterior ) {
                    estado_anterior = estado!!
                    if ( estado_anterior == BatteryManager.BATTERY_PLUGGED_AC ) {
                        mensaje = "Conectado a AC"
                    }
                    else if ( estado_anterior == BatteryManager.BATTERY_PLUGGED_USB ) {
                        mensaje = "Conectado a USB"
                    }
                    else if ( estado_anterior == BatteryManager.BATTERY_PLUGGED_WIRELESS ) {
                        mensaje = "Conectado inalambricamente"
                    }
                    else {
                        mensaje = "No conectado"
                    }
                    estadoBateria.setText(mensaje)
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                }

            }
        }

        // Creamos/Registramos el receptor:
        registerReceiver(receptor, filtro)

    }

    override fun onDestroy() {

        // Eliminamos el receptor al finalizar la actividad:
        unregisterReceiver(receptor)

        super.onDestroy()
    }
}
