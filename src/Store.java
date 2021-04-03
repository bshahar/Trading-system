import java.util.*;

public class Store {

    private int storeId;
    private String name;
    private Inventory inventory;
    private Permissions permissions;

    private double rate;
    private int ratesCount;
    //private List<Bag> shoppingBags;

    public Store(int id, String name, User owner, Map<Product,Integer>products) { //create a store with initial inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.permissions = new Permissions(owner);
        this.inventory = new Inventory(products);
        this.rate = 0;
        this.ratesCount = 0;
    }

    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.permissions = new Permissions(owner);
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public double getRate() {
        return rate;
    }

    public Set<User> getBosses() {
        return this.permissions.getBosses();
    }

    public boolean addToInventory(User currUser, Product prod, int numOfProd) {
        return this.inventory.addProduct(prod , numOfProd);
    }

    private boolean validatePermission(User user, Permissions.Operations operation) {
        return this.permissions.validatePermission(user, operation);
    }

    public boolean addBoss(User appointer, User appointee, int role) { //role = 1 -> owner, role  = 2 -> manager
        if(role == 1)
            return this.permissions.appointOwner(appointer.getId(), appointee.getId());
        return this.permissions.appointManager(appointer.getId(), appointee.getId());
    }

    public void rateStore(double newRate) {
        this.ratesCount ++;
        this.rate = (this.rate + newRate) / this.ratesCount;
    }

    public String getStoreInfo() {
        String str = "";
        str = str + "Store name - " + name + " The products in this store - " + getInventory().toString();
        return str;
    }

    private boolean validateProductId(int id){
        return this.inventory.validateProductId(id);
    }

    public int getStoreId() {
        return storeId;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public boolean addProductToStore(User user, int productId,  String name, List<Product.Category> categories, double price, String description, int quantity) {
        if( this.permissions.validatePermission(user, Permissions.Operations.AddProduct)){
            if(validateProductId(productId)){
                Product p = new Product(productId, name, categories, price, description);
                return this.inventory.addProduct(p, quantity);
            }
        }
        return false; // TODO add logger
    }

    public List<Integer> getProductsByName(String name){
         return this.inventory.getProductsByName(name);
    }

    public List<Integer> getProductsByCategory(String category) {
        return this.inventory.getProductsByCategory(category);
    }

    public List<Integer> getProductsByKeyWords(String[] filter) {
        return this.inventory.getProductsByKeyWords(filter);
    }

    public List<Integer> getProductsByPriceRange(String[] filter) {
        return this.inventory.getProductsByPriceRange(filter);
    }
}
