package org.openpnp.capture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openpnp.capture.library.CapFormatInfo;
import org.openpnp.capture.library.OpenpnpCaptureLibrary;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class CaptureDevice {
    Pointer context;
    int index;
    String name;
    String uniqueId;
    List<CaptureFormat> formats = new ArrayList<>();

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
        return new CaptureStream(id, format);
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

    public class CaptureFormat {
        final private int formatId;
        final private CapFormatInfo formatInfo;

        public CaptureFormat(int formatId, CapFormatInfo formatInfo) {
            this.formatId = formatId;
            this.formatInfo = formatInfo;
        }

        @Override
        public String toString() {
            return String.format("%d x %d, %d FPS, %s", formatInfo.width, formatInfo.height,
                    formatInfo.fps, OpenPnpCapture.fourCcToString(formatInfo.fourcc));
        }
    }

    public class CaptureStream {
        final private int streamId;
        final private CaptureFormat format;
        final private Memory memory;

        public CaptureStream(int streamId, CaptureFormat format) {
            this.streamId = streamId;
            this.format = format;
            memory = new Memory(format.formatInfo.width * format.formatInfo.height * 3);
            memory.clear();
        }

        public void close() {
            OpenpnpCaptureLibrary.INSTANCE.Cap_closeStream(context, streamId);
        }

        public synchronized BufferedImage capture() {
            // TODO: This is very inefficient. Figure out how to not call getByteArray().
            int res = OpenpnpCaptureLibrary.INSTANCE.Cap_captureFrame(context, streamId, memory,
                    (int) memory.size());
            if (res != OpenpnpCaptureLibrary.CAPRESULT_OK) {
                return null;
            }
            BufferedImage image = new BufferedImage(format.formatInfo.width,
                    format.formatInfo.height, BufferedImage.TYPE_3BYTE_BGR);
            byte[] bytes = memory.getByteArray(0, (int) memory.size());            
            image.getRaster().setDataElements(0, 0, image.getWidth(), image.getHeight(), bytes);
            return image;
        }

        @Override
        public String toString() {
            return String.format("%d", streamId);
        }
    }
}
