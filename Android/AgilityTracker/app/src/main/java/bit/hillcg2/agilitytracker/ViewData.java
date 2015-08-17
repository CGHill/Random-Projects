package bit.hillcg2.agilitytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewData extends AppCompatActivity {

    ArrayAdapter entryAdapter;
    DBManager dbManager;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        listView = (ListView)findViewById(R.id.listView);

        entryAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1);

        listView.setAdapter(entryAdapter);

        dbManager = new DBManager(this);

        ArrayList<AgilityEntry> allEntries = new ArrayList<AgilityEntry>();

        allEntries = dbManager.getAllEntries();

        for(AgilityEntry a: allEntries)
        {
            String listString = "Date: " + a.getDate() + "\n";
            listString += "Class: " + a.getDogClass() + "\n";
            listString += "File Path: " + a.getFilePath() + "\n";

            entryAdapter.add(listString);
            entryAdapter.notifyDataSetChanged();
        }
    }
}
