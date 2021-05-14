package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ReceiptlineEntityPK implements Serializable {
    private int receipId;
    private int receiptLineId;

    @Column(name = "receipId", nullable = false)
    @Id
    public int getReceipId() {
        return receipId;
    }

    public void setReceipId(int receipId) {
        this.receipId = receipId;
    }

    @Column(name = "receiptLineId", nullable = false)
    @Id
    public int getReceiptLineId() {
        return receiptLineId;
    }

    public void setReceiptLineId(int receiptLineId) {
        this.receiptLineId = receiptLineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptlineEntityPK that = (ReceiptlineEntityPK) o;
        return receipId == that.receipId &&
                receiptLineId == that.receiptLineId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(receipId, receiptLineId);
    }
}
