import type { HybridView, HybridViewProps, HybridViewMethods } from 'react-native-nitro-modules';
export interface MapProps extends HybridViewProps {
}
export interface MapMethods extends HybridViewMethods {
    changeCoordinates(latitude: number, longitude: number, regionRadius?: number): void;
}
export type MapView = HybridView<MapProps, MapMethods>;
