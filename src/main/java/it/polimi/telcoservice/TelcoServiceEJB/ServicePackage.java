package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_package", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "ServicePackage.findByOptional", query = "SELECT sp FROM ServicePackage sp")
})
public class ServicePackage {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private int packageID;
    private String name;
    private boolean fixed_phone;

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

    public boolean isFixed_phone() {
        return fixed_phone;
    }

    public void setFixed_phone(boolean fixed_phone) {
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
