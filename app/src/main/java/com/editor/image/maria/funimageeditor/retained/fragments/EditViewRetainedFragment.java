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
    private Boolean flipedVerticaly = false, flipedOrizontaly = false;



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

    public Boolean getFlipedVerticaly() {
        return flipedVerticaly;
    }

    public void setFlipedVerticaly(Boolean flipedVerticaly) {
        this.flipedVerticaly = flipedVerticaly;
    }

    public Boolean getFlipedOrizontaly() {
        return flipedOrizontaly;
    }

    public void setFlipedOrizontaly(Boolean flipedOrizontaly) {
        this.flipedOrizontaly = flipedOrizontaly;
    }
}