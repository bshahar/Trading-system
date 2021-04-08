package Domain;

import java.util.*;

public class Store {

    private int storeId;
    private String name;
    private Inventory inventory;
    private Permissions permissions;

    private double rate;
    private int ratesCount;
    //private List<Domain.Bag> shoppingBags;

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


    public boolean addToInventory(User currUser, Product prod, int numOfProd) {
        return this.inventory.addProduct(prod , numOfProd);
    }

    private boolean validatePermission(User user, Permissions.Operations operation) {
        return this.permissions.validatePermission(user, operation);
    }

    public boolean addBoss(User appointer, User appointee, int role) { //role = 1 -> owner, role  = 2 -> manager
        if(role == 1)
            return this.permissions.appointOwner(appointer.getId(), appointee);
        return this.permissions.appointManager(appointer.getId(), appointee);
    }

    public void rateStore(double newRate) {
        this.ratesCount ++;
        this.rate = (this.rate + newRate) / this.ratesCount;
    }

    public String getStoreInfo() {
        String str = "";
        str = str + "Domain.Store name - " + name + " The products in this store - " + getInventory().toString();
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
        return false;
    }

    public boolean removeProductFromStore(User user, int productId) {
        if( this.permissions.validatePermission(user, Permissions.Operations.RemoveProduct)){
            if(this.inventory.prodExists(productId)){
                return this.inventory.removeProduct(productId);
            }
        }
        return false;
    }

    public List<Integer> getProductsByName(Filter filter){
         return this.inventory.getProductsByName(filter,this.rate);
    }

    public List<Integer> getProductsByCategory(Filter filter) {
        return this.inventory.getProductsByCategory(filter,this.rate);
    }

    public List<Integer> getProductsByKeyWords(Filter filter) {

        return this.inventory.getProductsByKeyWords(filter, this.rate);
    }

    public List<Integer> getProductsByPriceRange(String[] filter) {
        return this.inventory.getProductsByPriceRange(filter);
    }
    public boolean addPermissions(int ownerId, int managerId, List<Integer> opIndexes) {
        return this.permissions.addPermissions(ownerId,managerId,opIndexes);
    }

    public boolean removePermissions(int ownerId, int managerId, List<Integer> opIndexes) {
        return this.permissions.removePermissions(ownerId,managerId,opIndexes);
    }

    public boolean removeAppointment(int ownerId, int managerId) {
        return this.permissions.removeAppointment(ownerId,managerId);
    }

    public List<User> getWorkersInformation(int ownerId) {
        return this.permissions.getWorkersInformation(ownerId);
    }

    public boolean getStorePurchaseHistory(int ownerId) {
        return this.permissions.getStorePurchaseHistory(ownerId);
    }

    public Product getProductById(int id) {
        return inventory.getProductById(id);
    }

    public boolean canBuyProduct(int id,int amount) {
        return inventory.canBuyProduct(getProductById(id),amount);
    }

    public void buyProduct(Integer prodId, Integer amount) {
        inventory.buyProduct(getProductById(prodId),amount);
    }
}
