package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StoreEmployees", schema = "zw9P3SlfWt", catalog = "")
public class StoreEmployeesEntity {
    private int storeId;
    private int userId;

    @Basic
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Id
    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreEmployeesEntity that = (StoreEmployeesEntity) o;
        return storeId == that.storeId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, userId);
    }
}
