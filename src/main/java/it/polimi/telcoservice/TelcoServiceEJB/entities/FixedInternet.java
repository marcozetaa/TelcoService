package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "fixed_internet", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "FixedInternet.findAll", query = "SELECT fi FROM FixedInternet fi"),
})
public class FixedInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    private int num_gb;
    private int fee_extra_gb;

    public FixedInternet(){
    }

    public FixedInternet(int numGB, int extraGB){
        this.num_gb = numGB;
        this.fee_extra_gb = extraGB;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getNumGB() {
        return num_gb;
    }

    public void setNumGB(int numGB) {
        this.num_gb = numGB;
    }

    public int getExtraGB() {
        return fee_extra_gb;
    }

    public void setExtraGB(int extraGB) {
        this.fee_extra_gb = extraGB;
    }
}
