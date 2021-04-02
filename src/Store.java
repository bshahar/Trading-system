import java.util.*;

public class Store {

    private int storeId;
    private Inventory inventory;
    //private List<Registered> bosses; //Owners & managers //TODO type Register/User? Move to Permissions?
    private Permissions permissions;
    //private List<Bag> shoppingBags;

    public Store(int id, User owner, List<Product>products) { //create a store with initial inventory
        this.storeId = id;
        //this.bosses = new LinkedList<>();
        //this.bosses.add(owner);
        //this.shoppingBags = new LinkedList<>();
        this.permissions = new Permissions(owner);
        this.inventory = new Inventory(products);
    }

    public Store(int id, User owner) { //create a store with empty inventory
        this.storeId = id;
        //this.bosses = new LinkedList<>();
        //this.bosses.add(owner);
        //this.shoppingBags = new LinkedList<>();
        this.permissions = new Permissions(owner);
        this.inventory = new Inventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Set<User> getBosses() {
        return this.permissions.getBosses();
    }

    public boolean addToInventory(User currUser, Product prod) {
        return this.inventory.addProduct(prod);
    }

    private boolean validatePermission(User user, Permissions.Operations operation) {
        return this.permissions.validatePermission(user, operation);
    }

    public boolean addBoss(User appointer, User appointee, int role) { //role = 1 -> owner, role  = 2 -> manager
        if(role == 1)
            return this.permissions.appointOwner(appointer.getId(), appointee.getId());
        return this.permissions.appointManager(appointer.getId(), appointee.getId());
    }

    public String getStoreInfo() {
        String str = "";
        return "";
    }
}
