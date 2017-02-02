package bit.hillcg2.blackjacktrainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setUpListMenu();
    }

    //Precondition:
    //Postcondition: sets up navigation list
    private void setUpListMenu()
    {
        String[] menuItems = {"Blackjack Payouts", "Perfect Pairs Payouts", "Instructions", "Settings"};

        ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);

        ListView lvMenu = (ListView)findViewById(R.id.lvMenu);
        lvMenu.setAdapter((menuItemAdapter));

        lvMenu.setOnItemClickListener(new ListViewMenuHandler());
    }

    //Handler for nav list
    public class ListViewMenuHandler implements OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> list, View item, int position, long id)
        {
            //Get item that was tapped
            String clickedItemString = list.getItemAtPosition(position).toString();

            Intent newIntent;

            //Set up intent to move activity
            switch(clickedItemString)
            {
                case "Blackjack Payouts":
                    newIntent = new Intent(getBaseContext(), PayoutsActivity.class);
                    newIntent.putExtra("PAYOUT_TYPE", "blackjack");
                    break;
                case "Perfect Pairs Payouts":
                    newIntent = new Intent(getBaseContext(), PayoutsActivity.class);
                    newIntent.putExtra("PAYOUT_TYPE", "pairs");
                    break;
                case "Settings":
                    newIntent = new Intent(getBaseContext(), SettingsActivity.class);
                    break;
                case "Instructions":
                    newIntent = new Intent(getBaseContext(), InstructionsActivity.class);
                    break;
                default:
                    newIntent = null;
            }

            //Start new activity
            if(newIntent != null)
                startActivity(newIntent);
        }
    }
}
