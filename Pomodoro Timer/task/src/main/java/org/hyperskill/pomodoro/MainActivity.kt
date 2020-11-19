package org.hyperskill.pomodoro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.hyperskill.pomodoro.timer.TimerView
import org.hyperskill.pomodoro.util.sendNotification
import java.text.SimpleDateFormat
import java.util.*

lateinit var timerView: TimerView
lateinit var startButton: Button
lateinit var settingButton: Button
lateinit var resetButton: Button
lateinit var timer: CountDownTimer
private const val ROUNDS = 2
private var workTime = 5000L
private var restTime = 5000L
private var currentWorkStages = 1
private var currentRestStages = 0
private const val workStages = ROUNDS
private const val restStages = ROUNDS - 1
private var totalTime = workStages * workTime + restStages * restTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        createChannel(getString(R.string.channel_id), "Rest")
        
        currentWorkStages = 1
        currentRestStages = 0
        
        timerView = findViewById(R.id.timerView)
        startButton = findViewById(R.id.startButton)
        settingButton = findViewById(R.id.settingButton)
        resetButton = findViewById(R.id.resetButton)
        
        timerView.timerText = getString(R.string.startTime)
        
        timer = createCountDownTimer()
        
        startButton.setOnClickListener {
            currentWorkStages = 1
            currentRestStages = 0
            timer.cancel()
            timer.start()
        }
        
        resetButton.setOnClickListener {
            timer.cancel()
            timerView.stop()
            timerView.circleColor = Color.RED
            timerView.timerText = SimpleDateFormat("mm:ss", Locale.US).format(workTime)
            currentWorkStages = 1
            currentRestStages = 0
        }
        
        settingButton.setOnClickListener {
            timer.cancel()
            timerView.stop()
            timerView.timerText = SimpleDateFormat("mm:ss", Locale.US).format(workTime)
            timerView.circleColor = Color.RED
            val dialogView = layoutInflater.inflate(R.layout.dialog_settings, null)
            AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val restTimeEditText = dialogView.findViewById<EditText>(R.id.restTime)
                        val workTimeEditText = dialogView.findViewById<EditText>(R.id.workTime)
                        if (restTimeEditText.text.toString() != "") restTime = restTimeEditText.text.toString().toLong() * 1000
                        if (workTimeEditText.text.toString() != "") workTime = workTimeEditText.text.toString().toLong() * 1000
                        totalTime = workStages * workTime + restStages * restTime
                        timer = createCountDownTimer()
                        timerView.timerText = SimpleDateFormat("mm:ss", Locale.US).format(workTime)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
        }
        
    }
    
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timerView.stop()
    }
    
    private fun createCountDownTimer() = object : CountDownTimer(totalTime, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timerView.timerText = SimpleDateFormat("mm:ss", Locale.US)
                    .format(millisUntilFinished - workTime * (workStages - currentWorkStages) -
                            restTime * (restStages - currentRestStages))
        
            when {
                millisUntilFinished % (workTime + restTime) in workTime - 1000..workTime -> {
                    timerView.stop()
                    timerView.circleColor = Color.RED
                    timerView.start((workTime / 1000).toInt())
                    timerView.timerText = SimpleDateFormat("mm:ss", Locale.US).format(workTime - 1000)
                    if (millisUntilFinished !in totalTime - 1000..totalTime) currentWorkStages++
                }
                millisUntilFinished % (workTime + restTime) in workTime + restTime - 1000..workTime + restTime -> {
                    timerView.stop()
                    timerView.circleColor = Color.GREEN
                    timerView.start((restTime / 1000).toInt())
                    timerView.timerText = SimpleDateFormat("mm:ss", Locale.US).format(restTime - 1000)
                    currentRestStages++
                    
                    val notificationManager = ContextCompat.getSystemService(applicationContext,
                            NotificationManager::class.java) as NotificationManager
                    notificationManager.sendNotification(applicationContext)
                    
                }
            }
        
            timerView.invalidate()
        }
    
        override fun onFinish() {
            timerView.timerText = getString(R.string.endTime)
            timerView.circleColor = Color.YELLOW
            currentWorkStages = 1
            currentRestStages = 0
            timerView.invalidate()
        }
    }
    
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
            
            notificationChannel.description = "Rest"
            
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    
}