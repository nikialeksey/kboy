package com.alexeycode.kboy.host

import android.os.Build
import android.os.VibrationEffect

class AndroidVibrator(
    private val vibrator: android.os.Vibrator
) : Vibrator {

    override fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
            } else {
                vibrator.vibrate(100L)
            }
        } else {
            vibrator.vibrate(100L)
        }
    }
}