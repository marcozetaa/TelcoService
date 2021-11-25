package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ServicePackage {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private int packageID;
    private String name;
    private boolean fixed_phone;

    //aggiungere in name il nome della tabella nel db
    @OneToOne(mappedBy = "package", cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription subscription;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "fixed_internet_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "fixed_internetID") }
    )
    private List<FixedInternet> fixedInternets;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "mobile_internet_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "mobile_InternetID") }
    )
    private List<MobileInternet> mobileInternets;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "mobile_phone_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "mobile_PhoneID") }
    )
    private List<MobilePhone> mobilePhones;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "optional_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "optional_name") }
    )
    private List<OptionalProduct> optionalProducts;

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

    public List<FixedInternet> getFixedInternets() {
        return fixedInternets;
    }

    public void addFixedInternet (FixedInternet fi){
        getFixedInternets().add(fi);
    }

    public List<MobileInternet> getMobileInternets() {
        return mobileInternets;
    }

    public void addMobileInternet(MobileInternet mi){
        getMobileInternets().add(mi);
    }

    public List<MobilePhone> getMobilePhones() {
        return mobilePhones;
    }

    public void addMobilePhone(MobilePhone mp){
        getMobilePhones().add(mp);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
