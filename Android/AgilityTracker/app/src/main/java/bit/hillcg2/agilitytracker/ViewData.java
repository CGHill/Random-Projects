package bit.hillcg2.agilitytracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    //ArrayAdapter entryAdapter;
    AgilityEntryArrayAdapter entryAdapter;
    DBManager dbManager;
    ListView listView;
    Button btnDelete;
    Button btnView;
    ArrayList<AgilityEntry> allEntries;

    AgilityEntry selectedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        init();
    }

    public void init(){
        selectedEntry = null;

        //Get devices size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int listViewHeight = size.y - 500;

        listView = (ListView) findViewById(R.id.listView);
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

        //entryAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1);
        entryAdapter = new AgilityEntryArrayAdapter(getBaseContext(), R.layout.entry_custom_listview);

        listView.setAdapter(entryAdapter);

        dbManager = new DBManager(this);

        allEntries = dbManager.getAllEntries();

        for (AgilityEntry a : allEntries) {
            entryAdapter.add(a);
            entryAdapter.notifyDataSetChanged();
        }
    }

    public class listItemSelectHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedEntry = allEntries.get(position);
            btnDelete.setEnabled(true);
            btnView.setEnabled(true);
        }
    }

    public class viewEntryHandler implements OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    public class  deleteHandler implements OnClickListener{

        @Override
        public void onClick(View v) {
            File file = new File(selectedEntry.getFilePath());
            file.delete();

            entryAdapter.remove(selectedEntry);
            entryAdapter.notifyDataSetChanged();
            dbManager.deleteEntry(selectedEntry.getID());
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
        }
    }

    public class AgilityEntryArrayAdapter extends ArrayAdapter<AgilityEntry> {

        public AgilityEntryArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());

            View customView = inflater.inflate(R.layout.entry_custom_listview, container, false);

            ImageView itemImageView = (ImageView) customView.findViewById(R.id.ivItemImage);
            TextView itemTextView = (TextView) customView.findViewById(R.id.ivItemWords);

            AgilityEntry currentItem = getItem(position);

            String photoFilePath = currentItem.getFilePath();
            Bitmap currItemPhoto = getBitmap(photoFilePath);

            itemImageView.setImageBitmap(currItemPhoto);
            itemImageView.setRotation(90);
            itemTextView.setText(currentItem.toString());


            return customView;
        }

        private Bitmap getBitmap(String path) {

            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b = null;

            if (scale > 1)
            {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeFile(path, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            }
            else
            {
                b = BitmapFactory.decodeFile(path);
            }
            return b;
        }
    }
}
