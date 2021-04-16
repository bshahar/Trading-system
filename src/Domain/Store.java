package Domain;

import java.util.*;

public class Store {

    private int storeId;
    private String name;
    private Inventory inventory;
    private List<Policy> policies;
    private List<Format> formats;
    private List<User> employees;
    private List<Receipt> receipts;

    private double rate;
    private int ratesCount;
    //private List<Domain.Bag> shoppingBags;

    public Store(int id, String name, User owner, Map<Product, Integer> products) { //create a store with initial inventory
        this.storeId = id;
        this.name = name;
        this.employees = new LinkedList<>();
        this.employees.add(owner);
        //this.shoppingBags = new LinkedList<>();
        this.inventory = new Inventory(products);
        this.rate = 0;
        this.ratesCount = 0;
        this.receipts = new LinkedList<>();
    }

    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = new LinkedList<>();
        this.employees.add(owner);
        this.receipts = new LinkedList<>();
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



    private boolean validateProductId(int id){
        return this.inventory.validateProductId(id);
    }

    public int getStoreId() {
        return storeId;
    }


    public boolean addProductToStore(User user, int productId,  String name, List<Product.Category> categories, double price, String description, int quantity) {

        return false;
    }

    public boolean removeProductFromStore(User user, int productId) {

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


    public List<User> getWorkersInformation(int ownerId) {
        return this.employees;
    }

    public boolean getStorePurchaseHistory(int ownerId) {
        return true;
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

    public void addEmployee(User user) {
        this.employees.add(user);
    }
    public List<User> getEmployees()
    {
        return this.employees;
    }

    public List<Receipt> getPurchaseHistory() {
        return this.receipts;
    }
}
