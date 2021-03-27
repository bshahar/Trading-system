public class Receipt {

    private int storeId;
    private String userName; //Unique
    private double totalPrice;
    //TODO - do we want information about the products here? Ids or more than that?

    public Receipt(int storeId, String userName, double totalPrice) {
        this.storeId = storeId;
        this.userName = userName;
        this.totalPrice = totalPrice;
    }
}
