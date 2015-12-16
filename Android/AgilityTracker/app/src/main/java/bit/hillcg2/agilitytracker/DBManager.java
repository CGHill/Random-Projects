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

        //String dropQuery = "drop table tblAgilityTracker";
        //AgilityDB.execSQL(dropQuery);

        //Create the table in case it doesn't already exist
        createQuery = "CREATE TABLE IF NOT EXISTS tblAgilityTracker(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT NOT NULL, coursePictureFilePath TEXT NOT NULL, resultsPictureFilePath TEXT NOT NULL, " +
                "dogClass TEXT NOT NULL)";
        AgilityDB.execSQL(createQuery);

        //Close connection
        AgilityDB.close();
    }

    //Makes a new Entry in the database for the data passed in
    public void insertEntry(String date, String courseFilePath, String resultsFilePath, String newDogClass){
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);

        //Insert incident into database
        String insertQuery = "INSERT INTO tblAgilityTracker VALUES(null, '" + date + "','" + courseFilePath +
                "','" + resultsFilePath + "','"+ newDogClass + "')";
        AgilityDB.execSQL(insertQuery);
        AgilityDB.close();
    }

    //Returns all entries as an Arraylist from the database
    public ArrayList<AgilityEntry> getAllEntries(){
        //Reconnect to database
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);

        //Query to get all entries
        String selectQuery = "SELECT * FROM tblAgilityTracker";
        Cursor recordSet = AgilityDB.rawQuery(selectQuery, null);

        //Set up to loop
        recordSet.moveToFirst();

        int recordCount = recordSet.getCount();

        //Initiate arraylist
        ArrayList<AgilityEntry> allEntires = new ArrayList<AgilityEntry>();

        //Loop over all of the records
        for(int i=0; i < recordCount; i++)
        {
            //Get all of the values for an entry
            int idIndex = recordSet.getColumnIndex("ID");
            int id = recordSet.getInt(idIndex);

            int dateIndex = recordSet.getColumnIndex("date");
            String date = recordSet.getString(dateIndex);

            int coursePictureFilePathIndex = recordSet.getColumnIndex("coursePictureFilePath");
            String coursePictureFilePath = recordSet.getString(coursePictureFilePathIndex);

            int resultsPictureFilePathIndex = recordSet.getColumnIndex("resultsPictureFilePath");
            String resultsPictureFilePath = recordSet.getString(resultsPictureFilePathIndex);

            int dogClassIndex = recordSet.getColumnIndex("dogClass");
            String currDogClass = recordSet.getString(dogClassIndex);

            //Create and store entry in arraylist
            AgilityEntry newEntry = new AgilityEntry(id, date, coursePictureFilePath, resultsPictureFilePath, currDogClass);
            allEntires.add(newEntry);

            //Get next record
            recordSet.moveToNext();
        }

        //Close database
        AgilityDB.close();

        return allEntires;
    }

    //Deletes a single entry from the database
    public void deleteEntry(int id){
        //Query string
        String deleteQuery = "DELETE FROM tblAgilityTracker WHERE ID=" + id;

        //Connect to DB, execute query and close
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);
        AgilityDB.execSQL(deleteQuery);
        AgilityDB.close();

    }

    //Finds and returns a single Entry from database
    public AgilityEntry getEntry(int id){
        //Set up query
        String selectQuery = "SELECT * FROM tblAgilityTracker WHERE ID=" + id;

        //Reconnect to DB
        AgilityDB = context.openOrCreateDatabase("agilityDB", context.MODE_PRIVATE, null);
        Cursor recordSet = AgilityDB.rawQuery(selectQuery, null);

        //Set up to loop
        recordSet.moveToFirst();

        int recordCount = recordSet.getCount();

        AgilityEntry foundEntry = null;

        if(recordCount > 0)
        {
            //Get all data for a single entry
            int dateIndex = recordSet.getColumnIndex("date");
            String date = recordSet.getString(dateIndex);

            int coursePictureFilePathIndex = recordSet.getColumnIndex("coursePictureFilePath");
            String coursePictureFilePath = recordSet.getString(coursePictureFilePathIndex);

            int resultsPictureFilePathIndex = recordSet.getColumnIndex("resultsPictureFilePath");
            String resultsPictureFilePath = recordSet.getString(resultsPictureFilePathIndex);

            int dogClassIndex = recordSet.getColumnIndex("dogClass");
            String currDogClass = recordSet.getString(dogClassIndex);

            //Create entry
            foundEntry = new AgilityEntry(id, date, coursePictureFilePath, resultsPictureFilePath, currDogClass);
        }

        //Close DB
        AgilityDB.close();

        //Return entry
        return foundEntry;
    }
}
