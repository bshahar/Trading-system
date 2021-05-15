package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "receipt", schema = "zw9P3SlfWt", catalog = "")
public class ReceiptEntity {
    private int id;
    private Integer storeId;
    private Integer userId;
    private String userName;
    private Integer totalCost;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "storeId", nullable = true)
    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "userId", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userName", nullable = true, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "totalCost", nullable = true)
    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptEntity that = (ReceiptEntity) o;
        return id == that.id &&
                Objects.equals(storeId, that.storeId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(totalCost, that.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeId, userId, userName, totalCost);
    }
}
