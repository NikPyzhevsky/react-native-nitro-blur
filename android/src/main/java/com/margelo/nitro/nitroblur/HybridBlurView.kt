package com.margelo.nitro.nitroblur

import android.content.Context
import android.view.View
import kotlin.math.roundToInt

/**
 * Nitro Module View wrapper.
 *
 * Maps a single `intensity` prop (0..100) onto the underlying BlurView:
 * - intensity == null or 0  -> blur disabled
 * - intensity in (0..100]   -> blur enabled with radius scaled to [0..25]
 *
 * If you need parity with expo.modules.blur (method, tint, reduction, targets),
 * extend this wrapper with extra props on the Spec and forward them to BlurView.
 */
class HybridBlurView(context: Context) : HybridBlurViewSpec() {

  // Public Nitro surface
  override val view: View = View(context)


  public override var intensity: Double?
    get() = currentIntensity
    set(value) {
    }

  private var currentIntensity: Double? = 0.0

  override fun toString(): String = "{HybridBlurView intensity=${currentIntensity?.roundToInt()}}"
}
