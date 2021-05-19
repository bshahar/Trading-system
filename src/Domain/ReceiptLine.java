package Domain;

import Persistance.ReceiptLinesEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ReceiptLines", schema = "zw9P3SlfWt", catalog = "")
public class ReceiptLine {

    String prodName;
    double price;
    int amount;

    public ReceiptLine(){}

    public ReceiptLine(String Prod, double price, int amount) {
        this.prodName = Prod;
        this.price = price;
        this.amount = amount;
    }

    @Basic
    @Column(name = "prodName")
    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "amount")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptLine that = (ReceiptLine) o;
        return Objects.equals(prodName, that.prodName) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodName, price, amount);
    }
}
