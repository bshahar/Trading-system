import java.util.*;

public class TradingSystem {

    private PaymentAdapter paymentAdapter;
    private SupplementAdapter supplementAdapter;
    private List<Store> stores;
    private Registered systemManager; //TODO change to whatever you want
    private List<Receipt> receipts;

    private void initializeSystem(Registered systemManager) {
        this.stores = new LinkedList<>();
        this.receipts = new LinkedList<>();
        this.systemManager = systemManager;
    }

    public static void main(String[] args) {
        System.out.println("FooBar123!");
        //initializeSystem(); TODO add Manager somehow

    }
}
