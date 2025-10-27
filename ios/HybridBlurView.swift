import Foundation
import UIKit
import SwiftUI


// MARK: - Nitro View
final class HybridBlurView: HybridBlurViewSpec {
    func resetStyles() throws {

    }

    var intensity: Double? {
        didSet {
          Task { @MainActor in
            self.updateIntensity()
          }
        }
      }

    private let blurView = BlurEffectView()
    var view: UIView { blurView }

    func updateIntensity(){
        blurView.intensity = intensity ?? 0
    }

}
