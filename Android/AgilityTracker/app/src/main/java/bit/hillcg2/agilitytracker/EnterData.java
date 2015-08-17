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
import android.view.inputmethod.EditorInfo;
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
    File photoFile;
    ImageView usersPictureBox;
    Button btnTakePicture;
    Button btnSave;
    DBManager dbManager;
    Spinner spinClass;
    String[] classes;
    ArrayAdapter classesAdapter;
    EditText dateBox;

    Boolean pictureTaken;
    Boolean dateEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        dbManager = new DBManager(this);

        pictureTaken = false;
        dateEntered = false;

        usersPictureBox = (ImageView)findViewById(R.id.usersPictureBox);
        btnTakePicture = (Button)findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new photoIntent());

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new saveData());
        btnSave.setEnabled(false);

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
                dateEntered = true;

                if(dateEntered && pictureTaken)
                    btnSave.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Creates a file with a unique file name
    public void makePhotoFile(){
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
       // File photoFile = new File(imageStorageDirectory.getPath() + File.separator + mPhotoFileName);
        photoFile = new File(imageStorageDirectory.getPath() + File.separator + mPhotoFileName);

        //return photoFile;
    }

    public class saveData implements OnClickListener{

        @Override
        public void onClick(View v) {
            String enteredDate = dateBox.getText().toString();
            String dogClass = spinClass.getSelectedItem().toString();
            String pictureFilePath = photoFile.getAbsolutePath();

            dbManager.insertEntry(enteredDate, pictureFilePath, dogClass);

            dateBox.setText("");
            spinClass.setSelection(0);
            usersPictureBox.setImageDrawable(null);
            btnSave.setEnabled(false);

            Toast.makeText(getBaseContext(), "Entry Saved", Toast.LENGTH_LONG).show();
        }
    }

    //Button click handler
    public class photoIntent implements OnClickListener {

        @Override
        public void onClick(View view) {
            //Make file
            makePhotoFile();

            Uri photoFileUri = Uri.fromFile(photoFile);

            //Make camera intent
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Pass data through to camera
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            //Start camera
            startActivityForResult(imageCaptureIntent, 1);
        }
    }

    //When camera returns
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //Request code from camera
        if(requestCode ==1)
        {
            //Check file was okay
            if(resultCode == RESULT_OK)
            {
                //Make sure we have real file path
                String realFilePath = photoFile.getAbsolutePath();

                //Get the picture as a bitmap
                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                usersPictureBox.setImageBitmap(userPhotoBitmap);

                pictureTaken = true;

                if(dateEntered && pictureTaken)
                    btnSave.setEnabled(true);
            }
            else
            {
                Toast.makeText(getBaseContext(), "No photo saved", Toast.LENGTH_LONG).show();
            }
        }
    }
}
