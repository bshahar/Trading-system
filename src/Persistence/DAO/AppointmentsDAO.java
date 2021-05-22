package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Appointments")
public class AppointmentsDAO {

    public AppointmentsDAO(){    }

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
    private int managerId;
    @DatabaseField(id = true)
    private int appointedId;

    public AppointmentsDAO(int storeId, int managerId, int appointedId) {
        this.storeId = storeId;
        this.managerId = managerId;
        this.appointedId = appointedId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(int appointedId) {
        this.appointedId = appointedId;
    }
}
