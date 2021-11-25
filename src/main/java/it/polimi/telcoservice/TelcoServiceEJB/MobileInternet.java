package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mobile_internet", schema = "telco_service_db")
public class MobileInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobileInternetID;

    @OneToMany(mappedBy = "mobileInternet")
    private List<ServicePackage> servicePackage;

    private int numGB;
    private int extraGB;

    public int getMobileInternetID() {
        return mobileInternetID;
    }

    public void setMobileInternetID(int mobileInternetID) {
        this.mobileInternetID = mobileInternetID;
    }

    public int getNumGB() {
        return numGB;
    }

    public void setNumGB(int numGB) {
        this.numGB = numGB;
    }

    public int getExtraGB() {
        return extraGB;
    }

    public void setExtraGB(int extraGB) {
        this.extraGB = extraGB;
    }
}
