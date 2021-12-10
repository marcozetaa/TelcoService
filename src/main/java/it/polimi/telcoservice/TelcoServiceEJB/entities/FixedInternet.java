package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fixed_internet", schema = "telco_service_db")
public class FixedInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int fixedInternetID;

    @OneToMany(mappedBy = "fixedInternet")
    private List<ServicePackage> servicePackage;

    public FixedInternet(){
    }

    public FixedInternet(int numGB, int extraGB){
        this.numGB = numGB;
        this.extraGB = extraGB;
    }

    private int numGB;
    private int extraGB;

    public int getFixedInternetID() {
        return fixedInternetID;
    }

    public void setFixedInternetID(int fixedInternetID) {
        this.fixedInternetID = fixedInternetID;
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
