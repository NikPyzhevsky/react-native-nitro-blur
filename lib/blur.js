import { getHostComponent } from 'react-native-nitro-modules';
import ViewConfig from '../nitrogen/generated/shared/json/BlurViewConfig.json';
export const BlurView = getHostComponent('BlurView', () => ViewConfig);
