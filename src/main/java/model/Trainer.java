package model;

public class Trainer extends User {
    private Integer experienceYears; 
    private String description;      
    private String specialization;   

    public Trainer() {
        super(); // Gọi constructor của lớp User
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
        return "Trainer{" +
                ", userName=" + getUserName() +
                ", gender=" + getGender() +
                ", email=" + getEmail() +
                ", address=" + getAddress() +
                ", avatarUrl=" + getAvatarUrl() +
                ", role=" + getRole() +
                ", status=" + getStatus() +
                ", experienceYears=" + experienceYears +
                ", description='" + description + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}