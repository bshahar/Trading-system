package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StoreReceiptsEntityPK implements Serializable {
    private int storeId;
    private int receiptId;

    @Column(name = "storeId")
    @Id
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Column(name = "receiptId")
    @Id
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreReceiptsEntityPK that = (StoreReceiptsEntityPK) o;
        return storeId == that.storeId && receiptId == that.receiptId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, receiptId);
    }
}
