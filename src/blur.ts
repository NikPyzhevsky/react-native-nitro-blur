import { getHostComponent } from 'react-native-nitro-modules'
import ViewConfig from '../nitrogen/generated/shared/json/BlurViewConfig.json'
import type { BlurMethods, BlurProps } from './specs/blur.nitro'

export const BlurView = getHostComponent<
  BlurProps,
  BlurMethods
>('BlurView', () => ViewConfig)
