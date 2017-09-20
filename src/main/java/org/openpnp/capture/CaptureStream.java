package org.openpnp.capture;

import java.awt.image.BufferedImage;

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
    
    // TODO: Color seems to be correct, but the array types are different. Library returns RBG and
    // the buffered image is BGR. Not sure why it's working.
    public synchronized BufferedImage capture() {
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