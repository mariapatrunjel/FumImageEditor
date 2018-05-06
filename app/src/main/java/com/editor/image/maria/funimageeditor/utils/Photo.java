package  com.editor.image.maria.funimageeditor.utils;

import android.graphics.Bitmap;

public final class Photo {

    private static final Photo INSTANCE = new Photo();
    private Bitmap image;
    private Photo() {}

    public static Photo getInstance() {
        return INSTANCE;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

