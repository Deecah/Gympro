
package model;

import java.time.LocalDateTime;

public class Certification {
    private int certificationID;
    private String name;
    private String description;
    private LocalDateTime expireDate;

    public Certification(int certificationID, String name, String description, LocalDateTime expireDate) {
        this.certificationID = certificationID;
        this.name = name;
        this.description = description;
        this.expireDate = expireDate;
    }

    public Certification() {
    }

    public int getCertificationID() {
        return certificationID;
    }

    public void setCertificationID(int certificationID) {
        this.certificationID = certificationID;
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

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "Certification{" + "certificationID=" + certificationID + ", name=" + name + ", description=" + description + ", expireDate=" + expireDate + '}';
    }
    
    
    
    
    
}
