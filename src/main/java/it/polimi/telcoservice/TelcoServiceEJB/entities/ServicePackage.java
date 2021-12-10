package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_package", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "ServicePackage.findByOptionalProduct", query = "SELECT sp FROM ServicePackage sp WHERE sp.optionalProducts=?1"),
        @NamedQuery(name = "ServicePackage.findByID", query = "SELECT sp FROM ServicePackage sp WHERE sp.packageID=?1"),
        @NamedQuery(name = "ServicePackage.findByFI", query = "SELECT sp FROM ServicePackage sp WHERE sp.fixedInternet=?1"),
        @NamedQuery(name = "ServicePackage.findByMI", query = "SELECT sp FROM ServicePackage sp WHERE sp.mobileInternet=?1"),
        @NamedQuery(name = "ServicePackage.findByMP", query = "SELECT sp FROM ServicePackage sp WHERE sp.mobilePhone=?1")
})
public class ServicePackage {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private int packageID;
    private String name;
    private FixedPhoneStatus fixed_phone;
    private double fee12;
    private double fee24;
    private double fee36;

    @OneToMany(mappedBy = "package", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinTable(name = "fixed_internetID")
    private FixedInternet fixedInternet;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinTable(name = "mobile_internetID")
    private MobileInternet mobileInternet;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinTable(name = "mobile_phoneID")
    private MobilePhone mobilePhone;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "optional_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "optional_name") }
    )
    private ArrayList<OptionalProduct> optionalProducts;

    public ServicePackage(){
    }

    public ServicePackage(String name,FixedPhoneStatus fixed_phone,int fee12, int fee24,int fee36){
        this.name = name;
        this.fee12 = fee12;
        this.fee24 = fee24;
        this.fee36 = fee36;
        this.fixed_phone = fixed_phone;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FixedPhoneStatus isFixed_phone() {
        return this.fixed_phone;
    }

    public void setFixed_phone(FixedPhoneStatus fixed_phone) {
        this.fixed_phone = fixed_phone;
    }

    public List<OptionalProduct> getOptionalProducts() {
        return optionalProducts;
    }

    public void addOptionalProduct(OptionalProduct op){
        getOptionalProducts().add(op);
        op.getServicePackage().add(this);
    }

    public void removeOptionalProduct(OptionalProduct op){
        getOptionalProducts().remove(op);
    }

    public FixedInternet getFixedInternet() {
        return fixedInternet;
    }

    public void setFixedInternet(FixedInternet fixedInternet) {
        this.fixedInternet = fixedInternet;
    }

    public MobileInternet getMobileInternet() {
        return mobileInternet;
    }

    public void setMobileInternet(MobileInternet mobileInternet) {
        this.mobileInternet = mobileInternet;
    }

    public MobilePhone getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(MobilePhone mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void addSubscriptions(Subscription subscriptions) {
        this.subscriptions.add(subscriptions);
    }
}
