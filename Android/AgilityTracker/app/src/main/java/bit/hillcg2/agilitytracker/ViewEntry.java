package bit.hillcg2.agilitytracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewEntry extends AppCompatActivity {

    //Globals
    private DBManager dbManager;
    private AgilityEntry selectedEntry;
    private Button btnBack;
    private ImageView coursePicture;
    private ImageView resultsPicture;
    private PictureLoader pictureLoader;
    private PhotoViewAttacher pic1Attacther;
    private PhotoViewAttacher pic2Attacther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        //Get everything set up
        init();
    }

    //Method to set everything up
    public void init(){
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new backHandler());

        //Check to see if any values were passed through
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            //Get the ID of entry selected in last screen
            int selectedID = extras.getInt("selectedID");

            //Set up DB and image loader
            dbManager = new DBManager(getBaseContext());
            pictureLoader = new PictureLoader();

            //Get an instance of the selected entry
            selectedEntry = dbManager.getEntry(selectedID);

            //Make sure there is an entry
            if(selectedEntry != null)
            {
                //Get reference to imageviews
                coursePicture = (ImageView)findViewById(R.id.coursePicture);
                resultsPicture = (ImageView)findViewById(R.id.resultsPicture);

                //Load in pictures and put them into imageviews
                String courseFilePath = selectedEntry.getCourseFilePath();
                Bitmap coursePictureBitmap = pictureLoader.getBitmap(courseFilePath);
                coursePicture.setImageBitmap(coursePictureBitmap);

                String resultsFilePath = selectedEntry.getResultFilePathFilePath();
                Bitmap resultsPictureBitmap = pictureLoader.getBitmap(resultsFilePath);
                resultsPicture.setImageBitmap(resultsPictureBitmap);

                pic1Attacther = new PhotoViewAttacher(coursePicture);
                pic2Attacther = new PhotoViewAttacher(resultsPicture);

                pic1Attacther.update();
                pic2Attacther.update();
            }
        }
    }

    //Handler for button that sends user back to previous screen
    public class backHandler implements OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), ViewData.class);
            startActivity(i);
            finish();
        }
    }
}
