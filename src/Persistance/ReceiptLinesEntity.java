package Persistance;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ReceiptLines", schema = "zw9P3SlfWt", catalog = "")
public class ReceiptLinesEntity {
    private int receiptId;
    private String prodName;
    private Integer price;
    private Integer amount;
    private Integer productId;

    @Basic
    @Column(name = "receiptId")
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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
    public Integer getPrice() {
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

    @Basic
    @Column(name = "productId")
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptLinesEntity that = (ReceiptLinesEntity) o;
        return receiptId == that.receiptId && Objects.equals(prodName, that.prodName) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptId, prodName, price, amount, productId);
    }
}
