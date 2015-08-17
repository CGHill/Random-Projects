package bit.hillcg2.agilitytracker;


public class AgilityEntry {
    private String date;
    private String filePath;
    private String dogClass;

    public AgilityEntry(String startDate, String startFilePath, String startDogClass){
        date = startDate;
        filePath = startFilePath;
        dogClass = startDogClass;
    }

    public void setDate(String newDate){
        date = newDate;
    }

    public void setFilePath(String newFilePath){
        filePath = newFilePath;
    }

    public void setDogClass(String newDogClass){
        filePath = newDogClass;
    }

    public String getDate(){
        return date;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getDogClass(){
        return dogClass;
    }
}
