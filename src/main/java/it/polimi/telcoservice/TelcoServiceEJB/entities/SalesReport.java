package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

@Entity
@Table(name = "salesReport", schema = "telco_service_db")
@NamedQuery(name = "SalesReport.findAll", query = "SELECT sr FROM SalesReport sr")
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
    @JoinColumn(name = "productPurchased")
    private int productPurchased;

    public String getPackageName() {
        return packageName;
    }

    public int getTotalPurchase() {
        return totalPurchase;
    }

    public int getTotal12() {
        return total12;
    }

    public int getTotal24() {
        return total24;
    }

    public int getTotal36() {
        return total36;
    }

    public float getNetValue() {
        return netValue;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public int getProductPurchased() {
        return productPurchased;
    }
}
