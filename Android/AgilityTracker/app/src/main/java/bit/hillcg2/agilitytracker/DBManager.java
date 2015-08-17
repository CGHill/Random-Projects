package bit.hillcg2.agilitytracker;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    //Global variables
    private SQLiteDatabase AgilityDB;
    private Context context;
    private String createQuery;

    public DBManager(Context activityContext){
        //Set context to global so other methods can use it
        context = activityContext;

        //Open existing or create new database
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);

        //Create the table in case it doesn't already exist
        createQuery = "CREATE TABLE IF NOT EXISTS tblAgilityTracker(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT NOT NULL, filePath TEXT NOT NULL, dogClass TEXT NOT NULL)";
        AgilityDB.execSQL(createQuery);

        //Close connection
        AgilityDB.close();
    }

    public void insertEntry(String date, String filePath, String newDogClass){
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);

        //Insert incident into database
        String insertQuery = "INSERT INTO tblAgilityTracker VALUES(null, '" + date + "','" + filePath  + "','"+ newDogClass + "')";
        AgilityDB.execSQL(insertQuery);
        AgilityDB.close();
    }

    public ArrayList<AgilityEntry> getAllEntries(){
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);

        String selectQuery = "SELECT * FROM tblAgilityTracker";

        Cursor recordSet = AgilityDB.rawQuery(selectQuery, null);

        //Set up to loop
        recordSet.moveToFirst();

        int recordCount = recordSet.getCount();

        ArrayList<AgilityEntry> allEntires = new ArrayList<AgilityEntry>();

        //Loop over all of the records
        for(int i=0; i < recordCount; i++)
        {
            int dateIndex = recordSet.getColumnIndex("date");
            String date = recordSet.getString(dateIndex);

            int filePathIndex = recordSet.getColumnIndex("filePath");
            String filePath = recordSet.getString(filePathIndex);

            int dogClassIndex = recordSet.getColumnIndex("dogClass");
            String currDogClass = recordSet.getString(dogClassIndex);

            AgilityEntry newEntry = new AgilityEntry(date, filePath, currDogClass);

            allEntires.add(newEntry);

            //Get next record
            recordSet.moveToNext();
        }

        return allEntires;
    }
}
