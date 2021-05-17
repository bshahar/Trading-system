package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StorePoliciesOnStores", schema = "zw9P3SlfWt", catalog = "")
public class StorePoliciesOnStoresEntity {
    private int storeId;
    private Integer immediateId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "immediateId")
    public Integer getImmediateId() {
        return immediateId;
    }

    public void setImmediateId(Integer immediateId) {
        this.immediateId = immediateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorePoliciesOnStoresEntity that = (StorePoliciesOnStoresEntity) o;
        return storeId == that.storeId && Objects.equals(immediateId, that.immediateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, immediateId);
    }
}
