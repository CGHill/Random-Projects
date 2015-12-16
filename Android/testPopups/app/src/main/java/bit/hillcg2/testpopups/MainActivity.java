package bit.hillcg2.testpopups;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btn1;
    PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                MarkerInfo mi = new MarkerInfo();
                mi.show(fm, "Dialog");
            }
        });
    }

    public void blah(){
        Toast.makeText(getBaseContext(), "Blah", Toast.LENGTH_LONG).show();
    }
}
