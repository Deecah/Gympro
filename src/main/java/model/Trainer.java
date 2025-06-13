package model;

public class Trainer extends User{
    private int experienceYears;
    private String description;
    private String specialization;

    public Trainer(int experienceYears, String description, String specialization) {
        this.experienceYears = experienceYears;
        this.description = description;
        this.specialization = specialization;
    }

    public Trainer() {
    }


    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperienceYears() {
        return experienceYears;
    }


}
