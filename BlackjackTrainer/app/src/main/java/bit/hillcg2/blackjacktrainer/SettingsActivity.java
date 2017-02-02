package bit.hillcg2.blackjacktrainer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    //BJ widgets
    private EditText boxBlackjackHighest;
    private CheckBox checkBJPinksActive;
    private CheckBox checkBJBlacksActive;
    private CheckBox checkBJGreensActive;
    private CheckBox checkBJRedsActive;

    //Pairs widgets
    private EditText boxPairsHighest;
    private CheckBox checkPPGreensActive;
    private CheckBox checkPPRedsActive;
    private CheckBox checkPPYellowsActive;
    private CheckBox checkPPBluesActive;

    private Button btnSave;
    private Button btnCancel;

    private SharedPreferences prefs;
    private Editor prefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setup();
    }

    //Precondition:
    //Postcondition: Does initial setup of widgets
    private void setup()
    {
        prefs = getSharedPreferences("BlackjackTrainer", MODE_PRIVATE);
        prefsEditor = prefs.edit();

        boxBlackjackHighest = (EditText)findViewById(R.id.boxBlackjackHighest);
        checkBJPinksActive = (CheckBox)findViewById(R.id.checkBJPinksActive);
        checkBJBlacksActive = (CheckBox)findViewById(R.id.checkBJBlacksActive);
        checkBJGreensActive = (CheckBox)findViewById(R.id.checkBJGreensActive);
        checkBJRedsActive = (CheckBox)findViewById(R.id.checkBJRedsActive);


        boxPairsHighest = (EditText)findViewById(R.id.boxPairsHighest);
        checkPPGreensActive = (CheckBox)findViewById(R.id.checkPPGreensActive);
        checkPPRedsActive = (CheckBox)findViewById(R.id.checkPPRedsActive);
        checkPPYellowsActive = (CheckBox)findViewById(R.id.checkPPYellowsActive);
        checkPPBluesActive = (CheckBox)findViewById(R.id.checkPPBluesActive);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new ButtonSaveSettings());
        btnCancel.setOnClickListener(new ButtonCancelActivity());

        loadExistingSettings();
    }

    //Precondition:
    //Postcondition: Loads widgets with previous saved settings
    private void loadExistingSettings()
    {
        //BJ
        int BJHighestBetTotal = prefs.getInt("blackjackHighest", 200);
        boolean BJPinksActive = prefs.getBoolean("BJPinksActive", false);
        boolean BJBlacksActive = prefs.getBoolean("BJBlacksActive", false);
        boolean BJGreensActive = prefs.getBoolean("BJGreensActive", true);
        boolean BJRedsActive = prefs.getBoolean("BJRedsActive", true);

        //Set up BJ widgets
        boxBlackjackHighest.setText(String.valueOf(BJHighestBetTotal));
        checkBJPinksActive.setChecked(BJPinksActive);
        checkBJBlacksActive.setChecked(BJBlacksActive);
        checkBJGreensActive.setChecked(BJGreensActive);
        checkBJRedsActive.setChecked(BJRedsActive);

        //Pairs
        int PPHighestBetTotal = prefs.getInt("pairsHighest", 50);
        boolean PPGreensActive = prefs.getBoolean("PPGreensActive", true);
        boolean PPRedsActive = prefs.getBoolean("PPRedsActive", true);
        boolean PPYellowsActive = prefs.getBoolean("PPYellowsActive", false);
        boolean PPBluesActive = prefs.getBoolean("PPBluesActive", false);

        //Set up pairs widgets
        boxPairsHighest.setText(String.valueOf(PPHighestBetTotal));
        checkPPGreensActive.setChecked(PPGreensActive);
        checkPPRedsActive.setChecked(PPRedsActive);
        checkPPYellowsActive.setChecked(PPYellowsActive);
        checkPPBluesActive.setChecked(PPBluesActive);
    }


    //Handler to save settings
    public class ButtonSaveSettings implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //Get highest blackjack value, defaults to 200 if field is empty
            String bjHighestString = boxBlackjackHighest.getText().toString();
            int blackjackHighest;
            if(!bjHighestString.equals(""))
                blackjackHighest = Integer.parseInt(bjHighestString);
            else
                blackjackHighest = 200;

            //Get blackjack checkboxes values
            boolean BJPinksActive = checkBJPinksActive.isChecked();
            boolean BJBlacksActive = checkBJBlacksActive.isChecked();
            boolean BJGreensActive = checkBJGreensActive.isChecked();
            boolean BJRedsActive = checkBJRedsActive.isChecked();

            //Get highest pairs value, defaults to 50 if field is empty
            String pairsHighestString = boxPairsHighest.getText().toString();
            int pairsHighest;
            if(!pairsHighestString.equals("")) {
                pairsHighest = Integer.parseInt(boxPairsHighest.getText().toString());

                if(pairsHighest > 50)
                    pairsHighest = 50;
            }
            else
                pairsHighest = 50;

            //Get pairs checkbox values
            boolean PPGreensActive = checkPPGreensActive.isChecked();
            boolean PPRedsActive = checkPPRedsActive.isChecked();
            boolean PPYellowsActive = checkPPYellowsActive.isChecked();
            boolean PPBluesActive = checkPPBluesActive.isChecked();

            //Save all of the settings into shared preferences
            prefsEditor.putInt("blackjackHighest", blackjackHighest);
            prefsEditor.putBoolean("BJPinksActive", BJPinksActive);
            prefsEditor.putBoolean("BJBlacksActive", BJBlacksActive);
            prefsEditor.putBoolean("BJGreensActive", BJGreensActive);
            prefsEditor.putBoolean("BJRedsActive", BJRedsActive);


            prefsEditor.putInt("pairsHighest", pairsHighest);
            prefsEditor.putBoolean("PPGreensActive", PPGreensActive);
            prefsEditor.putBoolean("PPRedsActive", PPRedsActive);
            prefsEditor.putBoolean("PPYellowsActive", PPYellowsActive);
            prefsEditor.putBoolean("PPBluesActive", PPBluesActive);

            prefsEditor.commit();

            //Inform user that save was successful
            Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_LONG).show();

            //Close activity
            finish();
        }
    }

    //Handler for cancel button, closes activity
    public class ButtonCancelActivity implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
