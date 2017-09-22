package org.openpnp.capture;

public class CaptureException extends Exception {
    private final int result;
    
    public CaptureException(int result) {
        super(OpenPnpCapture.getResultDescription(result));
        this.result = result;
    }
    
    public int getResult() {
        return result;
    }
}
