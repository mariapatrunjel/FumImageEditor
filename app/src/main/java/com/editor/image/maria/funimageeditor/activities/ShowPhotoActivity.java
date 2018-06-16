package  com.editor.image.maria.funimageeditor.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.editor.image.maria.funimageeditor.retained.fragments.ShowPhotoViewRetainedFragment;
import com.editor.image.maria.funimageeditor.utils.Photo;
import com.editor.image.maria.funimageeditor.R;
import com.editor.image.maria.funimageeditor.utils.ImageSaver;

import java.io.ByteArrayOutputStream;

public class ShowPhotoActivity extends Activity {
    private Bitmap currentImage;
    private static final String TAG_RETAINED_FRAGMENT = "EditViewRetainedFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        ShowPhotoViewRetainedFragment  mRetainedFragment = ( ShowPhotoViewRetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new ShowPhotoViewRetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setImage(currentImage);
        }
        currentImage = mRetainedFragment.getImage();

        Intent intent = getIntent();
        if(intent!=null){
            Photo photo = Photo.getInstance();
            currentImage = photo.getImage();
            mRetainedFragment.setImage(currentImage);
        }

        ImageView imgPicture =findViewById(R.id.imageView);
        imgPicture.setImageBitmap(currentImage);
    }

    public void onEditButtonClicked(View view){
        Photo photo = Photo.getInstance();
        photo.setImage(currentImage);

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void onSaveButtonClicked(View view){
        ImageSaver.saveImage(this,currentImage);
        finish();
    }

    public void onShareButtonClicked(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(this,currentImage));
        try {
            startActivity(Intent.createChooser(i, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }


    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
