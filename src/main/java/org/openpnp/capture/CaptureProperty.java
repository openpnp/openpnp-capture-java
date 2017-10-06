package org.openpnp.capture;

import org.openpnp.capture.library.OpenpnpCaptureLibrary;

public enum CaptureProperty {
    Brightness(OpenpnpCaptureLibrary.CAPPROPID_BRIGHTNESS),
    Contrast(OpenpnpCaptureLibrary.CAPPROPID_CONTRAST),
    Exposure(OpenpnpCaptureLibrary.CAPPROPID_EXPOSURE),
    Focus(OpenpnpCaptureLibrary.CAPPROPID_FOCUS),
    Gain(OpenpnpCaptureLibrary.CAPPROPID_GAIN),
    Gamma(OpenpnpCaptureLibrary.CAPPROPID_GAMMA),
    Saturation(OpenpnpCaptureLibrary.CAPPROPID_SATURATION),
    WhiteBalance(OpenpnpCaptureLibrary.CAPPROPID_WHITEBALANCE),
    Zoom(OpenpnpCaptureLibrary.CAPPROPID_ZOOM)
    ;
    
    final int propertyId;
    
    private CaptureProperty(int propertyId) {
        this.propertyId = propertyId;
    }
    
    public int getPropertyId() {
        return propertyId;
    }
}
