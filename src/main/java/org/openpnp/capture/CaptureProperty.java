package org.openpnp.capture;

import org.openpnp.capture.library.OpenpnpCaptureLibrary;

public enum CaptureProperty {
    Exposure(OpenpnpCaptureLibrary.CAPPROPID_EXPOSURE),
    Focus(OpenpnpCaptureLibrary.CAPPROPID_FOCUS),
    Zoom(OpenpnpCaptureLibrary.CAPPROPID_ZOOM),
    WhiteBalance(OpenpnpCaptureLibrary.CAPPROPID_WHITEBALANCE),
    Gain(OpenpnpCaptureLibrary.CAPPROPID_GAIN);
    
    final int propertyId;
    
    private CaptureProperty(int propertyId) {
        this.propertyId = propertyId;
    }
    
    public int getPropertyId() {
        return propertyId;
    }
}
