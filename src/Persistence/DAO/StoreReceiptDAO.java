package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreReceipts")
public class StoreReceiptDAO {

    public StoreReceiptDAO(){    }

    public StoreReceiptDAO(int storeId, int receiptId) {
        this.storeId = storeId;
        this.receiptId = receiptId;
    }

    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private int receiptId;


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }



}
