package Domain;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "Receipts", schema = "zw9P3SlfWt", catalog = "")
public class Receipt {

    private int id;
    private int storeId;
    private int userId;
    private String userName; //Unique
    private List<ReceiptLine> lines;
    private double totalCost;
    private int paymentTransactionId;
    private int supplementTransactionId;

    public Receipt(){}

    public Receipt(int id, int storeId, int userId, String userName, Map<Product, Integer> lines, int paymentTransaction, int supplementTransaction) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.paymentTransactionId = paymentTransaction;
        this.supplementTransactionId = supplementTransaction;
        this.lines = new LinkedList<>();
        for (Product p : lines.keySet()) {
            this.lines.add(new ReceiptLine(p.getName(), p.getPrice(), lines.get(p)));
        }
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "storeId")
    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "userId")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "totalCost")
    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @Basic
    @Column(name = "paymentTransactionId")
    public Integer getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Integer paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    @Basic
    @Column(name = "supplementTransactionId")
    public Integer getSupplementTransactionId() {
        return supplementTransactionId;
    }

    public void setSupplementTransactionId(Integer supplementTransactionId) {
        this.supplementTransactionId = supplementTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt that = (Receipt) o;
        return id == that.id && Objects.equals(storeId, that.storeId) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(totalCost, that.totalCost) && Objects.equals(paymentTransactionId, that.paymentTransactionId) && Objects.equals(supplementTransactionId, that.supplementTransactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeId, userId, userName, totalCost, paymentTransactionId, supplementTransactionId);
    }

    @Transient
    public List<ReceiptLine> getLines() {
        return lines;
    }
    public void setLines(List<ReceiptLine> lines){
        this.lines = lines;
    }

}
