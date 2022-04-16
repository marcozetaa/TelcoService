package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

@Entity
@Table(name = "salesReport", schema = "telco_service_db")
public class SalesReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "PackageName")
    private String packageName;
    @JoinColumn(name = "TotalPurchase")
    private int totalPurchase;
    @JoinColumn(name = "TotalFor12")
    private int total12;
    @JoinColumn(name = "TotalFor24")
    private int total24;
    @JoinColumn(name = "TotalFor36")
    private int total36;
    @JoinColumn(name = "NetValue")
    private float netValue;
    @JoinColumn(name = "TotalValue")
    private float totalValue;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(int totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public int getTotal12() {
        return total12;
    }

    public void setTotal12(int total12) {
        this.total12 = total12;
    }

    public int getTotal24() {
        return total24;
    }

    public void setTotal24(int total24) {
        this.total24 = total24;
    }

    public int getTotal36() {
        return total36;
    }

    public void setTotal36(int total36) {
        this.total36 = total36;
    }

    public float getNetValue() {
        return netValue;
    }

    public void setNetValue(float netValue) {
        this.netValue = netValue;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }
}
