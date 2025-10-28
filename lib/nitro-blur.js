// biome-ignore lint/correctness/noUnusedImports: Needed for JSX runtime
import React, {} from 'react';
import { BlurView } from './blur';
import {} from 'react-native';
const NitroBlur = ({ ...props }) => {
    return React.createElement(BlurView, { ...props, hybridRef: {
            f: (ref) => {
                console.log(ref.name); // <-- HybridCamera
            }
        } });
};
export default NitroBlur;
