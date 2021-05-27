package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreEmployees")
public class StoreEmployeesDAO {

    public StoreEmployeesDAO(){    }

    public StoreEmployeesDAO(int storeId, int userId) {
        this.storeId = storeId;
        this.userId = userId;
    }

    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private int userId;


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



}
