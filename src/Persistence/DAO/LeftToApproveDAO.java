package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class LeftToApproveDAO {

    public PurchaseOffersDAO getPo() {
        return po;
    }

    public void setPo(PurchaseOffersDAO po) {
        this.po = po;
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private PurchaseOffersDAO po;
   /* @DatabaseField(uniqueCombo = true)
    private int offerId;*/
    @DatabaseField(uniqueCombo = true)
    private int userId;

    public LeftToApproveDAO() {
        // ORMLite needs a no-arg constructor
    }


    public LeftToApproveDAO( int userId) {
        //this.offerId = offerId;
        this.userId = userId;
    }


    /*public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }*/

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
