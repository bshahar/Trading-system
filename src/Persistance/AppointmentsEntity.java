package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Appointments", schema = "zw9P3SlfWt", catalog = "")
@IdClass(AppointmentsEntityPK.class)
public class AppointmentsEntity {
    private int storeId;
    private int managerId;
    private int appointedId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Id
    @Column(name = "managerId")
    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Id
    @Column(name = "appointedId")
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
        AppointmentsEntity that = (AppointmentsEntity) o;
        return storeId == that.storeId && managerId == that.managerId && appointedId == that.appointedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, managerId, appointedId);
    }
}
