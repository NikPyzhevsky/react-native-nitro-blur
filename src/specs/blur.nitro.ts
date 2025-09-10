import type {
  HybridView,
  HybridViewProps,
  HybridViewMethods,
} from 'react-native-nitro-modules'

export interface BlurProps extends HybridViewProps {
    intensity?: number
}

export interface BlurMethods extends HybridViewMethods {}

export type BlurView = HybridView<BlurProps, BlurMethods>
