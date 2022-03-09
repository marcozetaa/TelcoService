package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "mobile_phone", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "MobilePhone.findAll", query = "SELECT mp FROM MobilePhone mp"),
})
public class MobilePhone{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    private int num_minutes;
    private int num_sms;
    private int fee_extra_minutes;
    private int fee_extra_sms;

    public MobilePhone(){
    }

    public MobilePhone(int num_min, int num_sms, int extra_min, int extra_sms){
        this.num_minutes = num_min;
        this.num_sms = num_sms;
        this.fee_extra_minutes = extra_min;
        this.fee_extra_sms = extra_sms;
    }

    public int getid() {
        return this.id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getNum_min() {
        return num_minutes;
    }

    public void setNum_min(int num_min) {
        this.num_minutes = num_min;
    }

    public int getNum_sms() {
        return num_sms;
    }

    public void setNum_sms(int num_sms) {
        this.num_sms = num_sms;
    }

    public int getExtra_min() {
        return fee_extra_minutes;
    }

    public void setExtra_min(int extra_min) {
        this.fee_extra_minutes = extra_min;
    }

    public int getExtra_sms() {
        return fee_extra_sms;
    }

    public void setExtra_sms(int extra_sms) {
        this.fee_extra_sms = extra_sms;
    }

}
