package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Discounts")
public class DiscountDAO {
    @DatabaseField
    private String mathOperator;
    @DatabaseField
    private String beginDate;
    @DatabaseField
    private String endDate;
    @DatabaseField
    private int percentage;
    @DatabaseField(id = true)
    private int id;

    public DiscountDAO() {}

    public DiscountDAO(int id, String mathOperator, String beginDate, String endDate, int percentage) {
        this.id = id;
        this.mathOperator = mathOperator;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.percentage = percentage;
    }


    public String getMathOperator() {
        return mathOperator;
    }
    public void setMathOperator(String mathOperator) {
        this.mathOperator = mathOperator;
    }

    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
