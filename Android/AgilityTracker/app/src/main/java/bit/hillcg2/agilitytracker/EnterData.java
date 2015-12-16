package bit.hillcg2.agilitytracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterData extends AppCompatActivity {

    //Make everyone be able to see the photo file
    private File coursePhotoFile;
    private File resultsPhotoFile;
    private File tempFile;

    //Widgets
    private ImageView mainPictureBox;
    private ImageView smallPictureBox;

    private Button btnCoursePicture;
    private Button btnResultsPicture;
    private Button btnSave;

    //Variables
    private DBManager dbManager;
    private Spinner spinClass;
    private String[] classes;
    private ArrayAdapter classesAdapter;
    private EditText dateBox;

    //States
    private Boolean coursePictureTaken;
    private Boolean resultsPictureTaken;
    private Boolean dateEntered;
    private Boolean entrySaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        init();
    }

    //Method that initiates everything
    public void init(){
        //Initiate DB
        dbManager = new DBManager(this);

        //Set up states
        coursePictureTaken = false;
        resultsPictureTaken = false;
        dateEntered = false;
        entrySaved = false;

        //Get references to XML widgets and do set ups
        mainPictureBox = (ImageView)findViewById(R.id.mainPictureBox);
        smallPictureBox = (ImageView)findViewById(R.id.smallPictureBox);

        btnCoursePicture = (Button)findViewById(R.id.btnCoursePicture);
        btnCoursePicture.setOnClickListener(new coursePictureHandler());

        btnResultsPicture = (Button)findViewById(R.id.btnResultsPicture);
        btnResultsPicture.setOnClickListener(new resultsPictureHandler());

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new saveData());
        btnSave.setEnabled(false);

        //Set up spinner
        classes = new String[] {"A", "B", "C"};
        classesAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, classes);
        spinClass = (Spinner)findViewById(R.id.spinClass);
        spinClass.setAdapter(classesAdapter);

        dateBox = (EditText)findViewById(R.id.dateBox);
        dateBox.setMaxWidth(dateBox.getWidth());
        dateBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Makes sure something has been entered in date box before saving
                dateEntered = true;
                if(dateEntered && coursePictureTaken && resultsPictureTaken)
                    btnSave.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Creates a file with a unique file name
    public File makePhotoFile(){
        //Get systems image folder
        File imageRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imageStorageDirectory = new File(imageRootPath, "AgilityTracker");

        //Make subdirectory in pictures folder
        if(!imageStorageDirectory.exists())
        {
            imageStorageDirectory.mkdirs();
        }

        //Get time stamp
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date currentTime = new Date();

        String timeStamp = timeStampFormat.format(currentTime);

        //Make file name using timestamp
        String mPhotoFileName = "IMG_" + timeStamp + ".jpg";

        //Create file with unique name and return it
        File createdFile = new File(imageStorageDirectory.getPath() + File.separator + mPhotoFileName);

        return createdFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Delete any files that weren't saved into the database to stop build up of files user can't delete
        if(!entrySaved)
        {
            if(coursePhotoFile != null)
                coursePhotoFile.delete();
            if(resultsPhotoFile != null)
                resultsPhotoFile.delete();

            coursePhotoFile = null;
            resultsPhotoFile = null;
        }

    }

    //Handler for button to save all the current data
    public class saveData implements OnClickListener{

        @Override
        public void onClick(View v) {
            //Get all info needed to save
            String enteredDate = dateBox.getText().toString();
            String dogClass = spinClass.getSelectedItem().toString();
            String coursePhotoFilePath = coursePhotoFile.getAbsolutePath();
            String resultPhotoFilePath = resultsPhotoFile.getAbsolutePath();

            //Insert into database
            dbManager.insertEntry(enteredDate, coursePhotoFilePath, resultPhotoFilePath, dogClass);

            //Reset page
            dateBox.setText("");
            spinClass.setSelection(0);
            mainPictureBox.setImageDrawable(null);
            smallPictureBox.setImageBitmap(null);
            btnSave.setEnabled(false);

            //Stops files getting deleted in onDestroy
            entrySaved = true;

            //Feedback for user
            Toast.makeText(getBaseContext(), "Entry Saved", Toast.LENGTH_LONG).show();

            //Go back to main page
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    //Button click handler to take course picture
    public class coursePictureHandler implements OnClickListener {

        @Override
        public void onClick(View view) {
            //Make file
            tempFile = makePhotoFile();

            Uri photoFileUri = Uri.fromFile(tempFile);

            //Make camera intent
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Pass data through to camera
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            //Start camera
            startActivityForResult(imageCaptureIntent, 1);
        }
    }

    //Button click handler to take result picture
    public class resultsPictureHandler implements OnClickListener {

        @Override
        public void onClick(View view) {
            //Make file
            tempFile = makePhotoFile();

            Uri photoFileUri = Uri.fromFile(tempFile);

            //Make camera intent
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Pass data through to camera
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            //Start camera
            startActivityForResult(imageCaptureIntent, 2);
        }
    }

    //When camera returns
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //Request code for course picture
        if(requestCode == 1)
        {
            //Check file was okay
            if(resultCode == RESULT_OK)
            {
                //Delete old file if there was one, to stop buildup of pictures the user can't delete
                if(coursePhotoFile != null)
                {
                    coursePhotoFile.delete();
                    coursePhotoFile = null;
                }

                coursePictureTaken = true;

                //Save the file into appropriate place
                coursePhotoFile = tempFile;

                //Make sure we have real file path
                String realFilePath = coursePhotoFile.getAbsolutePath();

                //Get the picture as a bitmap and set to main display
                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);
                mainPictureBox.setImageBitmap(userPhotoBitmap);

                //If the results picture has been taken, set it to smaller display
                if(resultsPictureTaken)
                {
                    //Make sure we have real file path
                    String resultsPictureFilePath = resultsPhotoFile.getAbsolutePath();

                    //Get the picture as a bitmap and set it to smaller imageview
                    Bitmap resultsPhotoBitmap = BitmapFactory.decodeFile(resultsPictureFilePath);
                    smallPictureBox.setImageBitmap(resultsPhotoBitmap);
                }

                //Enable save button if all requirements met
                if(dateEntered && coursePictureTaken && resultsPictureTaken)
                    btnSave.setEnabled(true);
            }
            else
            {
                Toast.makeText(getBaseContext(), "No photo saved", Toast.LENGTH_LONG).show();
            }
        }

        //Request code for results picture
        if(requestCode == 2)
        {
            //Check file was okay
            if(resultCode == RESULT_OK)
            {
                if(resultsPhotoFile != null)
                {
                    resultsPhotoFile.delete();
                    resultsPhotoFile = null;
                }
                resultsPictureTaken = true;

                //Save the file into appropriate place
                resultsPhotoFile = tempFile;

                //Make sure we have real file path
                String realFilePath = resultsPhotoFile.getAbsolutePath();

                //Get the picture as a bitmap and set to main display
                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);
                mainPictureBox.setImageBitmap(userPhotoBitmap);

                //If course picture has been taken, put it into smaller display
                if(coursePictureTaken)
                {
                    //Make sure we have real file path
                    String coursePictureFilePath = coursePhotoFile.getAbsolutePath();

                    //Get the picture as a bitmap and put into smaller display
                    Bitmap coursePhotoBitmap = BitmapFactory.decodeFile(coursePictureFilePath);
                    smallPictureBox.setImageBitmap(coursePhotoBitmap);
                }

                //Enable save button if requirements met
                if(dateEntered && coursePictureTaken && resultsPictureTaken)
                    btnSave.setEnabled(true);
            }
            else
            {
                Toast.makeText(getBaseContext(), "No photo saved", Toast.LENGTH_LONG).show();
            }
        }
    }
}
