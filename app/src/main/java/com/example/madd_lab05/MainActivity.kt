package com.example.madd_lab05

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mainRoot: FrameLayout
    private lateinit var imageView: ImageView
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRoot = findViewById(R.id.main_root)
        imageView = findViewById(R.id.imageView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mainRoot.setOnClickListener {
            resetImagePosition()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val rootWidth = mainRoot.width
        val rootHeight = mainRoot.height
        val imgWidth = imageView.width
        val imgHeight = imageView.height

        if (rootWidth == 0 || rootHeight == 0 || imgWidth == 0 || imgHeight == 0) return

        val x = event.values[0]
        val y = event.values[1]

        val deltaX = if (abs(x) > 0.2f) -x * 4f else 0f
        val deltaY = if (abs(y) > 0.2f) y * 4f else 0f

        val maxX = (rootWidth - imgWidth).toFloat()
        val maxY = (rootHeight - imgHeight).toFloat()

        val newX = (imageView.x + deltaX).coerceIn(0f, maxX)
        val newY = (imageView.y + deltaY).coerceIn(0f, maxY)

        imageView.x = newX
        imageView.y = newY
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun resetImagePosition() {
        val rootWidth = mainRoot.width
        val rootHeight = mainRoot.height
        val imgWidth = imageView.width
        val imgHeight = imageView.height

        if (rootWidth > 0 && rootHeight > 0) {
            imageView.x = ((rootWidth - imgWidth) / 2).toFloat()
            imageView.y = ((rootHeight - imgHeight) / 2).toFloat()
        }
    }
}
