package com.example.android.firstresponse

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class FlashlightManager(private val context: Context) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val handler = Handler(Looper.getMainLooper())

    private var cameraId: String? = null
    private var isFlashing = false
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer for alarm sound

    init {
        // Find a camera that supports flash
        try {
            for (id in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                val isBackFacing = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
                if (hasFlash && isBackFacing) {
                    cameraId = id
                    break
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Turn on the flashlight
    fun turnOn() {
        try {
            cameraId?.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(it, true)
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Turn off the flashlight
    fun turnOff() {
        try {
            cameraId?.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(it, false)
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Toggle the strobe light with adjustable frequency
    fun strobe(frequency: Long) {
        isFlashing = true
        handler.post(object : Runnable {
            override fun run() {
                if (isFlashing) {
                    turnOn()
                    handler.postDelayed({
                        turnOff()
                        if (isFlashing) {
                            handler.postDelayed(this, frequency)
                        }
                    }, frequency)
                }
            }
        })
    }

    // Turn on the SOS pattern (three short, three long, three short)
    fun sos() {
        isFlashing = true
        val sosPattern = listOf(200L, 200L, 200L, 600L, 600L, 600L, 200L, 200L, 200L)
        handler.post(object : Runnable {
            var index = 0

            override fun run() {
                if (isFlashing) {
                    if (index < sosPattern.size) {
                        if (index % 2 == 0) {
                            turnOn()
                        } else {
                            turnOff()
                        }
                        handler.postDelayed(this, sosPattern[index])
                        index++
                    } else {
                        index = 0 // Repeat the pattern
                        handler.postDelayed(this, 1000)
                    }
                }
            }
        })
    }

    // Start SOS with alarm sound
    fun sosWithAlarm() {
        isFlashing = true
        val sosPattern = listOf(200L, 200L, 200L, 600L, 600L, 600L, 200L, 200L, 200L)
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound) // Replace with your alarm sound file
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        handler.post(object : Runnable {
            var index = 0

            override fun run() {
                if (isFlashing) {
                    if (index < sosPattern.size) {
                        if (index % 2 == 0) {
                            turnOn()
                        } else {
                            turnOff()
                        }
                        handler.postDelayed(this, sosPattern[index])
                        index++
                    } else {
                        index = 0 // Repeat the pattern
                        handler.postDelayed(this, 1000)
                    }
                } else {
                    stopFlashing() // Stop flashing if it's turned off
                }
            }
        })
    }

    // Stop flashing
    fun stopFlashing() {
        isFlashing = false
        turnOff()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null // Release the MediaPlayer
    }
}
