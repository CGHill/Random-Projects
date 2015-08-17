package bit.hillcg2.agilitytracker;


public class AgilityEntry {
    private int ID;
    private String date;
    private String filePath;
    private String dogClass;

    public AgilityEntry(int startID, String startDate, String startFilePath, String startDogClass){
        ID = startID;
        date = startDate;
        filePath = startFilePath;
        dogClass = startDogClass;
    }

    public String getFilePath(){
        return filePath;
    }

    public int getID(){
        return ID;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Class: " + dogClass;
    }
}
