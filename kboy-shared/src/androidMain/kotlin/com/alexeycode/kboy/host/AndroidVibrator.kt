package com.alexeycode.kboy.host

import android.os.Build
import android.os.VibrationEffect

private const val VIBRATE_MS = 100L

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
                vibrator.vibrate(VIBRATE_MS)
            }
        } else {
            vibrator.vibrate(VIBRATE_MS)
        }
    }
}
