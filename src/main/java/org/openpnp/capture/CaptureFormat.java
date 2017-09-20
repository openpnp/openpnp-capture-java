package org.openpnp.capture;

import org.openpnp.capture.library.CapFormatInfo;

public class CaptureFormat {
    final int formatId;
    final CapFormatInfo formatInfo;

    public CaptureFormat(int formatId, CapFormatInfo formatInfo) {
        this.formatId = formatId;
        this.formatInfo = formatInfo;
    }
    
    public int getFormatId() {
        return formatId;
    }
    
    public CapFormatInfo getFormatInfo() {
        return formatInfo;
    }

    @Override
    public String toString() {
        return String.format("%d x %d, %d FPS, %s", formatInfo.width, formatInfo.height,
                formatInfo.fps, OpenPnpCapture.fourCcToString(formatInfo.fourcc));
    }

    @Override
    public int hashCode() {
        return formatInfo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CaptureFormat)) {
            return false;
        }
        CaptureFormat format = (CaptureFormat) obj;
        return format.formatInfo.equals(this.formatInfo);
    }
}