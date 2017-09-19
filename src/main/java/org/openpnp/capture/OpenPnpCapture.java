package org.openpnp.capture;

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

    public static String fourCcToString(int fourCc) {
        String s = "";
        for (int i = 0; i < 4; i++) {
            s += (char) (fourCc & 0xff);
            fourCc >>= 8;
        }
        return s;
    }
    
    // Library prefixes
    // darwin
    // win32-x86
    // win32-x86-64
    // linux-x86
    // linux-x86-64
    public static void main(String[] args) {
        OpenPnpCapture capture = new OpenPnpCapture();
        List<CaptureDevice> devices = capture.getDevices();
        System.out.println(devices);
    }
}
