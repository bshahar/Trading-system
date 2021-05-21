package Domain;
import java.util.Objects;


public class ReceiptLine {

    String prodName;
    double price;
    int amount;

    public ReceiptLine(){}

    public ReceiptLine(String Prod, double price, int amount) {
        this.prodName = Prod;
        this.price = price;
        this.amount = amount;
    }


    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptLine that = (ReceiptLine) o;
        return Objects.equals(prodName, that.prodName) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount);
    }


    public int hashCode() {
        return Objects.hash(prodName, price, amount);
    }
}
