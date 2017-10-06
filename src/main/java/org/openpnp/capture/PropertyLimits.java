package org.openpnp.capture;

public class PropertyLimits {
    private int min;
    private int max;
    private int def;
    
    public PropertyLimits(int min, int max, int def) {
        this.min = min;
        this.max = max;
        this.def = def;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    public int getDefault() {
        return def;
    }
    
    public void setDefault(int def) {
        this.def = def;
    }
}
