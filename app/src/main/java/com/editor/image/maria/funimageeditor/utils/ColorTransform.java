package com.editor.image.maria.funimageeditor.utils;

public class ColorTransform {
    
    private String filter;
    private Integer redValue, greenValue,blueValue;
    private Float brightness;

    public ColorTransform(String filter, Integer redValue, Integer greenValue, Integer blueValue, Float brightness) {
        this.filter = filter;
        this.redValue = redValue;
        this.greenValue = greenValue;
        this.blueValue = blueValue;
        this.brightness = brightness;
    }

    public Integer getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(Integer blueValue) {
        this.blueValue = blueValue;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getRedValue() {
        return redValue;
    }

    public void setRedValue(Integer redValue) {
        this.redValue = redValue;
    }

    public Integer getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(Integer greenValue) {
        this.greenValue = greenValue;
    }

    public Float getBrightness() {
        return brightness;
    }

    public void setBrightness(Float brightness) {
        this.brightness = brightness;
    }
}
