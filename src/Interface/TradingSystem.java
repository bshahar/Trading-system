package Interface;

import Domain.*;
import Service.*;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class TradingSystem {

    private static counter userCounter;
    private static counter storeCounter;
    private static counter productCounter;

    private PaymentAdapter paymentAdapter;
    private SupplementAdapter supplementAdapter;
    private UserAuth userAuth;
    private List<Store> stores;
    private User systemManager;
    private List<Receipt> receipts;
    private List<User> users;



    public TradingSystem (User systemManager) {
        this.paymentAdapter= new PaymentAdapter(new DemoPayment());
        this.stores = Collections.synchronizedList(new LinkedList<>());
        this.receipts =Collections.synchronizedList( new LinkedList<>());
        this.systemManager =systemManager;
        this.users = Collections.synchronizedList(new LinkedList<>());
        this.userAuth=new UserAuth();
        userCounter = new counter();
        storeCounter = new counter();
        productCounter=new counter();
    }


    public int register(String userName, String pass) {
        if(userAuth.register(userName,pass)){
            KingLogger.logEvent(Level.INFO, "Domain.User " + userName + " register to the system");
            int userId=userCounter.inc();
            users.add(new User(userName, userId, 1));
            return userId;
        }
        else{
            return -1;
        }

    }

    //if the user performed login successfully return his id. else return -1
    public int login(String userName,String pass) {
        if(userAuth.loginAuthentication(userName,pass)) {
            KingLogger.logEvent(Level.INFO, "Domain.User " + userName + " logged into the system.");
            for (User user : users) {
                if (user.getUserName().equals(userName) && !user.isLogged()) {
                    user.setLogged(true);
                    return user.getId();
                }
            }
        }
        return -1;
    }


    public int guestLogin() {
        User guest = new User("Guest", userCounter.inc(), 0);
        guest.setLogged(true);
        users.add(guest);
        int id = guest.getId();
        KingLogger.logEvent(Level.INFO, "Guest logged into the system with id: " + id);
        return id;
    }

    public boolean isLogged(int userId) {
        User user = getUserById(userId);
        if (user != null)
            return user.isLogged();
        return false;
    }


    public int guestRegister (int userId, String userName, String password){
        try {
            if(userAuth.guestRegister(userName,password)){
                getUserById(userId).setRegistered();
                getUserById(userId).setName(userName);
                KingLogger.logEvent(Level.INFO, "Domain.User " + userName + " registered to the system.");
                getUserById(userId).setLogged(false);
                return userId;

            }
            else{
                return -1;
            }
        }
        catch (Exception e) {
            KingLogger.logEvent(Level.WARNING, "Guest user failed registering to the system.");
            return -1;
        }
    }

    public boolean logout(int userId) {
        User user = getUserById(userId);
        if (user == null || !user.isLogged() || !user.isRegistered()) {
            return false;
        }
        user.setLogged(false);
        KingLogger.logEvent(Level.INFO, "Domain.User " + user.getUserName() + " logged out of the system.");
        return true;
    }

    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId)
                return user;
        }
        return null;
    }

    public int getNumOfUsers(){
        return users.size();
    }

    public List<Store> getAllStoresInfo(int userId) {
        User u = getUserById(userId);
        if (u != null && u.isLogged()) {
            return this.stores;
        }
        KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " tried to get stores info while logged out and failed.");
        return null;
    }
    public String getAllStoresNames(int userId) {
        User u = getUserById(userId);
        StringBuilder storesNames = new StringBuilder();
        if (u != null && u.isLogged()) {
            for(Store store : this.stores)
                storesNames.append(store.getName()+",");
            storesNames.deleteCharAt(storesNames.length()-1);
        }
        KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " tried to get stores info while logged out and failed.");
        return storesNames.toString();
    }




    public Map<Integer,Integer> getProducts(Filter filter, int userId){
        try{
            if(getUserById(userId).isLogged()) {
                Map<Integer, Integer> output = new HashMap<>();
                switch (filter.searchType) {
                    case "NAME":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByName(filter);
                            for (int productId : ps) {
                                output.put(s.getStoreId(), productId);
                            }
                        }
                        break;
                    case "CATEGORY":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByCategory(filter);
                            for (int productId : ps) {
                                output.put(s.getStoreId(), productId);
                            }
                        }
                        break;
                    case "KEYWORDS":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByKeyWords(filter);
                            for (int productId : ps) {
                                output.put(s.getStoreId(), productId);
                            }
                        }
                        break;

                    default:
                        break;
                }
                return output;
            }
            return new HashMap<>();
        }catch (Exception e){
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " didn't succeed getting products by filter.");
            return new HashMap<>();
        }
    }

    public boolean addProductToBag(int userId, int storeId, int prodId,int amount){
        try {
            Bag b = getUserById(userId).getBagByStoreId(storeId);
            if (getUserById(userId).isLogged() && getStoreById(storeId).getInventory().prodExists(prodId)) {
                if (b != null) {
                    b.addProduct(prodId, amount);
                    KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                    return true;
                }
                getUserById(userId).createNewBag(getStoreById(storeId), prodId, amount);
                KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                return true;
            }
            KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was not added to bag for user " + userId);
            return false;
        }
        catch (Exception e) {
            KingLogger.logEvent(Level.WARNING, "Domain.Product number " + prodId + " was not added to bag for user " + userId);
            return false;
        }
    }

    public List<Bag> getCart(int userId) {
        try {
            if (getUserById(userId) != null && getUserById(userId).isLogged()) {
                List<Bag> bags = getUserById(userId).getBags();
                return bags;
            }
            return new LinkedList<>();
        } catch (Exception e) {
            KingLogger.logEvent(Level.WARNING, "Domain.User with id " + userId + " couldn't view his cart.");
            return new LinkedList<>();
        }
    }

    public boolean removeProductFromBag(int userId,int storeId, int prodId){
        try {
            Bag b = getUserById(userId).getBagByStoreId(storeId);
            if (b != null) {
                b.removeProduct(prodId);
                KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was removed from bag of store " + storeId + " for user " + userId);
                return true;
            }
            KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " doesn't exist in the system.");
            return false;
        }
        catch (Exception e) {
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " doesn't exist in the system.");
            return false;
        }
    }

    public boolean buyProducts(int userId, int storeId,  String creditInfo){
        try {
            Map<Integer, Integer> productsIds = getBag(userId, storeId);
            Store store = getStoreById(storeId);
            Map<Product, Integer> productsAmountBag = new HashMap<>();
            for (int id : productsIds.keySet()) {
                Product p = store.getProductById(id);
                productsAmountBag.put(p, productsIds.get(id));
            }
            Map<Product,Integer> productsAmountBuy=new HashMap<>();
            double totalCost=0;
            for(Product product : productsAmountBag.keySet()){
                synchronized (product){
                    if(store.canBuyProduct(product,productsAmountBag.get(product))){
                        store.removeProductAmount(product,productsAmountBag.get(product));
                        productsAmountBuy.put(product,productsAmountBag.get(product));
                        totalCost+=(product.getPrice()*productsAmountBag.get(product));
                    }
                }
            }
            if(paymentAdapter.pay(totalCost,creditInfo)){
                Receipt rec = new Receipt(storeId, userId,getUserById(userId).getUserName(), productsAmountBuy);
                this.receipts.add(rec);
                store.addReceipt(rec);
                getUserById(userId).addReceipt(rec);
                KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " made purchase in store " + storeId);
                return true;
            }
            else{
                store.abortPurchase(productsAmountBuy);
                KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " couldn't make a purchase in store " + storeId);
                return false;
            }
        }
        catch (Exception e) {
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " couldn't make a purchase in store " + storeId);
            return false;
        }
    }

    private Map<Integer, Integer> getBag(int userId, int storeId) {
        try {
            User user = getUserById(userId);
            Bag bag = user.getBagByStoreId(storeId);
            return bag.getProductIds();
        }
        catch (Exception e) {
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " couldn't view his bag from store " + storeId);
            return null;
        }
    }

    public int addProductToStore(int userId, int storeId , String name, List<Product.Category> categories, double price, String description, int quantity) {
        User user =getUserById(userId);
        Store store= getStoreById(storeId);
        int productId = productCounter.inc();
        if (user.addProductToStore(productId,store,name, categories, price, description, quantity)){
            return productId;
        }
        else{
            return -1;
        }
    }


    public boolean removeProductFromStore(int userId,int storeId, int productId){
        User user = getUserById(userId);
        Store store = getStoreById(storeId);
        return user.removeProductFromStore(store,productId);

    }

    //returns the new store id
    public int openStore(int userId, String storeName){
        User user = getUserById(userId);
        if(user!=null && user.isRegistered()) {
            int newId = storeCounter.inc();
            Store store = new Store(newId, storeName, user);
            user.openStore(store);
            this.stores.add(store);
            return newId;
        }
        return -1;
    }

    public boolean addStoreOwner(int ownerId, int userId,int storeId) {
        if(!checkValidUser(ownerId) || !checkValidUser(userId)) return false;
        User owner=getUserById(ownerId);
        User user=getUserById(userId);
        return owner.addStoreOwner(owner,user,getStoreById(storeId));
    }
        public boolean checkValidUser(int userId)
        {
            User user = getUserById(userId);
            if(user!=null && user.isRegistered())
                return true;
            return false;
        }

    public boolean addStoreManager(int ownerId, int userId, int storeId){
        if(!checkValidUser(ownerId) || !checkValidUser(userId)) return false;
        return getUserById(ownerId).addStoreManager(getUserById(userId),getStoreById(storeId));
    }

    public boolean addPermissions(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        if(!checkValidUser(ownerId)) return false;
        return getUserById(ownerId).addPermissions(getUserById(managerId),getStoreById(storeId),opIndexes);
    }

    public boolean removePermission(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        if(!checkValidUser(ownerId)) return false;
        return getUserById(ownerId).removePermissions(getUserById(managerId),getStoreById(storeId),opIndexes);
    }

    public boolean removeManager(int ownerId, int managerId, int storeId){
        User user= getUserById(ownerId);
        Store store = getStoreById(storeId);
        return user.removeManagerFromStore(getUserById(managerId),store);
    }


    public List<User> getWorkersInformation(int ownerId, int storeId){
        if(!checkValidUser(ownerId)) return null;
        return getUserById(ownerId).getWorkersInformation(getStoreById(storeId));
    }

    public List<Receipt> getStorePurchaseHistory(int ownerId, int storeId){
        if(!checkValidUser(ownerId)) return null;
        return getUserById(ownerId).getStorePurchaseHistory(getStoreById(storeId));
    }

    public List<Receipt> getAllPurchases(int systemManager){
        if(this.systemManager.getId() == systemManager)
        {
            return this.receipts;
        }
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

    public List<Product> getProductsFromStore(int storeId) {
        return getStoreById(storeId).getInventory().getProducts();
    }

    public int getNumOfStores() {
        return stores.size();
    }

    public List<Receipt> getUserPurchaseHistory(int userId) {
        if(!getUserById(userId).isRegistered()){
            return null;
        }

        return getUserById(userId).getPurchaseHistory();

    }

    public boolean isRegister(int userId) {
        return checkValidUser(userId);
    }

    public List<Store> getMyStores(int id) {
        if(checkValidUser(id))
        {
            return getUserById(id).getMyStores();
        }
        return new LinkedList<>();
    }
}
