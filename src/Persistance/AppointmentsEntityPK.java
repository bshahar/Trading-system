package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AppointmentsEntityPK implements Serializable {
    private int storeId;
    private int managerId;
    private int appointedId;

    @Column(name = "storeId")
    @Id
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Column(name = "managerId")
    @Id
    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Column(name = "appointedId")
    @Id
    public int getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(int appointedId) {
        this.appointedId = appointedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentsEntityPK that = (AppointmentsEntityPK) o;
        return storeId == that.storeId && managerId == that.managerId && appointedId == that.appointedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, managerId, appointedId);
    }
}
