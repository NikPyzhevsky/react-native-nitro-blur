import React, {} from 'react';
import { MapView } from "./map";
const Map = ({ ...props }) => {
    return React.createElement(MapView, { ...props, hybridRef: {
            f: (ref) => {
                console.log(ref.name); // <-- HybridCamera
            }
        } });
};
export default Map;
