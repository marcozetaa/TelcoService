package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mobile_phone", schema = "telco_service_db")
public class MobilePhone{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobilePhoneID;

    @OneToMany(mappedBy = "mobilePhone")
    private List<ServicePackage> servicePackage;

    private int num_min;
    private int num_sms;
    private int extra_min;
    private int extra_sms;

    public int getMobilePhoneID() {
        return mobilePhoneID;
    }

    public void setMobilePhoneID(int mobilePhoneID) {
        this.mobilePhoneID = mobilePhoneID;
    }

    public int getNum_min() {
        return num_min;
    }

    public void setNum_min(int num_min) {
        this.num_min = num_min;
    }

    public int getNum_sms() {
        return num_sms;
    }

    public void setNum_sms(int num_sms) {
        this.num_sms = num_sms;
    }

    public int getExtra_min() {
        return extra_min;
    }

    public void setExtra_min(int extra_min) {
        this.extra_min = extra_min;
    }

    public int getExtra_sms() {
        return extra_sms;
    }

    public void setExtra_sms(int extra_sms) {
        this.extra_sms = extra_sms;
    }
}
