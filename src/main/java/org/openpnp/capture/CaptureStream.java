package org.openpnp.capture;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.openpnp.capture.library.OpenpnpCaptureLibrary;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class CaptureStream {
    final private Pointer context;
    final private int streamId;
    final private CaptureFormat format;
    final private Memory memory;

    public CaptureStream(Pointer context, int streamId, CaptureFormat format) {
        this.context = context;
        this.streamId = streamId;
        this.format = format;
        memory = new Memory(format.formatInfo.width * format.formatInfo.height * 3);
        memory.clear();
    }

    public void close() {
        OpenpnpCaptureLibrary.INSTANCE.Cap_closeStream(context, streamId);
    }

    public Pointer getContext() {
        return context;
    }

    public int getStreamId() {
        return streamId;
    }

    public CaptureFormat getFormat() {
        return format;
    }

    public PropertyLimits getPropertyLimits(CaptureProperty property) throws Exception {
        IntBuffer min = IntBuffer.allocate(1);
        IntBuffer max = IntBuffer.allocate(1);
        IntBuffer def = IntBuffer.allocate(1);
        int result = OpenpnpCaptureLibrary.INSTANCE.Cap_getPropertyLimits(context, streamId,
                property.getPropertyId(), min, max, def);
        if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
            throw new CaptureException(result);
        }
        PropertyLimits limits = new PropertyLimits(min.get(0), max.get(0), def.get(0));
        return limits;
    }

    public void setAutoProperty(CaptureProperty property, boolean on) throws Exception {
        int result = OpenpnpCaptureLibrary.INSTANCE.Cap_setAutoProperty(context, streamId,
                property.getPropertyId(), on ? 1 : 0);
        if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
            throw new Exception(OpenPnpCapture.getResultDescription(result));
        }
    }
    
    public void setProperty(CaptureProperty property, int value) throws Exception {
        int result = OpenpnpCaptureLibrary.INSTANCE.Cap_setProperty(context, streamId,
                property.getPropertyId(), value);
        if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
            throw new CaptureException(result);
        }
    }
    
    public boolean getAutoProperty(CaptureProperty property) throws Exception {
        IntBuffer value = IntBuffer.allocate(1);
        int result = OpenpnpCaptureLibrary.INSTANCE.Cap_getAutoProperty(context, streamId, 
                property.getPropertyId(), value);
        if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
            throw new CaptureException(result);
        }
        return value.get(0) == 0 ? false : true;
    }

    public int getProperty(CaptureProperty property) throws Exception {
        IntBuffer value = IntBuffer.allocate(1);
        int result = OpenpnpCaptureLibrary.INSTANCE.Cap_getProperty(context, streamId, 
                property.getPropertyId(), value);
        if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
            throw new CaptureException(result);
        }
        return value.get(0);
    }

    public BufferedImage capture() throws Exception {
        byte[] bytes;

        synchronized (memory) {
            int result = OpenpnpCaptureLibrary.INSTANCE.Cap_captureFrame(context, streamId, memory,
                    (int) memory.size());
            if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
                throw new CaptureException(result);
            }
            bytes = memory.getByteArray(0, (int) memory.size());
        }

        // Note: I assumed that setting an RGB buffer on a BGR image would result in R and B
        // swapped, but that doesn't seem to happen. I tested with manually converting
        // to ARGB and that worked too, so now my working assumption is that setDataElements
        // always expects RGB and converts to the BufferedImage's color model.
        BufferedImage image = new BufferedImage(format.formatInfo.width, format.formatInfo.height,
                BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster()
             .setDataElements(0, 0, image.getWidth(), image.getHeight(), bytes);

        return image;
    }
    
    public boolean hasNewFrame() {
        return OpenpnpCaptureLibrary.INSTANCE.Cap_hasNewFrame(context, streamId) == 1;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", streamId, format);
    }
}
