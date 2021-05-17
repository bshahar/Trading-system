package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Receipts", schema = "zw9P3SlfWt", catalog = "")
public class ReceiptsEntity {
    private int id;
    private Integer storeId;
    private Integer userId;
    private String userName;
    private Double totalCost;
    private Integer paymentTransactionId;
    private Integer supplementTransactionId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "storeId")
    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "userId")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "totalCost")
    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @Basic
    @Column(name = "paymentTransactionId")
    public Integer getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Integer paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    @Basic
    @Column(name = "supplementTransactionId")
    public Integer getSupplementTransactionId() {
        return supplementTransactionId;
    }

    public void setSupplementTransactionId(Integer supplementTransactionId) {
        this.supplementTransactionId = supplementTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptsEntity that = (ReceiptsEntity) o;
        return id == that.id && Objects.equals(storeId, that.storeId) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(totalCost, that.totalCost) && Objects.equals(paymentTransactionId, that.paymentTransactionId) && Objects.equals(supplementTransactionId, that.supplementTransactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeId, userId, userName, totalCost, paymentTransactionId, supplementTransactionId);
    }
}
