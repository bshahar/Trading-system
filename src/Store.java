import java.util.*;

public class Store {

    private int id;
    private Inventory inventory;
    private List<Registered> bosses; //Owners & managers //TODO type Register/User? Move to Permissions?
    //private List<Bag> shoppingBags;

    public Store(int id, Registered owner) {
        this.id = id;
        this.bosses = new LinkedList<>();
        this.bosses.add(owner);
        //this.shoppingBags = new LinkedList<>();
        //TODO init inventory?
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Registered> getBosses() {
        return bosses;
    }

    public boolean addToInventory(Product prod) {
        return this.inventory.addProduct(prod);
    }

    public void addBoss(Registered boss) {
        this.bosses.add(boss);
    }

}
