package org.openpnp.capture;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

import org.openpnp.capture.library.OpenpnpCaptureLibrary;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class CaptureStream {
    final private Pointer context;
    final private int streamId;
    final private CaptureFormat format;
    final private Memory memory;
    private int imageType;

    public CaptureStream(Pointer context, int streamId, CaptureFormat format) {
        this.context = context;
        this.streamId = streamId;
        this.format = format;
        imageType = getBufferedImageType();
        if (imageType == BufferedImage.TYPE_3BYTE_BGR) {
            memory = new Memory(format.formatInfo.width * format.formatInfo.height * 3);
        }
        else {
            memory = new Memory(format.formatInfo.width * format.formatInfo.height * 4);
        }
        memory.clear();
    }
    
    private int getBufferedImageType() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration config = dev.getDefaultConfiguration();
        BufferedImage image = config.createCompatibleImage(format.formatInfo.width, 
                format.formatInfo.height);
        int type = image.getType();
        if (type == BufferedImage.TYPE_3BYTE_BGR 
                || type == BufferedImage.TYPE_4BYTE_ABGR
                || type == BufferedImage.TYPE_4BYTE_ABGR_PRE
                || type == BufferedImage.TYPE_INT_ARGB
                || type == BufferedImage.TYPE_INT_ARGB_PRE 
                || type == BufferedImage.TYPE_INT_BGR
                || type == BufferedImage.TYPE_INT_RGB) {
            return type;
        }
        else {
            return BufferedImage.TYPE_INT_RGB;
        }
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
        synchronized (memory) {
            int result = OpenpnpCaptureLibrary.INSTANCE.Cap_captureFrame(context, streamId, memory,
                    (int) memory.size());
            if (result != OpenpnpCaptureLibrary.CAPRESULT_OK) {
                throw new CaptureException(result);
            }
            
            BufferedImage image = new BufferedImage(format.formatInfo.width, format.formatInfo.height,
                    imageType);
            switch (imageType) {
                case BufferedImage.TYPE_3BYTE_BGR: 
                    return convertRgbTo3ByteBgr(memory, image);
                case BufferedImage.TYPE_4BYTE_ABGR:
                case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                    return convertRgbTo4ByteBgr(memory, image);
                case BufferedImage.TYPE_INT_ARGB:
                case BufferedImage.TYPE_INT_ARGB_PRE: 
                case BufferedImage.TYPE_INT_RGB:
                    return convertRgbToIntRgbBgr(memory, image, false);
                case BufferedImage.TYPE_INT_BGR:
                    return convertRgbToIntRgbBgr(memory, image, true);
                default:
                    throw new Error("Programmer error: Unknown image type.");
            }
        }
    }
    
    private BufferedImage convertRgbToIntRgbBgr(Memory memory, BufferedImage image, boolean bgr) {
        byte[] rgb = memory.getByteArray(0, (int) memory.size());
        // Get a reference to the image's raster array.
        int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < format.formatInfo.width * format.formatInfo.height; i++) {
            int a = 0xff;
            int r = rgb[i * 3 + 0] & 0xff;
            int g = rgb[i * 3 + 1] & 0xff;
            int b = rgb[i * 3 + 2] & 0xff;
            if (bgr) {
                imageData[i] = (a << 24) | (b << 16) | (g << 8) | (r << 0);
            }
            else {
                imageData[i] = (a << 24) | (r << 16) | (g << 8) | (b << 0);
            }
        }
        return image;
    }
    
    private BufferedImage convertRgbTo3ByteBgr(Memory memory, BufferedImage image) {
        // Get a reference to the image's raster array.
        byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        // Copy from the captured frame memory to the raster.
        memory.read(0, imageData, 0, (int) memory.size());
        // Swap R and B since the capture data is RGB and we created a BGR image.
        for (int i = 0; i < format.formatInfo.width * format.formatInfo.height * 3; i += 3) {
            byte r = imageData[i];
            imageData[i] = imageData[i + 2];
            imageData[i + 2] = r;
        }
        return image;
    }

    private BufferedImage convertRgbTo4ByteBgr(Memory memory, BufferedImage image) {
        byte[] rgb = memory.getByteArray(0, (int) memory.size());
        // Get a reference to the image's raster array.
        byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < format.formatInfo.width * format.formatInfo.height; i++) {
            int r = rgb[i * 3 + 0] & 0xff;
            int g = rgb[i * 3 + 1] & 0xff;
            int b = rgb[i * 3 + 2] & 0xff;
            imageData[i * 4 + 0] = (byte) 0xff;
            imageData[i * 4 + 1] = (byte) b;
            imageData[i * 4 + 2] = (byte) g;
            imageData[i * 4 + 3] = (byte) r;
        }
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
