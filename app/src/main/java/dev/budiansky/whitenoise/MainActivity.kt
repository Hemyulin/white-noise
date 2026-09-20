package dev.budiansky.whitenoise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        val intent = Intent(this, NoiseService::class.java)
        intent.action = "PLAY"
        ContextCompat.startForegroundService(this, intent)

        setContent {
            StopButton()
        }
    }

    @Composable
    fun StopButton() {
            val context = LocalContext.current

        Button(
            onClick = {
                val intent = Intent(context, NoiseService::class.java)
                intent.action = "STOP"
                ContextCompat.startForegroundService(context, intent)
            }
        ) {
            Text("Stop")
        }
    }
}
