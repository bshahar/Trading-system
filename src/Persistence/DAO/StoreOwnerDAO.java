package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreOwners")
public class StoreOwnerDAO {

    public StoreOwnerDAO(){    }

    public StoreOwnerDAO(int storeId, int userId) {
        this.storeId = storeId;
        this.userId = userId;
    }

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
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
