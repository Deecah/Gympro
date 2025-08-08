package model;

public class Customer extends User{

    private Double weight;             
    private Double height;             
    private String goal;              
    private String medicalConditions;  

    public Customer() {
    }

    public Customer(Double weight, Double height, String goal, String medicalConditions) {
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.medicalConditions = medicalConditions;
    }

    public Customer(Double weight, Double height, String goal, String medicalConditions, String userName, String email, byte[] password, String role) {
        super(userName, email, password, role);
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.medicalConditions = medicalConditions;
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
        return "Customer{" + "weight=" + weight + ", height=" + height + ", goal=" + goal + ", medicalConditions=" + medicalConditions + '}';
    }


}