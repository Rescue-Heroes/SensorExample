package dev.comet.telemetry

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientMode
import androidx.wear.ambient.AmbientModeSupport
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : FragmentActivity(), SensorEventListener, View.OnClickListener, AmbientModeSupport.AmbientCallbackProvider {
    inner class Reading(val time: Long, val channel0: Float, val channel1: Float)

    private var ppgSensor = 0
    private lateinit var mSensorManager: SensorManager
    private var logFile: FileWriter? = null
    private var isLogging = false
    private var readingQueue = LinkedList<Reading>()
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    private class AmbientCallbackControl : AmbientModeSupport.AmbientCallback() {
        override fun onEnterAmbient(ambientDetails: Bundle?) {

        }

        override fun onExitAmbient() {

        }

        override fun onUpdateAmbient() {

        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = AmbientCallbackControl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ambientController = AmbientModeSupport.attach(this)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val sensorList: List<Sensor> = mSensorManager.getSensorList(Sensor.TYPE_ALL)
        for(currentSensor in sensorList)
        {
            Log.d("List sensors", "Name: ${currentSensor.name} /Type_String: ${currentSensor.stringType} /Type_number: ${currentSensor.type}");
            if(currentSensor.stringType == "com.google.wear.sensor.ppg")
            {
                ppgSensor = currentSensor.type;
                Log.d("Sensor", "Using of type ${currentSensor.type}")
                break;
            }
        }

        if(ppgSensor > 0)
        {
            val logButton: Button = findViewById(R.id.log_button)
            logButton.setOnClickListener(this)
        }
        else
        {
            Toast.makeText(this, "Your watch doesn't have the PPG sensor!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View?) {
        if(isLogging) {
            mSensorManager.unregisterListener(this)
            val logButton: Button = v as Button
            logButton.text = getString(R.string.stopping)
            for (reading in readingQueue)
            {
                logFile?.write("${reading.time},${reading.channel0},${reading.channel1}\n")
            }
            logFile?.close()
            readingQueue.clear()

            logButton.text = getString(R.string.start_logging)
        } else {
            logFile = FileWriter("${filesDir}/${SimpleDateFormat("dd_MM_yyyy-hh_mm_ss", Locale.US).format(Calendar.getInstance().time)}_logfile.csv")
            mSensorManager.registerListener(this, mSensorManager!!.getDefaultSensor(ppgSensor), SensorManager.SENSOR_DELAY_FASTEST)

            val logButton: Button = v as Button
            logButton.text = getString(R.string.stop_logging)
        }

        isLogging = !isLogging
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == ppgSensor) {
            readingQueue.add(Reading(event.timestamp, event.values[0], event.values[1]))
        }
    }
}
