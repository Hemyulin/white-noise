package dev.budiansky.whitenoise

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.random.Random

class NoiseEngine {
        @Volatile
    var isPlaying = false
    var audioTrack: AudioTrack? = null
    var nowPlaying: Thread? = null
    val sampleRate = 48000
    val encoding = AudioFormat.ENCODING_PCM_16BIT
    val channelConfig = AudioFormat.CHANNEL_OUT_MONO
    val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding)
    val samples = ShortArray(bufferSize  / 2)

    var volume = 0.2f
    var previousSample = 0f

    fun start() {
        if (isPlaying) return


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

        nowPlaying = Thread {
        while(isPlaying){
            for (i in samples.indices) {
                val randomSample = Random.nextInt(-32767, 32767)
                samples[i] = (randomSample * volume).toInt().toShort()
            }

            audioTrack?.write(samples, 0, samples.size)
        }
        }
        nowPlaying?.start()
    }

    fun stop(){
        isPlaying = false
        audioTrack?.pause()
        audioTrack?.flush()
        nowPlaying?.join()
        audioTrack?.release()
        audioTrack = null
        nowPlaying = null
    }
}