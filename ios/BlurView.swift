import UIKit

// MARK: - TintStyle
enum TintStyle {
  case extraLight
  case light
  case dark
  case regular
  case prominent

  static let `default`: TintStyle = .regular

  func toBlurEffect() -> UIBlurEffect.Style {
    switch self {
    case .extraLight: return .extraLight
    case .light:      return .light
    case .dark:       return .dark
    case .regular:    return .regular
    case .prominent:  return .prominent
    }
  }
}

/**
 Property wrapper clamping the value between an upper and lower bound
 */
@propertyWrapper
struct Clamping<Value: Comparable> {
  private var value: Value
  private let lowerBound: Value
  private let upperBound: Value

  init(wrappedValue: Value, lowerBound: Value, upperBound: Value) {
    self.lowerBound = lowerBound
    self.upperBound = upperBound
    self.value = max(lowerBound, min(upperBound, wrappedValue))
  }

  var wrappedValue: Value {
    get { value }
    set { value = max(lowerBound, min(upperBound, newValue)) }
  }
}

final class BlurEffectView: UIVisualEffectView {

  @Clamping(lowerBound: 0.0, upperBound: 1.0)
  var intensity: Double = 0.5 {
    didSet { setNeedsDisplay() }
  }

  var tint: TintStyle = .light {
    didSet { visualEffect = UIBlurEffect(style: tint.toBlurEffect()) }
  }

  private var visualEffect: UIVisualEffect = UIBlurEffect(style: TintStyle.default.toBlurEffect()) {
    didSet { setNeedsDisplay() }
  }

  private var animator: UIViewPropertyAnimator?

  init() {
    super.init(effect: nil)
  }

  @available(*, unavailable)
  required init?(coder aDecoder: NSCoder) { nil }

  deinit {
    animator?.stopAnimation(true)
  }

  override func draw(_ rect: CGRect) {
    super.draw(rect)

    if isDetoxPresent() {
      effect = intensity > 0 ? visualEffect : nil
      return
    }

    effect = nil
    animator?.stopAnimation(true)
    let animator = UIViewPropertyAnimator(duration: 1, curve: .linear) { [weak self] in
      guard let self = self else { return }
      self.effect = self.visualEffect
    }
    animator.fractionComplete = CGFloat(intensity)
    self.animator = animator
  }
}

private func isDetoxPresent() -> Bool {
  let args = ProcessInfo.processInfo.arguments
  return args.contains("-detoxServer") && args.contains("-detoxSessionId")
}
