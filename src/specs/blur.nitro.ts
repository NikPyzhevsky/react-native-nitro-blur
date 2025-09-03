import type { HybridView, HybridViewProps, HybridViewMethods } from 'react-native-nitro-modules'

export interface BlurProps extends HybridViewProps {
  /** Радиус блюра в поинтах (по умолчанию 0) */
  blurRadius?: number
}

export interface BlurMethods extends HybridViewMethods {
  /** Сбросить стили к дефолтным значениям */
  resetStyles(): void
}

export type BlurView = HybridView<BlurProps, BlurMethods>
