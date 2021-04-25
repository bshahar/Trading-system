package Domain;

import Domain.DiscountFormat.*;
import Domain.DiscountPolicies.DiscountCondition;
import Service.counter;

import java.util.*;

public class Store {

    private int storeId;
    private String name;
    private Inventory inventory;
    private List<Format> formats;
    private List<User> employees;
    private List<User> owners;
    private List<User> managers;
    private List<Receipt> receipts;
    private Service.counter counter;
    private Map<Product, ConditionalDiscount> discountsOnProducts;
    private Map<Product.Category, ConditionalDiscount> discountsOnCategories;
    private List<ConditionalDiscount> discountsOnStore;
    private Map<User,List<User>> appointments; //appointer & list of appointees
    private Map<Integer, Bag> usersBags;
    private double rate;
    private int ratesCount;


    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = Collections.synchronizedList(new LinkedList<>());
        this.employees.add(owner);
        this.receipts = new LinkedList<>();
        this.appointments= new HashMap<>();
        appointments.put(owner,new LinkedList<>());
        this.owners = Collections.synchronizedList(new LinkedList<>());
        this.managers = Collections.synchronizedList(new LinkedList<>());
        this.discountsOnProducts = new HashMap<>();
        this.discountsOnCategories = new HashMap<>();
        this.discountsOnStore = new LinkedList<>();
        this.counter = new counter();
        this.usersBags = new HashMap<>();
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

    public String getName() {
        return name;
    }

    private boolean validateProductId(int id){
        return this.inventory.validateProductId(id);
    }

    public int getStoreId() {
        return storeId;
    }

    public boolean addProductToStore(int productId,  String name, List<Product.Category> categories, double price, String description, int quantity) {
        Product p = new Product(productId, name, categories, price, description);
        return this.inventory.addProduct(p, quantity);
    }

    public Result removeProductFromStore( int productId) {
        synchronized (inventory){
            if(this.inventory.prodExists(productId)){
                return this.inventory.removeProduct(productId);
            }
            else{
                return new Result(false,"Product not exist");
            }

        }
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


    public Result removeManager(User owner, User manager) {
        if(appointments.get(owner).remove(manager)){
            employees.remove(manager);
            if(appointments.containsKey(manager)){
                List<User> managers=appointments.get(manager);
                for(User user : managers){
                    this.managers.remove(user);
                    removeManager(manager,user);
                }

            }
            return new Result(true,true);
        }
        return new Result(false,"Remove of the manager has failed");
    }

    public List<User> getWorkersInformation(int ownerId) {
        return this.employees;
    }

    public boolean getStorePurchaseHistory(int ownerId) {
        return true;
    }//TODO

    public Product getProductById(int id) {
        return inventory.getProductById(id);
    }

    public boolean canBuyProduct(Product product, int amount) {
        return inventory.canBuyProduct(product,amount);
    }

    public void removeProductAmount(Product product, Integer amount) {
        inventory.removeProductAmount(product,amount);
    }

    public void addEmployee(User owner,User user) {
        this.employees.add(user);
        this.appointments.get(owner).add(user);
    }
    public Result getEmployees()
    {
        return new Result(true,this.employees);
    }

    public Result getPurchaseHistory() {
        return new Result(true,this.receipts);
    }

    public void addOwnerToAppointments( User user) {
        appointments.put(user,new LinkedList<>());
    }

    public void addReceipt(Receipt receipt)
    {
        this.receipts.add(receipt);
    }

    public boolean addOwner(User user) {
        if(this.owners.contains(user))
            return false;
        owners.add(user);
        return true;
    }
    public boolean addManager(User user)
    {
        synchronized (managers) {
            if (this.managers.contains(user))
                return false;
            managers.add(user);
            return true;
        }
    }

    public void abortPurchase(Map<Product, Integer> productsAmount) {
        for(Product product : productsAmount.keySet()){
            synchronized (product){
                this.inventory.addProductAmount(product,productsAmount.get(product));
            }
        }
    }

    public void addDiscountOnProduct(int prodId, Date begin, Date end, DiscountCondition conditions, int percentage) {
        this.discountsOnProducts.put(getProductById(prodId), new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage));
    }

    public void addDiscountOnCategory(Product.Category category, Date begin, Date end, DiscountCondition conditions, int percentage) {
        this.discountsOnCategories.put(category, new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage));
    }

    public void addDiscountOnStore(Date begin, Date end, DiscountCondition conditions, int percentage) {
        this.discountsOnStore.add(new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage));
    }

    public double calculateDiscounts(double totalCost, int userId) {
        Bag bag = this.usersBags.get(userId);
        if (this.discountsOnStore.size() > 0)
            totalCost = calcStoreDiscount(totalCost, userId, bag);
        if (this.discountsOnCategories.size() > 0)
            totalCost = calcCategoriesDiscount(totalCost, userId, bag);
        if (this.discountsOnProducts.size() > 0)
            totalCost = calcProductsDiscount(totalCost, userId, bag);
        return totalCost;
    }

    private double calcStoreDiscount(double totalCost, int userId, Bag bag) {
        double discount = 0;
        for (ConditionalDiscount disCon: this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, userId, new Date(), bag);
        }
        return totalCost - discount;
    }

    private double calcCategoriesDiscount(double totalCost, int userId, Bag bag) {
        double discount = 0;
        Bag discountProds = new Bag(this);
        for (Product prod: bag.getProducts()) {
            for (Product.Category category: prod.getCategories()) {
                if (this.discountsOnCategories.containsKey(category))
                    discountProds.addProduct(prod, bag.getProductsAmounts().get(prod));
            }
        }
        for (ConditionalDiscount disCon : this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, userId, new Date(), discountProds);
        }
        return totalCost - discount;
    }

    private double calcProductsDiscount(double totalCost, int userId, Bag bag) {
        double discount = 0;
        Bag discountProds = new Bag(this);
        for (Product prod : bag.getProducts()) {
            if (this.discountsOnProducts.containsKey(prod)) {
                discountProds.addProduct(prod, bag.getProductsAmounts().get(prod));
            }
        }
        for (ConditionalDiscount disCon : this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, userId, new Date(), bag);
        }
        return totalCost - discount;
    }


}
