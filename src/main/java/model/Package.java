package model;

public class Package {

    private int packageID;
    private int trainerID;
    private String name;
    private String description;
    private double price;
    private int duration; //theo thang, vi du 1, thang 2 thang

    public Package() {
    }

    public Package(int packageID, int trainerID, String name, String description, double price, int duration) {
        this.packageID = packageID;
        this.trainerID = trainerID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

}
