package Domain;

import java.util.*;

public class Store {

    private int storeId;
    private String name;
    private Inventory inventory;
    private List<Policy> policies;
    private List<Format> formats;
    private List<User> employees;
    private List<User> owners;
    private List<User> managers;
    private List<Receipt> receipts;

    private Map<User,List<User>> appointments;
    private double rate;
    private int ratesCount;


    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = Collections.synchronizedList(new LinkedList<>());;
        this.employees.add(owner);
        this.receipts = new LinkedList<>();
        this.appointments= new HashMap<>();
        appointments.put(owner,new LinkedList<>());
        this.owners = Collections.synchronizedList(new LinkedList<>());;
        this.managers = Collections.synchronizedList(new LinkedList<>());;
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



    public boolean addProductToStore(int productId,  String name, List<Product.Category> categories, double price, String description, int quantity) {
        Product p = new Product(productId, name, categories, price, description);
        return this.inventory.addProduct(p, quantity);
    }

    public boolean removeProductFromStore( int productId) {
        synchronized (inventory){
            if(this.inventory.prodExists(productId)){
                return this.inventory.removeProduct(productId);
            }
            else{
                return false;
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


    public boolean removeManager(User owner, User manager) {
        if(appointments.get(owner).remove(manager)){
            employees.remove(manager);
            if(appointments.containsKey(manager)){
                List<User> managers=appointments.get(manager);
                for(User user : managers){
                    this.managers.remove(user);
                    removeManager(manager,user);
                }

            }
            return true;
        }
        return false;
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

    public void addOwnerToAppointments(User owner, User user) {
        appointments.get(owner).add(user);
        appointments.put(user,new LinkedList<>());
    }
    public void addReceipt(Receipt receipt)
    {
        this.receipts.add(receipt);
    }

    public boolean addOwner(User user)
    {
        if(this.owners.contains(user))
            return false;
        owners.add(user);
        return true;
    }
    public boolean addManager(User user)
    {
        if(this.managers.contains(user))
            return false;
        managers.add(user);
        return true;
    }


}
