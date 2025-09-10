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
    var view: UIView { intensity }

    func updateRadius(){
        blurView.intensity = intensity ?? 0
    }

}
