//package com.margelo.nitro.nitroblur
//
//import android.app.Activity
//import android.content.Context
//import android.graphics.Color
//import android.util.AttributeSet
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import eightbitlab.com.blurview.BlurView as DimezisBlurView
//import eightbitlab.com.blurview.BlurTarget
//
///**
// * Android v3 BlurView контейнер:
// * - Внутри создаёт BlurTarget (то, что нужно размывать)
// * - Сверху кладёт BlurView и привязывает его к таргету через setupWith(BlurTarget)
// * - Любые добавленные извне дети автоматически попадают внутрь таргета
// *
// * Требования: eightbitlab.com.blurview v3+, minSdk 26
// */
//class BlurContainerView @JvmOverloads constructor(
//  context: Context,
//  attrs: AttributeSet? = null,
//  defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//  private val target = BlurTarget(context).apply {
//    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//  }
//
//  private val blurView = DimezisBlurView(context).apply {
//    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//  }
//
//  // Текущие параметры
//  private var currentRadius = 20f
//  private var currentOverlay = 0x22FFFFFF.toInt()
//  private var currentEnabled = true
//
//  init {
//    // Порядок важен: сначала таргет (контент), сверху — блюр
//    super.addView(target)
//    super.addView(blurView)
//    configure()
//  }
//
//  private fun configure() {
//    val decorBg = (context as? Activity)?.window?.decorView?.background
//
//    // Привязка BlurView к BlurTarget (v3 API)
//    val facade = blurView
//      .setupWith(target)
//      .setFrameClearDrawable(decorBg)
//      .setBlurRadius(currentRadius)
//      .setOverlayColor(currentOverlay)
//
//    // Включаем по умолчанию
//    blurView.setBlurEnabled(currentEnabled && currentRadius > 0f)
//    // Контейнер прозрачен, чтобы видеть размытый контент
//    setBackgroundColor(Color.TRANSPARENT)
//  }
//
//  /** Публичный API для моста/JS */
//
//  fun setBlurRadius(radius: Float) {
//    val r = radius.coerceAtLeast(0f)
//    currentRadius = r
//    blurView.setBlurEnabled(currentEnabled && r > 0f)
//    if (r > 0f) {
//      blurView.setBlurRadius(r)
//      blurView.invalidate()
//      setBackgroundColor(Color.TRANSPARENT)
//    } else {
//      // Без радиуса просто не рисуем блюр-слой
//      setBackgroundColor(Color.TRANSPARENT)
//    }
//  }
//
//  fun setOverlayColor(color: Int) {
//    currentOverlay = color
//    blurView.setOverlayColor(color)
//    blurView.invalidate()
//  }
//
//  fun setBlurEnabled(enabled: Boolean) {
//    currentEnabled = enabled
//    blurView.setBlurEnabled(enabled && currentRadius > 0f)
//    setBackgroundColor(Color.TRANSPARENT)
//  }
//
//  /** Не даём внешним детям обходить таргет: все дети идут внутрь BlurTarget */
//  override fun addView(child: View?) {
//    if (child === target || child === blurView) {
//      super.addView(child)
//    } else {
//      target.addView(child)
//    }
//  }
//
//  override fun addView(child: View?, index: Int) {
//    if (child === target || child === blurView) {
//      super.addView(child, index)
//    } else {
//      target.addView(child, index)
//    }
//  }
//
//  override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
//    if (child === target || child === blurView) {
//      super.addView(child, params)
//    } else {
//      target.addView(child, params)
//    }
//  }
//
//  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
//    if (child === target || child === blurView) {
//      super.addView(child, index, params)
//    } else {
//      target.addView(child, index, params)
//    }
//  }
//}
