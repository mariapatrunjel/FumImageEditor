package  com.editor.image.maria.funimageeditor.retained.fragments;

import android.app.Fragment;
import android.os.Bundle;

public class CameraViewRetainedFragment extends Fragment
{
    // data object we want to retain
    private String filter;
    private Integer redValue,greenValue,blueValue;
    private Float brightness;
    private int cameraId;
    private int flashMode;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
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

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public int getFlashMode() {
        return flashMode;
    }

    public void setFlashMode(int flashMode) {
        this.flashMode = flashMode;
    }


}