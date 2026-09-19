package dev.budiansky.whitenoise

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.random.Random

class NoiseEngine {
        @Volatile
    var isPlaying = false
    var audioTrack: AudioTrack? = null
    val sampleRate = 48000
    val encoding = AudioFormat.ENCODING_PCM_16BIT
    val channelConfig = AudioFormat.CHANNEL_OUT_MONO
    val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding)
    val samples = ShortArray(bufferSize  / 2)

    fun start() {
        isPlaying = true
        audioTrack = AudioTrack.Builder().setTransferMode(AudioTrack.MODE_STREAM)
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(encoding)
                    .setSampleRate(sampleRate)
                    .setChannelMask(channelConfig)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()

        audioTrack?.play()
        println("PLAY STATE: ${audioTrack?.playState}")

        Thread {
        while(isPlaying){
            for (i in samples.indices) {
                samples[i] = Random.nextInt(-32767, 32767).toShort()
            }

            audioTrack?.write(samples, 0, samples.size)
        }
        }.start()
    }

    fun stop(){
        isPlaying = false
        audioTrack?.stop()
    }
}