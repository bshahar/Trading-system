package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ReceiptLines")
public class ReceiptLinesDAO {

    @DatabaseField(id = true)
    private int receiptId;
    @DatabaseField(id = true)
    private int productId;
    @DatabaseField
    private String prodName;
    @DatabaseField
    private double price;
    @DatabaseField
    private int amount;

    public ReceiptLinesDAO() {
        // ORMLite needs a no-arg constructor
    }

    public ReceiptLinesDAO(int receiptId, int productId, String prodName, double price, int amount) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.prodName = prodName;
        this.price = price;
        this.amount = amount;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProdName() {
        return prodName;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
