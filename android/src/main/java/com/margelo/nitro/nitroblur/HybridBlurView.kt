package com.margelo.nitro.nitroblur

import android.content.Context
import android.util.Log
import android.view.View
import kotlin.math.roundToInt

/**
 * Nitro wrapper for BackdropBlurViewV2.
 *
 * Exposes a single prop: `intensity` (0–100),
 * which maps linearly to blur radius (0–25 px).
 */
class HybridBlurView(context: Context) : HybridBlurViewSpec() {

  companion object {
    private const val TAG = "HybridBlurView"
  }

  // Используем реальный блюр-вью
  private val blurView = BackdropBlurViewV2(context)

  override val view: View = blurView

  // Значение, приходящее с JS
  private var currentIntensity: Double = 0.0

  public override var intensity: Double?
    get() = currentIntensity
    set(value) {
      Log.d(TAG, "set intensity=${value ?: 0.0}")
      currentIntensity = value ?: 0.0
      applyIntensity()
    }

  private fun applyIntensity() {
    val intensity = currentIntensity.coerceIn(0.0, 100.0)
    Log.d(TAG, "applyIntensity(): setting intensity=$intensity")
    val radius = (intensity / 100.0 * 25.0).toFloat() // 0..25
    blurView.setBlurEnabled(radius > 0)
    blurView.setBlurRadius(radius)
    Log.d(TAG, "applyIntensity(): radius=$radius, enabled=${radius > 0}")
  }

  override fun toString(): String {
    Log.d(TAG, "toString(): currentIntensity=${currentIntensity.roundToInt()}")
    return "{HybridBlurView intensity=${currentIntensity.roundToInt()}}"
  }
}
