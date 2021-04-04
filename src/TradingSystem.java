import javafx.util.Pair;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;

public class TradingSystem {

    private static counter userCounter;

    private PaymentAdapter paymentAdapter;
    private SupplementAdapter supplementAdapter;
    private  List<Store> stores;
    private Registered systemManager; //TODO change to whatever you want
    private  List<Receipt> receipts;
    private  List<User> users; //TODO every user is a thread
    private  HashMap<String,String> userPass;
    private Encryptor encryptor;



    public TradingSystem () {
        this.stores = new LinkedList<>();
        this.receipts = new LinkedList<>();
        this.systemManager =null;
        this.users = new LinkedList<>();
        this.userPass = new LinkedHashMap<>();
        this.encryptor = new Encryptor();
        this.userCounter = new counter();
    }


    public boolean register(String userName, String pass) {
        if(userPass.containsKey(userName))
        {
            return false;
        }
        else
        {
            userPass.put(userName,this.encryptor.encrypt(pass));
            KingLogger.logEvent(Level.INFO,"User "+userName+" register to the system");
            users.add(new User(userName,userCounter.inc(),1));
            return true;
        }
    }

    //if the user login successfully return his id. else return -1
    public int login(String userName,String pass) {
        if(loginAuthentication(userName,pass))
        {
            System.out.println("Login successfully!\n");
            KingLogger.logEvent(Level.INFO,"User "+userName+" enter the system\n");
            int id = -1;
            for(User user : users)
            {
                if(user.getUserName() == userName)
                {
                    return user.getId();
                }
            }
        }
        return -1;
    }

    private boolean loginAuthentication(String userName, String pass) {
        if(userPass.containsKey(userName))//write like this for the error log
        {
            if(userPass.get(userName).equals(encryptor.encrypt(pass)))
                return true;
            else
                KingLogger.logEvent(Level.INFO,"User try to login with name "+userName+" and pass "+pass+" and Failed");
        }
        else
        {
            KingLogger.logEvent(Level.INFO,"User try to login with name "+userName+" that doesn't exist");
        }
        return false;
    }

    public int guestLogin() {
        User guest=new User("Guest",userCounter.inc(),0);
        users.add(guest);
        return guest.getId();
    }
    public boolean isLogged(int userId)
    {
        User user =getUserById(userId);
        if(user != null)
            return user.isLooged();
        return false;
    }



    private String getPurchasesHistory(int userId) {
        return null;
    }



    private String storesInfo(int storeIndex) {
        //TODO print the stores names and let the user choose one
        if(storeIndex != -1) {
            Store s = getStoreByIndex(storeIndex);
            return (s.getStoreInfo());
        }
        else
            return null;
    }

    private Store getStoreByIndex(int index) {
        Store[] storesArr = (Store[]) stores.toArray();
        return storesArr[index-1];
    }

    public boolean guestRegister (int userId,String userName,String password){
        if(userPass.containsKey(userName))
        {
            return false;
        }
        else
        {
            userPass.put(userName,this.encryptor.encrypt(password));
            KingLogger.logEvent(Level.INFO,"User "+userName+" register to the system");
            getUserById(userId).setRegistered();
            return true;
        }
    }

    public boolean logout(int userId) {
        if(getUserById(userId) != null)
        {
            getUserById(userId).setLooged(false);
            return true;
        }
        return false;
    }

    private User getUserById(int userId)
    {
        for(User user : users)
        {
            if(user.getId() == userId)
                return user;
        }
        return null;
    }


    public int getNumOfUsers(){
        return users.size();
    }

    public String getAllStoresInfo() {
        String output = "";
        Store[] storesArr = (Store[]) stores.toArray();
        for (int i = 0; i < storesArr.length ; i ++){
            output += storesInfo(i);
        }
        return output;
    }
    //TODO return the store id, product id
    public Map<Integer,Integer> getProducts(String searchType, String param, String[] filter){
        Map<Integer,Integer> output = new HashMap<>();
        switch (searchType){
            case "NAME":
                for (Store s: stores) {
                    List<Integer> ps = s.getProductsByName(param);
                    for (int productId: ps) {
                        output.put(s.getStoreId(),productId);
                    }
                }
                break;
            case "CATEGORY":
                for (Store s: stores) {
                    List<Integer> ps = s.getProductsByCategory(param);
                    for (int productId: ps) {
                        output.put(s.getStoreId(),productId);
                    }
                }
                break;

            default:
                break;
        }

        return output;
    }

    public boolean saveProductInBug(int userId, int storeId){
        //TODO
        return false;
    }
    //TODO
    public List<Integer> getChart(int userId){

        return null;
    }
    //TODO
    public boolean addProductToChart(int userId,int storeId, int ProductId){

        return false;
    }
    //TODO
    public boolean removeProductFromChart(int userId,int storeId, int ProductId){

        return false;
    }
    //TODO
    public boolean buyProducts(int userId, int storeId, List<Integer> productsIds, String creditInfo){
        //TODO
        double totalCost = 0;
        List<Product> products = new LinkedList<>();
        for (int id: productsIds) {
            //Product p =
            //totalCost += p.getPrice();
        }
        for (Store s: stores) {
            if(true) { //change to s.getId() == storeId
                if(true) { //s.buy ...
                    boolean paid = paymentAdapter.pay(totalCost, creditInfo);
                    if(paid) {
                        // TODO add to log
                        return true;
                    }
                    return false;
                }
            }
        }
        //TODO add to log
        return false;
    }
    //TODO
    public boolean addProductToStore(int userId, int productId, int storeId ,String name, List<Product.Category> categories,double price, String description, int quantity){
        Store store = getStoreById(storeId);
        return store.addProductToStore(getUserById(userId),productId, name, categories, price, description,quantity);
    }
    //TODO
    public boolean removeProductFromStore(int userId,int storeId, int productId){

        return false;
    }

    //returns the new store id
    public int openStore(int userId, String storeName){
        //TODO
        return -1;
    }

    public boolean addStoreOwner(int ownerId, int userId,int storeId){
        return getStoreById(storeId).getPermissions().appointOwner(ownerId,userId);
    }

    public boolean addStoreManager(int ownerId, int userId, int storeId){
        return getStoreById(storeId).getPermissions().appointManager(ownerId,userId);
    }

    public boolean addPermissions(int ownerId, int managerId, int storeId, List<Integer> permissions){
        return getStoreById(storeId).getPermissions().addPermissions(ownerId,storeId,permissions);
    }

    public boolean removePermissions(int ownerId, int managerId, int storeId,List<Integer> permissions){
        return getStoreById(storeId).getPermissions().addPermissions(ownerId,storeId,permissions);
    }

    public boolean removeManager(int ownerId, int managerId, int storeId){
        //TODO
        return false;
    }

    public String getWorkersInformation(int ownerId, int storeId){
        //TODO
        return null;
    }

    public String getStorePurchaseHistory(int ownerId, int storeId){
        //TODO
        return null;
    }

    public boolean doSomething(int mangaerId, int storeId, Permissions permission){
        //TODO
        return false;
    }

    public String getAllPurchases(int systemManager){
        //TODO
        return null;
    }

    public Store getStoreById(int storeId)
    {
        for(Store store : stores)
        {
            if(store.getStoreId() == storeId)
                return store;
        }
        return null;
    }


    public List<Integer> getProductsFromStore(int storeId1) {
        //TODO
        return null;

    }
}
