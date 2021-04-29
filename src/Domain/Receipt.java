package Domain;

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

    private int id;
    private int storeId;
    private int userId;
    private String userName; //Unique
    private List<ReceiptLine> lines;
    private double totalCost;



    public Receipt(int id, int storeId,int userId, String userName, Map<Product, Integer> lines) {
        this.id = id;
        this.storeId = storeId;
        this.userId= userId;
        this.userName = userName;
        this.lines= new LinkedList<ReceiptLine>();
        for (Product p: lines.keySet()) {
            this.lines.add(new ReceiptLine(p.getName(), p.getPrice(),lines.get(p)));
        }
    }

    public int getReceiptId() { return this.id; }

    public int getStoreId() {
        return storeId;
    }

    public String getUserName(){return userName;}

    public int getUserId(){return userId;}

    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public double getTotalCost() { return this.totalCost; }



}
