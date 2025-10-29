package com.margelo.nitro.nitroblur

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import eightbitlab.com.blurview.BlurView as DimezisBlurView
import eightbitlab.com.blurview.BlurAlgorithm
import eightbitlab.com.blurview.RenderScriptBlur

/**
 * Backdrop blur overlay (Dimezis BlurView v2).
 * - Блюрит "фон" под собой (соседние/нижние вью) — т.е. android.R.id.content.
 * - Любые дети, добавленные в этот контейнер, рисуются поверх размытого фона
 *   (можно класть текст/иконки/скрим).
 *
 * Требования:
 *   - eightbitlab.com.blurview 2.0.6
 *   - MinSdk ~ 17+
 */
class BackdropBlurViewV2 @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val blurView = DimezisBlurView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }

  // Текущее состояние
  private var currentRadius = 16f          // px
  private var currentOverlay = 0x00000000  // ARGB (transparent by default to avoid lightening)
  private var currentEnabled = true
  private var configured = false
  private var configuredAlgoName: String = "N/A"

  init {
    Log.i(TAG, "init: creating BackdropBlurViewV2; will add internal BlurView and configure on attach")
    // Сначала кладём сам слой размытия, поверх него — твои дети
    super.addView(blurView)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    Log.i(TAG, "onAttachedToWindow(): configured=$configured")
    ensureConfigured()
    // Second-stage retry if not configured (in case window is attached late)
    if (!configured) {
      post {
        if (!configured) {
          Log.i(TAG, "onAttachedToWindow(): retry ensureConfigured() on posted frame")
          ensureConfigured()
        }
      }
    }
  }

  private fun ensureConfigured() {
    if (configured) return
    Log.i(TAG, "ensureConfigured(): calling configure()")
    try {
      configure()
    } catch (t: Throwable) {
      Log.e(TAG, "ensureConfigured(): configure() failed: ${t.message}", t)
    }
  }

  // Helper to reliably get the Activity from any Context wrapper
  private fun findActivity(): Activity? {
    var ctx: Context? = context
    while (ctx is android.content.ContextWrapper) {
      if (ctx is Activity) return ctx
      ctx = ctx.baseContext
    }
    return null
  }

  private fun configure() {
    Log.i(TAG, "configure(): start")
    val activity = findActivity()
    val decorView = activity?.window?.decorView

    if (decorView == null) {
      Log.w(TAG, "configure(): decorView is null (no window yet). Will retry on next frame.")
      // Retry once on the next frame
      post { ensureConfigured() }
      return
    }

    val decorBg = decorView.background
    var root: ViewGroup? = decorView.findViewById(android.R.id.content)

    // Fallback: climb to the top-most View via View.parent (avoid ViewParent.parent ambiguity)
    if (root == null) {
      var top: android.view.View = this
      while (true) {
        val p = top.parent
        if (p is android.view.View) {
          top = p
        } else {
          break
        }
      }
      root = top as? ViewGroup
    }

    Log.d(
      TAG,
      "configure(): activity=${activity != null}, decorView=${decorView != null}, root(foundContent)=${decorView.findViewById<ViewGroup>(android.R.id.content) != null}, root(used)=${root != null}"
    )

    if (root == null) {
      Log.e(TAG, "android.R.id.content not found — fallback to transparent")
      setBackgroundColor(Color.TRANSPARENT)
      return
    }

    // Выбор алгоритма: RenderEffectBlur (если доступен в класспате) на API 31+,
    // иначе RenderScriptBlur (deprecated, но ок для v2)
    val algo: BlurAlgorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val maybe = try {
        val cls = Class.forName("eightbitlab.com.blurview.RenderEffectBlur")
        cls.getDeclaredConstructor().newInstance()
      } catch (t: Throwable) {
        Log.w(TAG, "configure(): RenderEffectBlur class not found on API ${Build.VERSION.SDK_INT}, falling back to RenderScriptBlur (may be NO-OP on modern devices): ${t.message}")
        null
      }
      (maybe as? BlurAlgorithm) ?: RenderScriptBlur(context)
    } else {
      @Suppress("DEPRECATION")
      RenderScriptBlur(context)
    }

    configuredAlgoName = algo::class.java.simpleName
    Log.i(TAG, "configure(): using algorithm=$configuredAlgoName (api=${Build.VERSION.SDK_INT})")

    blurView
      .setupWith(root, algo)
      .setFrameClearDrawable(decorBg)
      .setBlurRadius(currentRadius)
      .setOverlayColor(currentOverlay)

    blurView.setBlurEnabled(currentEnabled && currentRadius > 0f)
    Log.i(TAG, "configure(): blurEnabled=${currentEnabled && currentRadius > 0f}, radius=$currentRadius, overlay=#${Integer.toHexString(currentOverlay)}")

    // Контейнер прозрачен, чтобы был виден размазанный фон
    setBackgroundColor(Color.TRANSPARENT)

    Log.i(TAG, "configure(): finished. Backdrop blur is wired to android.R.id.content")
    configured = true
    Log.d(TAG, "Configured backdrop (v2). radius=$currentRadius overlay=#${Integer.toHexString(currentOverlay)}")
  }

  /** Публичный API — зови из Nitro/JS моста */

  fun setBlurRadius(radius: Float) {
    Log.i(TAG, "setBlurRadius(): requested=$radius (pre-configured=$configured, algo=$configuredAlgoName)")
    ensureConfigured()
    currentRadius = radius.coerceAtLeast(0f)
    Log.d(TAG, "setBlurRadius(): called with radius=$radius")
    blurView.setBlurEnabled(currentEnabled && currentRadius > 0f)
    if (currentRadius > 0f) {
      blurView.setBlurRadius(currentRadius)
      blurView.invalidate()
      setBackgroundColor(Color.TRANSPARENT)
    } else {
      // без радиуса — просто прозрачный контейнер
      setBackgroundColor(Color.TRANSPARENT)
    }
    Log.d(TAG, "setBlurRadius($radius)")
  }

  fun setOverlayColor(color: Int) {
    Log.i(TAG, "setOverlayColor(): requested=#${Integer.toHexString(color)} (pre-configured=$configured)")
    ensureConfigured()
    currentOverlay = color
    Log.d(TAG, "setOverlayColor(): called with color=#${Integer.toHexString(color)}")
    blurView.setOverlayColor(color)
    blurView.invalidate()
    Log.d(TAG, "setOverlayColor(#${Integer.toHexString(color)})")
  }

  fun setBlurEnabled(enabled: Boolean) {
    Log.i(TAG, "setBlurEnabled(): requested=$enabled (pre-configured=$configured)")
    ensureConfigured()
    currentEnabled = enabled
    Log.d(TAG, "setBlurEnabled(): called with enabled=$enabled")
    blurView.setBlurEnabled(enabled && currentRadius > 0f)
    setBackgroundColor(Color.TRANSPARENT)
    Log.d(TAG, "setBlurEnabled($enabled)")
  }

  // Некоторые версии v2 имеют автообновление — если нет метода, оставь no-op.
  fun setAutoUpdate(auto: Boolean) {
    Log.i(TAG, "setAutoUpdate(): requested=$auto (pre-configured=$configured)")
    ensureConfigured()
    try {
      val m = DimezisBlurView::class.java.getMethod("setBlurAutoUpdate", Boolean::class.javaPrimitiveType)
      m.invoke(blurView, auto)
      Log.d(TAG, "setAutoUpdate($auto)")
    } catch (_: Throwable) {
      // no-op для версий без API
    }
  }

  companion object {
    private const val TAG = "BackdropBlurViewV2" // Note: Using info-level logging for lifecycle and config events
  }
}
