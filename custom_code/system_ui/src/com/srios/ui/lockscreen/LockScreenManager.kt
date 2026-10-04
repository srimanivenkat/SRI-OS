package com.srios.ui.lockscreen

import android.content.Context
import android.provider.Settings
import android.view.View

class LockScreenManager(private val context: Context) {

    /**
     * Checks user settings and applies the Magazine Depth Effect if enabled.
     * This hooks into the SystemUI Keyguard rendering pipeline.
     */
    fun updateDepthEffectLayer(clockLayer: View, wallpaperForegroundLayer: View) {
        val isDepthEnabled = Settings.System.getInt(
            context.contentResolver, 
            "lock_screen_depth_effect", 
            0
        ) == 1
        
        if (isDepthEnabled) {
            // Push clock behind the foreground subject layer (Z-index manipulation)
            clockLayer.z = -1f
            wallpaperForegroundLayer.z = 1f
            wallpaperForegroundLayer.visibility = View.VISIBLE
        } else {
            // Standard AOSP behavior
            clockLayer.z = 1f
            wallpaperForegroundLayer.visibility = View.GONE
        }
    }

    /**
     * Reads the AOD state from Settings to dictate power management.
     * Returns: 0 (Always On), 1 (Tap to Show), 2 (Smart/Notifications only)
     */
    fun getAodState(): Int {
        return Settings.System.getInt(context.contentResolver, "aod_display_mode", 1)
    }
}
