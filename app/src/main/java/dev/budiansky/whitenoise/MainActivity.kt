package dev.budiansky.whitenoise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
            ) {
                PlayStopButton()
            }
        }
    }

    @Composable
    fun PlayStopButton() {
        val context = LocalContext.current
        var isPlaying by remember { mutableStateOf(true) }

        Button(
            onClick = {
                val intent = Intent(context, NoiseService::class.java)

                if (isPlaying) {
                    intent.action = "STOP"
                    isPlaying = false
                } else {
                    intent.action = "PLAY"
                    isPlaying = true
                }

                ContextCompat.startForegroundService(context, intent)
            },
            modifier = Modifier.size(160.dp),
            shape = CircleShape

        ) {
            Text(
                if (isPlaying) "Stop" else "Play",
                fontSize = 24.sp
            )
        }
    }
}
