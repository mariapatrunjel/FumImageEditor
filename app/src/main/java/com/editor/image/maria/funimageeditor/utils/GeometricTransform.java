package com.editor.image.maria.funimageeditor.utils;

public class GeometricTransform {

    private Float rotation;
    private Boolean flippedvertically, flippedHorizontally;

    public GeometricTransform(Float rotation, Boolean flippedVertically, Boolean flippedHorizontally) {
        this.rotation = rotation;
        this.flippedvertically = flippedVertically;
        this.flippedHorizontally = flippedHorizontally;
    }

    public Float getRotation() {
        return rotation;
    }

    public void setRotation(Float rotation) {
        this.rotation = rotation;
    }

    public Boolean getFlippedVertically() {
        return flippedvertically;
    }

    public void setFlippedVertically(Boolean flippedvertically) {
        this.flippedvertically = flippedvertically;
    }

    public Boolean getFlippedHorizontally() {
        return flippedHorizontally;
    }

    public void setFlippedHorizontally(Boolean flippedHorizontally) {
        this.flippedHorizontally = flippedHorizontally;
    }
}
