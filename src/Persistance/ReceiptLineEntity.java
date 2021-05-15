package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "receiptLine", schema = "zw9P3SlfWt", catalog = "")
@IdClass(ReceiptLineEntityPK.class)
public class ReceiptLineEntity {
    private int receiptId;
    private int receiptLineId;
    private String prodName;
    private Integer price;
    private Integer amount;

    @Id
    @Column(name = "receiptId", nullable = false)
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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
        ReceiptLineEntity that = (ReceiptLineEntity) o;
        return receiptId == that.receiptId &&
                receiptLineId == that.receiptLineId &&
                Objects.equals(prodName, that.prodName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptId, receiptLineId, prodName, price, amount);
    }
}
