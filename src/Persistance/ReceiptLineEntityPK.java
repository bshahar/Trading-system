package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ReceiptLineEntityPK implements Serializable {
    private int receiptId;
    private int receiptLineId;

    @Column(name = "receiptId", nullable = false)
    @Id
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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
        ReceiptLineEntityPK that = (ReceiptLineEntityPK) o;
        return receiptId == that.receiptId &&
                receiptLineId == that.receiptLineId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptId, receiptLineId);
    }
}
