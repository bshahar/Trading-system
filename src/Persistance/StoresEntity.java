package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Stores", schema = "zw9P3SlfWt", catalog = "")
public class StoresEntity {
    private int storeId;
    private Integer notificationId;
    private String name;
    private Double rate;
    private Integer rateCount;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "notificationId")
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "rate")
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Basic
    @Column(name = "rateCount")
    public Integer getRateCount() {
        return rateCount;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoresEntity that = (StoresEntity) o;
        return storeId == that.storeId && Objects.equals(notificationId, that.notificationId) && Objects.equals(name, that.name) && Objects.equals(rate, that.rate) && Objects.equals(rateCount, that.rateCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, notificationId, name, rate, rateCount);
    }
}
