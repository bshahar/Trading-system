package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LeftToApprove")
public class LeftToApproveDAO {

    @DatabaseField(uniqueCombo = true)
    private int offerId;
    @DatabaseField(uniqueCombo = true)
    private int userId;

    public LeftToApproveDAO() {
        // ORMLite needs a no-arg constructor
    }


    public LeftToApproveDAO(int offerId, int userId) {
        this.offerId = offerId;
        this.userId = userId;
    }


    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
