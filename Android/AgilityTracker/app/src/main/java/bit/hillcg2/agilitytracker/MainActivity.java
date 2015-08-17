package bit.hillcg2.agilitytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    Button btnEnterData;
    Button btnViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnterData = (Button)findViewById(R.id.btnEnterData);
        btnEnterData.setOnClickListener(new enterDataHandler());

        btnViewData = (Button)findViewById(R.id.btnViewData);
        btnViewData.setOnClickListener(new viewDataHandler());



    }

    public class enterDataHandler implements OnClickListener{

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(),EnterData.class);
            startActivity(i);
        }
    }

    public class viewDataHandler implements OnClickListener{

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), ViewData.class);
            startActivity(i);
        }
    }
}
