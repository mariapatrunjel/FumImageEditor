package  com.editor.image.maria.funimageeditor.activities;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.editor.image.maria.funimageeditor.retained.fragments.EditViewRetainedFragment;
import com.editor.image.maria.funimageeditor.utils.ColorTransform;
import com.editor.image.maria.funimageeditor.utils.GeometricTransform;
import com.editor.image.maria.funimageeditor.utils.ImageSaver;
import com.editor.image.maria.funimageeditor.utils.Modifier;
import com.editor.image.maria.funimageeditor.utils.ModifiersList;
import com.editor.image.maria.funimageeditor.utils.MyImageProcessing;
import com.editor.image.maria.funimageeditor.utils.Photo;
import com.editor.image.maria.funimageeditor.R;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;


public class MainActivity extends Activity {

    private static final int IMAGE_GALLERY_REQUEST = 20;
    private Bitmap currentImage;
    private Bitmap initialImage;
    private Bitmap newImage;
    private String currentView = "Disable";
    private String currentFilter = "Normal";

    private Integer redValue=0, greenValue = 0,blueValue = 0;
    private Float brightness = 1.0f;
    private Float rotation = 0.0f;
    private Boolean flippedVertically = false, flippedHorizontally = false;
    private Float twirledAlpha = 0.0f;
    private Integer twirledRadius = 0;
    private Boolean rippled = false;
    private Integer sphericalRadius = 0;

    private static final String TAG_RETAINED_FRAGMENT = "EditViewRetainedFragment";
    private EditViewRetainedFragment mRetainedFragment;

    private ModifiersList modifierList;

    private static final String TAG = "MainActivity";

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modifierList = ModifiersList.getInstance();

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (EditViewRetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new EditViewRetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
           setModifiers();
        }

        getModifiers();
        changeSeekBars();
        setSeekBarsListeners();
        setImageViewOnTouchListener();
        changeView();
        changeTransformedPicture();
        if(modifierList.isEmpty()) {
            ImageButton undo = findViewById(R.id.undo);
            undo.setVisibility(View.GONE);
        } else {
            ImageButton undo =  findViewById(R.id.undo);
            undo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processIntentData();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri imageUri = data.getData();
                InputStream inputStream;
                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);

                        initialImage = BitmapFactory.decodeStream(inputStream);
                        mRetainedFragment.setInitialImage(initialImage);
                        currentImage = initialImage.copy(Bitmap.Config.ARGB_8888, true);
                        mRetainedFragment.setImage(currentImage);

                        ImageView imgPicture = findViewById(R.id.imageView);
                        imgPicture.setImageBitmap(currentImage);
                        setShareFacebook(currentImage);
                        newImage = currentImage;

                        ImageButton undo = findViewById(R.id.undo);
                        undo.setVisibility(View.GONE);

                        resetModifiers();
                        modifierList.reset();

