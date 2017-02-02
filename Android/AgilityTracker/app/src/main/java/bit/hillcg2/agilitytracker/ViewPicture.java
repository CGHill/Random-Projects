package bit.hillcg2.agilitytracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPicture extends Activity {

    private PictureLoader pictureLoader;
    private ImageView largerPicture;
    private PhotoViewAttacher picAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);

        //Check to see if any values were passed through
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String pictureFilePath = extras.getString("pictureFilePath");
            pictureLoader = new PictureLoader();

            largerPicture = (ImageView)findViewById(R.id.largerPicture);
            Bitmap pictureBitmap = pictureLoader.getBitmap(pictureFilePath);
            largerPicture.setImageBitmap(pictureBitmap);

            picAttacher = new PhotoViewAttacher(largerPicture);
            picAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
            picAttacher.update();
        }
    }
}
