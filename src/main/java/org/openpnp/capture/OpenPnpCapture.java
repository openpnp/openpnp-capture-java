package org.openpnp.capture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.openpnp.capture.library.OpenpnpCaptureLibrary;

import com.sun.jna.Pointer;

public class OpenPnpCapture {
    Pointer context;
    
    public OpenPnpCapture() {
        context = OpenpnpCaptureLibrary.INSTANCE.Cap_createContext();
    }
    
    public List<CaptureDevice> getDevices() {
        int deviceCount = OpenpnpCaptureLibrary.INSTANCE.Cap_getDeviceCount(context);
        List<CaptureDevice> devices = new ArrayList<>();
        for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
            CaptureDevice device = new CaptureDevice(context, deviceIndex);
            devices.add(device);
        }
        return devices;
    }
    
    public void close() {
        OpenpnpCaptureLibrary.INSTANCE.Cap_releaseContext(context);
    }
    
    public Pointer getContext() {
        return context;
    }
    
    public String getLibraryVersion() {
        return OpenpnpCaptureLibrary.INSTANCE.Cap_getLibraryVersion().getString(0, "UTF8");
    }
    
    public static String getResultDescription(int result) {
        switch (result) {
            case OpenpnpCaptureLibrary.CAPRESULT_OK:
                return "OK";
            case OpenpnpCaptureLibrary.CAPRESULT_ERR:
                return "Error";
            case OpenpnpCaptureLibrary.CAPRESULT_DEVICENOTFOUND:
                return "Device Not Found";
            case OpenpnpCaptureLibrary.CAPRESULT_FORMATNOTSUPPORTED:
                return "Format Not Supported";
            case OpenpnpCaptureLibrary.CAPRESULT_PROPERTYNOTSUPPORTED:
                return "Property Not Supported";
            default:
                return "" + result;
        }
    }

    public static String fourCcToString(int fourCc) {
        String s = "";
        for (int i = 0; i < 4; i++) {
            s += (char) (fourCc & 0xff);
            fourCc >>= 8;
        }
        return s;
    }
    
    public static void main(String[] args) throws Exception {
        OpenPnpCapture capture = new OpenPnpCapture();
        CaptureDevice device = capture.getDevices().get(0);
        CaptureFormat format = device.getFormats().get(0);
        CaptureStream stream = device.openStream(format);
        BufferedImage image = stream.capture();
    }
}
