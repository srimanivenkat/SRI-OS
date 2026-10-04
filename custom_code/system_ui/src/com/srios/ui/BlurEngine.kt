package com.srios.ui

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View
import android.provider.Settings

object BlurEngine {

    /**
     * Applies a high-performance frosted glass blur to the Control Center background.
     * Checks the user's settings before applying to save battery if disabled.
     */
    fun applyGlassmorphism(context: Context, view: View) {
        val isBlurEnabled = Settings.System.getInt(
            context.contentResolver, 
            "qs_enable_blur", 
            1 // Default true
        ) == 1
        
        if (isBlurEnabled) {
            val blurRadius = Settings.System.getInt(
                context.contentResolver,
                "qs_blur_radius",
                50
            ).toFloat()
            
            // Android 12+ hardware-accelerated RenderEffect
            val blurEffect = RenderEffect.createBlurEffect(
                blurRadius, 
                blurRadius, 
                Shader.TileMode.CLAMP
            )
            view.setRenderEffect(blurEffect)
        } else {
            // Remove blur effect if user disabled it in settings
            view.setRenderEffect(null)
        }
    }
}
