import { type FC } from 'react';
import type { BlurProps } from './specs/blur.nitro';
import { type StyleProp, type ViewStyle } from 'react-native';
interface NitroBlurProps extends BlurProps {
    style?: StyleProp<ViewStyle>;
}
declare const NitroBlur: FC<NitroBlurProps>;
export default NitroBlur;
