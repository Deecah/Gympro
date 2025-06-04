package model;

public class Customer extends User {
    private Double weight;             
    private Double height;             
    private String goal;              
    private String medicalConditions;  

    public Customer() {
        super(); // Gọi constructor của lớp cha (User)
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", userName='" + getUserName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", avatarUrl='" + getAvatarUrl() + '\'' +
                ", role='" + getRole() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", goal='" + goal + '\'' +
                ", medicalConditions='" + medicalConditions + '\'' +
                '}';
    }
}