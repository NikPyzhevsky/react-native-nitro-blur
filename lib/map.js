import { getHostComponent } from "react-native-nitro-modules";
import ViewConfig from "../nitrogen/generated/shared/json/MapViewConfig.json";
export const MapView = getHostComponent('MapView', () => ViewConfig);
