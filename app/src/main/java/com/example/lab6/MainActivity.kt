package com.example.lab6

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.fixedRateTimer

private lateinit var fusedLocationClient: FusedLocationProviderClient

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private var temperatureSensor: Sensor? = null
    private var orientationSensor: Sensor? = null
    private var stepSensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var temperatureView: TextView? = null
    private var lightView: TextView? = null
    private var press: TextView? = null
    private var orientaionView: TextView? = null
    private var lon: TextView? = null
    private var lat: TextView? = null

    fun refreshLocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                runOnUiThread() {
                    lon?.text = location?.longitude.toString()
                    lat?.text = location?.longitude.toString()
                }
            }
    }

    private fun refreshOrientation() {
        runOnUiThread {
            if (resources.configuration.orientation == 1) {
                orientaionView?.text = "portrait"
            } else {
                orientaionView?.text = "landscape"
            }
        }
    }

    private fun refreshTemperature(){

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lon = findViewById<TextView>(R.id.lon)
        lat = findViewById<TextView>(R.id.lat)
        lightView = findViewById<TextView>(R.id.lumens)
        temperatureView = findViewById<TextView>(R.id.temperature)
        press = findViewById<TextView>(R.id.pressure)
        orientaionView = findViewById<TextView>(R.id.orientation)
        Thread() {
            while (true) {
                refreshLocation()
                refreshOrientation()
                Thread.sleep(7000)
            }
        }.start()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(
            this,
            lightSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensor?.let {
            sensorManager.registerListener(
                this@MainActivity,
                it,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        temperatureSensor?.let {
            sensorManager.registerListener(
                this,
                temperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("not implemented")
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        val millibarsOfPressure = event?.values!![0] // if(event.values != null) else void

        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperatureView?.text = millibarsOfPressure.toString()
                Thread.sleep(60 * 1000)
            }
            Sensor.TYPE_LIGHT -> {
                lightView?.text = millibarsOfPressure.toString()
            }
            Sensor.TYPE_STEP_COUNTER -> {
                press?.text = millibarsOfPressure.toString()
            }
        }


    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()

        lightSensor?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }

        pressureSensor?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

}

