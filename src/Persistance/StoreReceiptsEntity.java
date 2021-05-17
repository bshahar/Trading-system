package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StoreReceipts", schema = "zw9P3SlfWt", catalog = "")
@IdClass(StoreReceiptsEntityPK.class)
public class StoreReceiptsEntity {
    private int storeId;
    private int receiptId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Id
    @Column(name = "receiptId")
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
        StoreReceiptsEntity that = (StoreReceiptsEntity) o;
        return storeId == that.storeId && receiptId == that.receiptId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, receiptId);
    }
}
