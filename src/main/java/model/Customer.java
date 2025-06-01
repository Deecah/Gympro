
package model;

public class Customer extends User{
    private double weight;
    private double height;
    private String goal;
    private String medical_conditions;

    public Customer(double weight, double height, String goal, String medical_conditions) {
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.medical_conditions = medical_conditions;
    }

    public Customer() {
    }
    
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getMedical_conditions() {
        return medical_conditions;
    }

    public void setMedical_conditions(String medical_conditions) {
        this.medical_conditions = medical_conditions;
    }

    @Override
    public String toString() {
        return "Customer{" + "weight=" + weight + ", height=" + height + ", goal=" + goal + ", medical_conditions=" + medical_conditions + '}';
    }
   
    
}
