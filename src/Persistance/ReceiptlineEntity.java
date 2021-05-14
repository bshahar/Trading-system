package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "receiptline", schema = "tradingsystemproject", catalog = "")
@IdClass(ReceiptlineEntityPK.class)
public class ReceiptlineEntity {
    private int receipId;
    private int receiptLineId;
    private String prodName;
    private Integer price;
    private Integer amount;

    @Id
    @Column(name = "receipId", nullable = false)
    public int getReceipId() {
        return receipId;
    }

    public void setReceipId(int receipId) {
        this.receipId = receipId;
    }

    @Id
    @Column(name = "receiptLineId", nullable = false)
    public int getReceiptLineId() {
        return receiptLineId;
    }

    public void setReceiptLineId(int receiptLineId) {
        this.receiptLineId = receiptLineId;
    }

    @Basic
    @Column(name = "prodName", nullable = true, length = 255)
    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Basic
    @Column(name = "price", nullable = true)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "amount", nullable = true)
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
        ReceiptlineEntity that = (ReceiptlineEntity) o;
        return receipId == that.receipId &&
                receiptLineId == that.receiptLineId &&
                Objects.equals(prodName, that.prodName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receipId, receiptLineId, prodName, price, amount);
    }
}
