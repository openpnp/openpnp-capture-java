package org.openpnp.capture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openpnp.capture.library.CapFormatInfo;
import org.openpnp.capture.library.OpenpnpCaptureLibrary;

import com.sun.jna.Pointer;

public class CaptureDevice {
    final Pointer context;
    final int index;
    final String name;
    final String uniqueId;
    final List<CaptureFormat> formats = new ArrayList<>();

    public CaptureDevice(Pointer context, int index) {
        this.context = context;
        this.index = index;
        this.name = OpenpnpCaptureLibrary.INSTANCE.Cap_getDeviceName(context, index)
                                                  .getString(0, "UTF8");
        this.uniqueId = OpenpnpCaptureLibrary.INSTANCE.Cap_getDeviceUniqueID(context, index)
                                                      .getString(0, "UTF8");
        int formatCount = OpenpnpCaptureLibrary.INSTANCE.Cap_getNumFormats(context, index);
        for (int formatIndex = 0; formatIndex < formatCount; formatIndex++) {
            CapFormatInfo formatInfo = new CapFormatInfo();
            OpenpnpCaptureLibrary.INSTANCE.Cap_getFormatInfo(context, index, formatIndex,
                    formatInfo);
            formats.add(new CaptureFormat(formatIndex, formatInfo));
        }
    }

    public CaptureStream openStream(CaptureFormat format) {
        int id = OpenpnpCaptureLibrary.INSTANCE.Cap_openStream(context, index, format.formatId);
        if (id == -1) {
            return null;
        }
        return new CaptureStream(context, id, format);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public List<CaptureFormat> getFormats() {
        return Collections.unmodifiableList(formats);
    }
}
