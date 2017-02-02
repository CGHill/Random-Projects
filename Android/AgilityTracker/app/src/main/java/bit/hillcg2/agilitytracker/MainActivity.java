package bit.hillcg2.agilitytracker;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    //Buttons
    private ImageButton imgBtnViewData;
    private ImageButton imgBtnEnterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load pictures for button, user another class that reduces size of picture to save memory
        Resources res = getResources();
        PictureLoader pictureLoader = new PictureLoader();
        Bitmap pic = pictureLoader.getBitmapFromDrawable(res, R.drawable.screen1);
        Bitmap pic2 = pictureLoader.getBitmapFromDrawable(res, R.drawable.screen2);

        //Set up buttons
        imgBtnEnterData = (ImageButton)findViewById(R.id.imgBtnEnterData);
        imgBtnEnterData.setOnClickListener(new enterDataHandler());
        imgBtnEnterData.setImageBitmap(pic);

        imgBtnViewData = (ImageButton)findViewById(R.id.imgBtnViewData);
        imgBtnViewData.setOnClickListener(new viewDataHandler());
        imgBtnViewData.setImageBitmap(pic2);
    }

    //Handler to move to EnterData screen
    public class enterDataHandler implements OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(),EnterData.class);
            startActivity(i);
        }
    }

    //Handler to move to ViewData screen
    public class viewDataHandler implements OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), ViewData.class);
            startActivity(i);
        }
    }
}
