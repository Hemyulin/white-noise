package dev.budiansky.whitenoise

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import android.app.Service
import android.content.Intent
import android.media.AudioDeviceInfo
import android.os.IBinder
import android.os.Build
import android.media.AudioDeviceCallback
import android.media.AudioManager


class NoiseService: Service() {
    val noiseEngine = NoiseEngine()
    private val channelId = "noise_playback"

    override fun onCreate() {
        super.onCreate()

        val audioManager = getSystemService(AudioManager::class.java)
        audioManager.registerAudioDeviceCallback(audioDeviceCallback, null)

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
        val openAppIntent = Intent(this, MainActivity::class.java)

        val stopIntent = Intent(this, NoiseService::class.java)
        stopIntent.action = "STOP"

        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("White Noise")
            .setContentText("Playing")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.mipmap.ic_launcher,
                "STOP",
                stopPendingIntent
            )
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

    private val audioDeviceCallback = object : AudioDeviceCallback() {
        override fun onAudioDevicesRemoved(
            removedDevices: Array<AudioDeviceInfo>
        ) {
            for (removedDevice in removedDevices) {
                if (
                    removedDevice.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
                    removedDevice.type == AudioDeviceInfo.TYPE_BLE_HEADSET ||
                    removedDevice.type == AudioDeviceInfo.TYPE_BLE_SPEAKER
                ) {
                    noiseEngine.stop()
                }
            }
        }
    }
}