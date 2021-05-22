package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "Products")
public class ProductDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private double price;
    @DatabaseField
    private double rate;
    @DatabaseField
    private int ratesCount;
    @DatabaseField
    private String description;
    @DatabaseField
    private int amount;
    @DatabaseField
    private int storeId;


    public ProductDAO(int id, String name, double price, double rate, int ratesCount, String description, int amount, int storeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rate = rate;
        this.ratesCount = ratesCount;
        this.description = description;
        this.amount = amount;
        this.storeId = storeId;
    }

    public ProductDAO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(int ratesCount) {
        this.ratesCount = ratesCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
