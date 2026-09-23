package dev.budiansky.whitenoise

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Build


class NoiseService: Service() {
    val noiseEngine = NoiseEngine()
    private val channelId = "noise_playback"

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "White noise playback",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("White Noise")
            .setContentText("Playing")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, notification)

        when (intent?.action) {
            "PLAY" -> noiseEngine.start()
            "STOP" -> noiseEngine.stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        noiseEngine.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

        super.onTaskRemoved(rootIntent)
    }
}