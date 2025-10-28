import { type FC } from 'react';
import type { MapProps } from './specs/map.nitro';
import type { StyleProp, ViewStyle } from 'react-native';
interface NitroMapProps extends MapProps {
    style?: StyleProp<ViewStyle>;
}
declare const Map: FC<NitroMapProps>;
export default Map;
