package com.example.lab6

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.BufferedReader
import java.io.InputStreamReader

private lateinit var fusedLocationClient: FusedLocationProviderClient

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var temp: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var lon = findViewById<TextView>(R.id.lon)
        var lat = findViewById<TextView>(R.id.lat)
        temp = findViewById<TextView>(R.id.temperature)

//        Thread() {
//            run {
//                Thread.sleep(5000);
//            }
//            runOnUiThread {
//                val floatV = cpuTemperature()
//                Toast.makeText(
//                    this@MainActivity,
//                    "Temperature: $floatV",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }.start()
        Thread() {
            run {

                Thread.sleep(1000);
            }
            runOnUiThread() {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        "Data  not fetched",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@runOnUiThread
                }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        lon.text = location?.longitude.toString()
                        lat.text = location?.longitude.toString()
                    }
            }
        }.start()


//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

//        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

//        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val millibarsOfPressure = event?.values!![0] // if(event.values != null) else void
//        temp?.text = millibarsOfPressure.toString()

//        Thread() {
//            run {
//                Thread.sleep(1000);
//            }
//            runOnUiThread {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Temperature: $millibarsOfPressure",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }.start()

    }

//    override fun onPause() {
//        // Be sure to unregister the sensor when the activity pauses.
//        super.onPause()
//        sensorManager.unregisterListener(this)
//    }
//
//    override fun onResume() {
//        // Register a listener for the sensor.
//        super.onResume()
//
//        temperature?.also { proximity ->
//            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
//        }
//    }

    fun cpuTemperature(): Float {
        val process: Process
        return try {
            process =
                Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp")
            process.waitFor()
            val reader =
                BufferedReader(InputStreamReader(process.inputStream))
            val line = reader.readLine()
            if (line != null) {
                val temp = line.toFloat()
                temp / 1000.0f
            } else {
                51.0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0f
        }
    }

}