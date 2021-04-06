import javafx.util.Pair;

import java.util.*;

public class Receipt {

    private int storeId;
    private String userName; //Unique
    private Map<Pair<String, Double>, Integer> purchases;

    public Receipt(int storeId, String userName, Map<Pair<String, Double>, Integer> purchases) {
        this.storeId = storeId;
        this.userName = userName;
        this.purchases = purchases;
    }
}
