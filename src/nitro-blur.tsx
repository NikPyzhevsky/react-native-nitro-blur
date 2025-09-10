// biome-ignore lint/correctness/noUnusedImports: Needed for JSX runtime
import React, { type FC } from 'react'
import type { BlurProps } from './specs/blur.nitro'
import { BlurView } from './blur'
import type { StyleProp, ViewStyle } from 'react-native'

interface NitroBlurProps extends BlurProps {
  style?: StyleProp<ViewStyle>
}

 const NitroBlur: FC<NitroBlurProps> = ({
  ...props
}) => {
  return <BlurView {...props} hybridRef={{
      f: (ref) => {
          console.log(ref.name) // <-- HybridCamera
      }
  }}
  />
}

export default NitroBlur;