                        changeView();
                        changeSeekBars();
                        setSeekBarsListeners();
                        setImageViewOnTouchListener();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                    }
            }
        }
    }


    // Selectare poza din galerie
    public void onImageGalleryButtonClicked(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data,"image/*");
        startActivityForResult(photoPickerIntent,IMAGE_GALLERY_REQUEST);
    }

    // Deschide camera
    public void onCameraActivityButtonClicked(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);

    }

    // Salvare
    public void onSaveButtonClicked(View view) {
        ImageSaver.saveImage(this,newImage);
        Toast toast = Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    // Share
    public void onShareButtonClicked(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(this,newImage));
        try {
            startActivity(Intent.createChooser(i, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }


    }

    // Apasare menu filters
    public void onFiltersViewClicked(View view){
        currentView = "FiltersView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }

    // Apasare menu culori
    public void onPalletViewClicked(View view){
        currentView = "PalletView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }

    // Apasare menu brightness
    public void onBrightnessViewClicked(View view){
        currentView = "BrightnessView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }

    // Apasare back din orice menu
    public void onBackToMainMenuClicked(View view){
        currentView = "MenuView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }

    //Apasare flip vertical
    public void onFlipVerticallyClicked(View view) {
        flippedVertically = !flippedVertically;
        mRetainedFragment.setFlippedHorizontally(flippedVertically);

        changeTransformedPicture();

        modifierList.insert(new Modifier("FlippedVertically",flippedVertically.toString()));
        ImageButton undo = findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);


    }

    //Apasare flip orizontal
    public void onFlipHorizontallyClicked(View view){
        flippedHorizontally = !flippedHorizontally;
        mRetainedFragment.setFlippedHorizontally(flippedHorizontally);

        changeTransformedPicture();

        modifierList.insert(new Modifier("FlippedHorizontally",flippedHorizontally.toString()));
        ImageButton undo = findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);


    }

    //Apasare rotate left
    public void onRotateLeftClicked(View view){
        rotation = (rotation + 15.0f) % 360.0f;
        changeRotate();
    }

    //Apasare rotate right
    public void onRotateRightClicked(View view){
        rotation = (rotation - 15.0f ) % 360.0f;
        if(rotation < 0.0f )
            rotation = 360.0f + rotation ;
        changeRotate();
    }

    //Apasare rotate 90 left
    public void onRotate90LeftClicked(View view){
        rotation = (rotation + 90.0f) % 360.0f;
        changeRotate();
    }

    //Apasare rotate 90 right
    public void onRotate90RightClicked(View view){
        rotation = (rotation - 90.0f ) % 360.0f;
        if(rotation < 0.0f )
            rotation = 360.0f + rotation ;
        changeRotate();
    }

    //Apasare menu twirl
    public void onTwirlClicked(View view) {
        currentView = "TwirlView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }
    //Apasare menu twirl
    public void onRippleClicked(View view) {
        rippled = !rippled;
        mRetainedFragment.setRippled(rippled);

        changeTransformedPicture();

        modifierList.insert(new Modifier("Rippled",rippled.toString()));
        ImageButton undo = findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);
    }
    public void onSphericalClicked(View view) {
        currentView = "SphericalView";
        mRetainedFragment.setMenuView(currentView);
        changeView();
    }
    // Undo
    public void onUndoClicked(View view){
        if(!modifierList.isEmpty()){

            if(modifierList.lastIsATransformModifier()){
                modifierList.delete();
                GeometricTransform geometricTransform = modifierList.getLastGeometricTransform();
                rotation = geometricTransform.getRotation();
                flippedHorizontally = geometricTransform.getFlippedHorizontally();
                flippedVertically = geometricTransform.getFlippedVertically();
                twirledAlpha = geometricTransform.getTwirledAlpha();
                twirledRadius = geometricTransform.getTwirledRadius();
                rippled = geometricTransform.getRippled();
                sphericalRadius = geometricTransform.getSphericalRadius();

                setModifiers();
                changeTransformedPicture();
            }
            else {
                modifierList.delete();
                ColorTransform colorTransform = modifierList.getLastColorTransform();
                currentFilter = colorTransform.getFilter();
                redValue = colorTransform.getRedValue();
                greenValue = colorTransform.getGreenValue();
                blueValue = colorTransform.getBlueValue();
                brightness = colorTransform.getBrightness();
                setModifiers();
                changeModifiedPicture();
                changeSeekBars();
            }

        }

        if(modifierList.isEmpty()){
            ImageButton undo = findViewById(R.id.undo);
            undo.setVisibility(View.GONE);
        }
    }


    // Schimbarea modificatorilor
    private void setModifiers(){
        mRetainedFragment.setInitialImage(initialImage);
        mRetainedFragment.setImage(currentImage);

        mRetainedFragment.setFilter(currentFilter);
        mRetainedFragment.setRedValue(redValue);
        mRetainedFragment.setGreenValue(greenValue);
        mRetainedFragment.setBlueValue(blueValue);
        mRetainedFragment.setBrightness(brightness);

        mRetainedFragment.setMenuView(currentView);

        mRetainedFragment.setRotation(rotation);
        mRetainedFragment.setFlippedHorizontally(flippedHorizontally);
        mRetainedFragment.setFlippedVertically(flippedVertically);
        mRetainedFragment.setTwirledAlpha(twirledAlpha);
        mRetainedFragment.setTwirledRadius(twirledRadius);
        mRetainedFragment.setRippled(rippled);
        mRetainedFragment.setSphericalRadius(sphericalRadius);

    }

    private void getModifiers(){
        initialImage = mRetainedFragment.getInitialImage();
        currentImage = mRetainedFragment.getImage();

        currentFilter = mRetainedFragment.getFilter();
        redValue = mRetainedFragment.getRedValue();
        greenValue = mRetainedFragment.getGreenValue();
        blueValue = mRetainedFragment.getBlueValue();
        brightness = mRetainedFragment.getBrightness();

        currentView = mRetainedFragment.getMenuView();

        rotation = mRetainedFragment.getRotation();
        flippedVertically = mRetainedFragment.getFlippedVertically();
        flippedHorizontally = mRetainedFragment.getFlippedHorizontally();
        twirledAlpha = mRetainedFragment.getTwirledAlpha();
        twirledRadius = mRetainedFragment.getTwirledRadius();
        rippled = mRetainedFragment.getRippled();
        sphericalRadius = mRetainedFragment.getSphericalRadius();
    }

    private void resetModifiers(){
        currentFilter = "Normal";
        redValue=0;
        greenValue = 0;
        blueValue = 0;
        brightness = 1.0f;
        currentView = "MenuView";
        rotation = 0.0f;
        flippedHorizontally = false;
        flippedVertically = false;
        twirledAlpha = 0.0f;
        twirledRadius = 0;
        rippled = false;
        sphericalRadius = 0;

        mRetainedFragment.setFilter(currentFilter);
        mRetainedFragment.setRedValue(redValue);
        mRetainedFragment.setGreenValue(greenValue);
        mRetainedFragment.setBlueValue(blueValue);
        mRetainedFragment.setBrightness(brightness);

        mRetainedFragment.setMenuView(currentView);
        mRetainedFragment.setRotation(rotation);
        mRetainedFragment.setFlippedHorizontally(flippedHorizontally);
        mRetainedFragment.setFlippedVertically(flippedVertically);
        mRetainedFragment.setTwirledAlpha(twirledAlpha);
        mRetainedFragment.setTwirledRadius(twirledRadius);
        mRetainedFragment.setRippled(rippled);
        mRetainedFragment.setSphericalRadius(sphericalRadius);
    }

    // Scimba poza
    private void changeModifiedPicture(){
        if(currentImage!=null)
            new ModifyImage().execute(currentFilter,redValue.toString(),greenValue.toString(),blueValue.toString(),brightness.toString());
    }
    // Transforma poza
    private void changeTransformedPicture(){
        if(initialImage!=null)
            new TransformImage().execute(rotation.toString(),flippedVertically.toString(),flippedHorizontally.toString(),twirledAlpha.toString(), twirledRadius.toString(),rippled.toString(),sphericalRadius.toString());
    }

    // Schimbarea unui filtru
    private void changeFilter(String filterName){
        currentFilter = filterName;
        mRetainedFragment.setFilter(currentFilter);

        changeModifiedPicture();

        modifierList.insert(new Modifier("Filter",filterName));
        ImageButton undo = findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);

    }

    private void changeRotate(){
        mRetainedFragment.setRotation(rotation);

        changeTransformedPicture();

        modifierList.insert(new Modifier("Rotation",rotation.toString()));
        ImageButton undo = findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);
    }

    // Schimbare view de la menu
    private void changeView(){
        currentView = mRetainedFragment.getMenuView();

        HorizontalScrollView menuView = findViewById(R.id.menuView);
        HorizontalScrollView filtersView = findViewById(R.id.filtersView);
        ConstraintLayout palletView = findViewById(R.id.palletView);
        ConstraintLayout brightnessView = findViewById(R.id.brightnessView);
        ConstraintLayout twirlView = findViewById(R.id.twirlView);
        ConstraintLayout sphericalView = findViewById(R.id.sphericalView);
        menuView.setVisibility(View.INVISIBLE);
        filtersView.setVisibility(View.GONE);
        palletView.setVisibility(View.GONE);
        brightnessView.setVisibility(View.GONE);
        twirlView.setVisibility(View.GONE);
        sphericalView.setVisibility(View.GONE);
        switch (currentView) {
            case "FiltersView":
                filtersView.setVisibility(View.VISIBLE);
                break;
            case "PalletView":
                palletView.setVisibility(View.VISIBLE);
                break;
            case "BrightnessView":
                brightnessView.setVisibility(View.VISIBLE);
                break;
            case "MenuView":
                menuView.setVisibility(View.VISIBLE);
                break;
            case "TwirlView":
                twirlView.setVisibility(View.VISIBLE);
                break;
            case "SphericalView":
                sphericalView.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

    }
    // Schimbare seekbars
    private void changeSeekBars(){
       SeekBar redBar = findViewById(R.id.redBar);
       SeekBar greenBar = findViewById(R.id.greenBar);
       SeekBar blueBar = findViewById(R.id.blueBar);
       SeekBar brightnessBar = findViewById(R.id.brightnessBar);
       SeekBar alphaBar = findViewById(R.id.alphaBar);
       SeekBar radiusBar = findViewById(R.id.radiusBar);
       SeekBar radiusSphericalBar = findViewById(R.id.radiusSphericalBar);

       redBar.setProgress(redValue/10+25);
       greenBar.setProgress(greenValue/10+25);
       blueBar.setProgress(blueValue/10+25);
       brightnessBar.setProgress((int)((brightness+0.1)*10));
       alphaBar.setProgress((int)((twirledAlpha + 180.0f)/15.0f));
       radiusBar.setProgress(twirledRadius);
       radiusSphericalBar.setProgress(sphericalRadius);

       TextView red = findViewById(R.id.redValue);
       TextView green = findViewById(R.id.greenValue);
       TextView blue = findViewById(R.id.blueValue);

       red.setText(String.format(Locale.ENGLISH,"%d",redValue));
       green.setText(String.format(Locale.ENGLISH,"%d",greenValue));
       blue.setText(String.format(Locale.ENGLISH,"%d",blueValue));
    }

    //Facebook Share
    private void setShareFacebook(Bitmap image){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareButton shareButton = findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }
    // Setare seekbars listeners
    private void setSeekBarsListeners(){

        SeekBar redBar = findViewById(R.id.redBar);
        SeekBar greenBar = findViewById(R.id.greenBar);
        SeekBar blueBar = findViewById(R.id.blueBar);
        SeekBar brightnessBar = findViewById(R.id.brightnessBar);
        SeekBar alphaBar = findViewById(R.id.alphaBar);
        SeekBar radiusBar = findViewById(R.id.radiusBar);
        SeekBar radiusSphericalBar = findViewById(R.id.radiusSphericalBar);

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue = (progress - 25)*10;
                TextView red = findViewById(R.id.redValue);
                red.setText(String.format(Locale.ENGLISH,"%d",redValue));
                mRetainedFragment.setRedValue(redValue);

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("Red",redValue.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeModifiedPicture();
            }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue =(progress-25)*10;
                TextView green = findViewById(R.id.greenValue);
                green.setText(String.format(Locale.ENGLISH,"%d",greenValue));
                mRetainedFragment.setGreenValue(greenValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("Green",greenValue.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeModifiedPicture();

            }
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValue =(progress-25)*10;
                TextView blue =findViewById(R.id.blueValue);
                blue.setText(String.format(Locale.ENGLISH,"%d",blueValue));
                mRetainedFragment.setBlueValue(blueValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("Blue",blueValue.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeModifiedPicture();
            }
        });

        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness=(((float) progress)-0.1f)/10;
                mRetainedFragment.setBrightness(brightness);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("Brightness",brightness.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeModifiedPicture();
            }
        });

        alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                twirledAlpha = 15 * progress - 180.0f;
                mRetainedFragment.setTwirledAlpha(twirledAlpha);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("TwirledAlpha",twirledAlpha.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeTransformedPicture();
            }
        });

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                twirledRadius = progress;
                mRetainedFragment.setTwirledRadius(twirledRadius);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("TwirledRadius",twirledRadius.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeTransformedPicture();
            }
        });

        radiusSphericalBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sphericalRadius = progress;
                mRetainedFragment.setSphericalRadius(sphericalRadius);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                modifierList.insert(new Modifier("SphericalRadius",sphericalRadius.toString()));
                ImageButton undo = findViewById(R.id.undo);
                undo.setVisibility(View.VISIBLE);
                changeTransformedPicture();
            }
        });


    }

    // Setare image view listener
    private void setImageViewOnTouchListener(){

        ImageView imageView = findViewById(R.id.imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                 if (event.getAction() == MotionEvent.ACTION_DOWN) {//Button pressed
                     if (initialImage != null) {
                         ImageView imgPicture = findViewById(R.id.imageView);
                         imgPicture.setImageBitmap(initialImage);
                         return true;
                     }
                 }
                 else if (event.getAction() == MotionEvent.ACTION_UP) {// Button released
                     if(currentImage != null) {
                         ImageView imgPicture = findViewById(R.id.imageView);
                         imgPicture.setImageBitmap(newImage);
                     }
                 }
                 return false;
            }
      });
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    // Proceseaza poza primita de la camera
    private void processIntentData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Photo photo = Photo.getInstance();
            initialImage = photo.getImage();
            mRetainedFragment.setInitialImage(initialImage);
            currentImage = initialImage.copy(Bitmap.Config.ARGB_8888, true);
            mRetainedFragment.setImage(currentImage);

            ImageView imgPicture = findViewById(R.id.imageView);
            imgPicture.setImageBitmap(currentImage);
            setShareFacebook(currentImage);
            newImage = currentImage;

            ImageButton undo = findViewById(R.id.undo);
            undo.setVisibility(View.GONE);

            resetModifiers();
            modifierList.reset();

            changeView();
            changeSeekBars();
            setSeekBarsListeners();
            setImageViewOnTouchListener();

        }
        else {
            initialImage = mRetainedFragment.getInitialImage();
            currentImage = mRetainedFragment.getImage();
            changeTransformedPicture();
            changeView();
        }
    }



    // Modificare imagine ( Thread )
    private class ModifyImage extends AsyncTask<String,Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground( String... params) {
            return getModifiedImage(params[0],Integer.parseInt(params[1]),Integer.parseInt(params[2]), Integer.parseInt(params[3]), Float.parseFloat(params[4]));
        }

        @Override
        protected void onPreExecute(){
            ProgressBar spinner = findViewById(R.id.progressBar);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imgPicture = findViewById(R.id.imageView);
            imgPicture.setImageBitmap(result);
            ProgressBar spinner = findViewById(R.id.progressBar);
            spinner.setVisibility(View.GONE);
            setShareFacebook(result);
            newImage = result;
        }
    }

    // Functia de modificare a imaginii apelata de thread
    private Bitmap getModifiedImage(String currentFilter,int redValue,int greenValue,int blueValue, float brightness) {
        if(currentImage != null) {
             // convert Bitmap to Mat
            Mat mRgba = new Mat();
            Bitmap bmp32 = currentImage.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, mRgba);

            //process image
           MyImageProcessing.processImage(mRgba, currentFilter, redValue, greenValue, blueValue, brightness).copyTo(mRgba);

            // convert Mat to Bitmap
            Bitmap modifiedImage = (Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888));
            Utils.matToBitmap(mRgba, modifiedImage);

            return modifiedImage;
        }
        return null;
    }


    //Transformare geometrica a imaginii
    private class TransformImage extends AsyncTask<String,Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground( String... params) {
            return getTransformedImage( Float.parseFloat(params[0]),Boolean.parseBoolean(params[1]),Boolean.parseBoolean(params[2]),Float.parseFloat(params[3]),Integer.parseInt(params[4]),Boolean.parseBoolean(params[5]),Integer.parseInt(params[6]));
        }

        @Override
        protected void onPreExecute(){
            ProgressBar spinner = findViewById(R.id.progressBar);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            currentImage = result.copy(Bitmap.Config.ARGB_8888, true);
            mRetainedFragment.setImage(currentImage);

            changeModifiedPicture();

            ImageButton undo = findViewById(R.id.undo);
            undo.setVisibility(View.VISIBLE);

            ProgressBar spinner = findViewById(R.id.progressBar);
            spinner.setVisibility(View.GONE);
        }
    }

    // Functia de transformare geometrica a imaginii apelata de thread
    private Bitmap getTransformedImage(float rotation , boolean flippedVertically , boolean flippedHorizontally,float twirledAlpha,int twirledRadius,Boolean rippled,int sphericalRadius) {
        if(initialImage != null) {
            if (initialImage != null) {
                // convert Bitmap to Mat
                Mat mRgba = new Mat();
                Bitmap bmp32 = initialImage.copy(Bitmap.Config.ARGB_8888, true);
                Utils.bitmapToMat(bmp32, mRgba);

                //Rotate
                if(rotation != 0.0f)
                    MyImageProcessing.rotateImage(mRgba, rotation).copyTo(mRgba);

                //flip_Horizontally image
                if (flippedVertically)
                    MyImageProcessing.flipImageVertically(mRgba).copyTo(mRgba);
                if (flippedHorizontally)
                    MyImageProcessing.flipImageHorizontally(mRgba).copyTo(mRgba);


                if (twirledAlpha != 0.0f || twirledRadius != 0)
                    MyImageProcessing.twirlImage(mRgba,twirledAlpha,twirledRadius).copyTo(mRgba);
                if (rippled)
                    MyImageProcessing.rippleImage(mRgba).copyTo(mRgba);
                if(sphericalRadius != 0)
                    MyImageProcessing.sphericalImage(mRgba,sphericalRadius).copyTo(mRgba);
                // convert Mat to Bitmap
                Bitmap modifiedImage = (Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888));
                Utils.matToBitmap(mRgba, modifiedImage);

                return modifiedImage;
            }
        }
        return null;
    }



    // Filtrele
    public void onNormalFilterClicked(View view) {
        changeFilter("Normal");
    }

    public void onSepiaFilterClicked(View view) {
        changeFilter("Sepia");
    }

    public void onGrayFilterClicked(View view) {
        changeFilter("Gray");
    }

    public void onNegativeFilterClicked(View view) {
        changeFilter("Negative");
    }

    public void onBinaryFilterClicked(View view) {
        changeFilter("Binary");
    }

    public void onSketchFilterClicked(View view) {
        changeFilter("Sketch");
    }

    public void onCannyFilterClicked(View view) {
        changeFilter("Canny");
    }

    public void onRedFilterClicked(View view) {
        changeFilter("Red");
    }

    public void onBlueFilterClicked(View view) {
        changeFilter("Blue");
    }

    public void onGreenFilterClicked(View view) {
        changeFilter("Green");
    }

    public void onMagentaFilterClicked(View view) {
        changeFilter("Magenta");
    }

    public void onPinkFilterClicked(View view) {
        changeFilter("Pink");
    }

    public void onHotFilterClicked(View view) {
        changeFilter("Hot");
    }

    public void onBoneFilterClicked(View view) {
        changeFilter("Bone");
    }

    public void onOceanFilterClicked(View view) {
        changeFilter("Ocean");
    }

    public void onHEqYFilterClicked(View view) {
        changeFilter("HEqY");
    }

    public void onHEqSFilterClicked(View view) {
        changeFilter("HEqS");
    }

    public void onHEqVFilterClicked(View view) {
        changeFilter("HEqV");
    }


}
