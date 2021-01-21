package dev.comet.telemetry

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log


class MainActivity : Activity(), SensorEventListener {
    private var ppgSensor = 0
    private var mSensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        var sensorList: List<Sensor> = mSensorManager!!.getSensorList(Sensor.TYPE_ALL)
        for(currentSensor in sensorList)
        {
            if(currentSensor.stringType == "com.google.wear.sensor.ppg")
            {
                ppgSensor = currentSensor.type;
                break;
            }
        }

        mSensorManager!!.registerListener(this, mSensorManager!!.getDefaultSensor(ppgSensor), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == ppgSensor)
        {
            Log.d("PPG", "Sensor reading: " + event.values[0])
        }
    }

    override fun onPause() {
        super.onPause()

        mSensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        mSensorManager!!.registerListener(this, mSensorManager!!.getDefaultSensor(ppgSensor), SensorManager.SENSOR_DELAY_NORMAL)
    }
}
