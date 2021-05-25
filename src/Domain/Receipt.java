package Domain;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
            this.lines.add(new ReceiptLine(p.getName(), p.getPrice(), lines.get(p),id,storeId));
        }
    }

    public Receipt(int id, int storeId, int userId, String userName, int paymentTransaction, int supplementTransaction) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.paymentTransactionId = paymentTransaction;
        this.supplementTransactionId = supplementTransaction;
        this.lines = new LinkedList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }


    public Integer getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Integer paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }


    public Integer getSupplementTransactionId() {
        return supplementTransactionId;
    }

    public void setSupplementTransactionId(Integer supplementTransactionId) {
        this.supplementTransactionId = supplementTransactionId;
    }




    public List<ReceiptLine> getLines() {
        return lines;
    }
    public void setLines(List<ReceiptLine> lines){
        this.lines = lines;
    }

}
