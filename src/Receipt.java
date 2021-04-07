import java.util.*;

public class Receipt {

    private class ReceiptLine{
        String ProdName;
        double price;
        int amount;

        public ReceiptLine(String Prod, double price, int amount){
            this.ProdName= Prod;
            this.price=price;
            this.amount=amount;
        }
    }

    private int storeId;
    private String userName; //Unique
    private List<ReceiptLine> lines;


    public Receipt(int storeId, String userName, Map<Product, Integer> lines) {
        this.storeId = storeId;
        this.userName = userName;
        this.lines= new LinkedList<ReceiptLine>();
        for (Product p: lines.keySet()) {
            this.lines.add(new ReceiptLine(p.getName(), p.getPrice(),lines.get(p)));
        }
    }

    public int getStoreId() {
        return storeId;
    }
    public String getUserName(){return userName;}



}
