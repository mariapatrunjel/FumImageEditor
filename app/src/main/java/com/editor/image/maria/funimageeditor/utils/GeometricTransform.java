package com.editor.image.maria.funimageeditor.utils;

public class GeometricTransform {

    private Float rotation;
    private Boolean flippedvertically, flippedHorizontally;
    private Float twirledAlpha;
    private Integer twirledRadius;
    private Boolean rippled;
    private Integer sphericalRadius;

    public GeometricTransform(Float rotation, Boolean flippedVertically, Boolean flippedHorizontally,Float twirledAlpha,Integer twirledRadius,Boolean rippled,Integer sphericalRadius) {
        this.rotation = rotation;
        this.flippedvertically = flippedVertically;
        this.flippedHorizontally = flippedHorizontally;
        this.twirledAlpha = twirledAlpha;
        this.twirledRadius = twirledRadius;
        this.rippled = rippled;
        this.sphericalRadius = sphericalRadius;
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

    public Float getTwirledAlpha() {
        return twirledAlpha;
    }

    public void setTwirledAlpha(Float twirledAlpha) {
        this.twirledAlpha = twirledAlpha;
    }

    public Integer getTwirledRadius() {
        return twirledRadius;
    }

    public void setTwirledRadius(Integer twirledRadius) {
        this.twirledRadius = twirledRadius;
    }

    public Boolean getRippled() {
        return rippled;
    }

    public void setRippled(Boolean rippled) {
        this.rippled = rippled;
    }

    public Integer getSphericalRadius() {
        return sphericalRadius;
    }

    public void setSphericalRadius(Integer sphericalRadius) {
        this.sphericalRadius = sphericalRadius;
    }
}
