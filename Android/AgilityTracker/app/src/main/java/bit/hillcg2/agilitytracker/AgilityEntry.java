package bit.hillcg2.agilitytracker;

//Model class
public class AgilityEntry {
    private int ID;
    private String date;
    private String courseFilePath;
    private String resultFilePath;
    private String dogClass;

    public AgilityEntry(int startID, String startDate, String startCourseFilePath, String startResultsFilePath, String startDogClass){
        ID = startID;
        date = startDate;
        courseFilePath = startCourseFilePath;
        resultFilePath = startResultsFilePath;
        dogClass = startDogClass;
    }

    public String getCourseFilePath(){
        return courseFilePath;
    }

    public String getResultFilePathFilePath(){
        return resultFilePath;
    }

    public int getID(){
        return ID;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Class: " + dogClass;
    }
}
