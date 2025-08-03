package model;

public class Trainer extends User{
    private Integer experienceYears; 
    private String description;      
    private String specialization;   

    public Trainer() {
    }
    
    public Trainer(Integer experienceYears, String description, String specialization) {
        this.experienceYears = experienceYears;
        this.description = description;
        this.specialization = specialization;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
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

    @Override
    public String toString() {
        return "Trainer{" + "experienceYears=" + experienceYears + ", description=" + description + ", specialization=" + specialization + '}';
    }


}