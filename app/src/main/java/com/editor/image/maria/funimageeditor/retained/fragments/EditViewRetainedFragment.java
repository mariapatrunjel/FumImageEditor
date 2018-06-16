package  com.editor.image.maria.funimageeditor.retained.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

public class EditViewRetainedFragment extends Fragment
{
    // data object we want to retain
    private Bitmap image;
    private Bitmap initialImage;
    private String filter;
    private Integer redValue,greenValue,blueValue;
    private Float brightness;
    private String menuView;

    private Float rotation = 0.0f;
    private Boolean flippedVertically = false, flippedHorizontally = false;

    private Float twirledAlpha = 0.0f;
    private Integer twirledRadius = 0;
    private Boolean rippled = false;
    private Integer sphericalRadius = 0;


    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);

    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getInitialImage() {
        return initialImage;
    }

    public void setInitialImage(Bitmap initialImage) {
        this.initialImage = initialImage;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(Integer greenValue) {
        this.greenValue = greenValue;
    }

    public Integer getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(Integer blueValue) {
        this.blueValue = blueValue;
    }

    public Integer getRedValue() {
        return redValue;
    }

    public void setRedValue(Integer redValue) {
        this.redValue = redValue;
    }

    public Float getBrightness() {
        return brightness;
    }

    public void setBrightness(Float brightness) {
        this.brightness = brightness;
    }

    public String getMenuView(){
        return menuView;
    }

    public void setMenuView(String menuView){
        this.menuView = menuView;
}

    public Float getRotation() {
        return rotation;
    }

    public void setRotation(Float rotation) {
        this.rotation = rotation;
    }

    public Boolean getFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(Boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
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