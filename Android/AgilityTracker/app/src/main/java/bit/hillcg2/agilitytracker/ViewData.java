package bit.hillcg2.agilitytracker;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import android.view.View.OnClickListener;

public class ViewData extends AppCompatActivity {

    //Global variables
    private AgilityEntryArrayAdapter entryAdapter;
    private DBManager dbManager;
    private ListView listView;
    private Button btnDelete;
    private Button btnView;
    private ArrayList<AgilityEntry> allEntries;
    private PictureLoader pictureLoader;
    private AgilityEntry selectedEntry;
    private ConfirmDialog confirmDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        init();
    }

    //Method to initiate everything
    public void init(){
        selectedEntry = null;

        //Get devices size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Give button enough room at the bottom of the screen
        int listViewHeight = size.y - 500;

        //Get references to XML widgets and do setup
        listView = (ListView) findViewById(R.id.listView);

        //Set list size so button has room at bottom of screen no matter which device
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        params.height = listViewHeight;
        listView.setLayoutParams(params);
        listView.setOnItemClickListener(new listItemSelectHandler());

        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new deleteHandler());
        btnDelete.setEnabled(false);

        btnView = (Button)findViewById(R.id.btnView);
        btnView.setOnClickListener(new viewEntryHandler());
        btnView.setEnabled(false);

        //Custom array adapter for listview
        entryAdapter = new AgilityEntryArrayAdapter(getBaseContext(), R.layout.entry_custom_listview);

        listView.setAdapter(entryAdapter);

        dbManager = new DBManager(this);
        pictureLoader = new PictureLoader();

        //Load in all entries into list
        allEntries = dbManager.getAllEntries();

        for (AgilityEntry a : allEntries) {
            entryAdapter.add(a);
            entryAdapter.notifyDataSetChanged();
        }
    }

    //Handler to select items from the listview
    public class listItemSelectHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Update current selected item
            selectedEntry = allEntries.get(position);

            //Enable buttons for use
            btnDelete.setEnabled(true);
            btnView.setEnabled(true);
        }
    }

    //Handler to move to screen to view entries in more detail
    public class viewEntryHandler implements OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), ViewEntry.class);

            //Pass ID of selected into next ViewEntry
            int selectedID = selectedEntry.getID();
            i.putExtra("selectedID", selectedID);

            startActivity(i);
            finish();
        }
    }

    //Called from ConfirmDialog class to make sure that user really wants to delete file
    public void confirmDelete(Boolean deleteConfirmed){
        //If user said they wanted to delete
        if(deleteConfirmed)
        {
            //Delete photo from phone
            File file = new File(selectedEntry.getCourseFilePath());
            file.delete();

            //Remove entry from list
            entryAdapter.remove(selectedEntry);
            entryAdapter.notifyDataSetChanged();

            //Remove entry from database
            dbManager.deleteEntry(selectedEntry.getID());

            //Disable buttons again to prevent errors
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
        }
    }

    //Handler for delete button
    public class  deleteHandler implements OnClickListener{
        @Override
        public void onClick(View v) {

            //Open a dialog to confirm user wants to delete
            confirmDelete = new ConfirmDialog("Delete?");
            FragmentManager fm = getFragmentManager();
            confirmDelete.show(fm, "Confirm");
        }
    }

    //Inner class for custom array adapter
    public class AgilityEntryArrayAdapter extends ArrayAdapter<AgilityEntry> {

        //Constructor
        public AgilityEntryArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        //Called by system to set up the listview
        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());

            //Inflate the layout
            View customView = inflater.inflate(R.layout.entry_custom_listview, container, false);

            //Get references to widgets
            ImageView itemImageView = (ImageView) customView.findViewById(R.id.ivItemImage);
            TextView itemTextView = (TextView) customView.findViewById(R.id.ivItemWords);

            //Get the current item
            AgilityEntry currentItem = getItem(position);

            //Put load picture
            String photoFilePath = currentItem.getCourseFilePath();
            Bitmap currItemPhoto = pictureLoader.getBitmap(photoFilePath);

            //Put picture into listview and text into textview
            itemImageView.setImageBitmap(currItemPhoto);
            itemTextView.setText(currentItem.toString());

            //Return the layout
            return customView;
        }
    }
}
