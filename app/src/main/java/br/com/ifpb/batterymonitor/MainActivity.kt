package br.com.ifpb.batterymonitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvReceiver: TextView
    private lateinit var tvReceiver2: TextView
    private var caboReceiver: CaboReceiver? = null
    private lateinit var ifCabo: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.tvReceiver = findViewById(R.id.tvReceiver)
        this.tvReceiver2 = findViewById(R.id.tvReceiver2)
    }

    override fun onResume() {
        super.onResume()

        if (this.caboReceiver == null){
            this.caboReceiver = CaboReceiver()
            this.ifCabo = IntentFilter().apply {
                addAction(Intent.ACTION_BATTERY_CHANGED)
            }
        }
        registerReceiver(this.caboReceiver, this.ifCabo)
    }

    inner class CaboReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                    || status == BatteryManager.BATTERY_STATUS_FULL

            if (isCharging){
                this@MainActivity.tvReceiver.text = "Conectou!"
                this@MainActivity.tvReceiver2.text = this.calcularStatus(intent).toString()
            }else{
                this@MainActivity.tvReceiver.text = "Desconectou!"
                this@MainActivity.tvReceiver2.text = this.calcularStatus(intent).toString()
            }

        }

        fun calcularStatus(intent: Intent?): Float {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return (level!! * 100)/ scale!!.toFloat();
        }
    }
}