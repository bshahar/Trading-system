package Domain;

import java.util.*;

public class Receipt {

    public class ReceiptLine{

        String ProdName;
        double price;
        int amount;
        public ReceiptLine(String Prod, double price, int amount){
            this.ProdName= Prod;
            this.price=price;
            this.amount=amount;
        }

        public String getProdName() {
            return ProdName;
        }

        public double getPrice() {
            return price;
        }

        public int getAmount() {
            return amount;
        }
    }
    private int storeId;

    private int userId;
    private String userName; //Unique
    private List<ReceiptLine> lines;

    public Receipt(int storeId,int userId, String userName, Map<Product, Integer> lines) {
        this.storeId = storeId;
        this.userId= userId;
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
    public int getUserId(){return userId;}

    public List<ReceiptLine> getLines() {
        return lines;

    }



}
