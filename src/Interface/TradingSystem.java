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
    public Result login(String userName,String pass) {
        if(userAuth.loginAuthentication(userName,pass)) {
            KingLogger.logEvent(Level.INFO, "Domain.User " + userName + " logged into the system.");
            for (User user : users) {
                if (user.getUserName() == userName) {
                    user.setLogged(true);
                    return new Result( true,user.getId());
                }
            }
        }
        return new Result(false, -1);
    }


    public Result guestLogin() {
        User guest = new User("Guest", userCounter.inc(), 0);
        users.add(guest);
        int id = guest.getId();
        KingLogger.logEvent(Level.INFO, "Guest logged into the system with id: " + id);
        return new Result(true,id);
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

    public Result logout(int userId) {
        User user = getUserById(userId);
        if (user == null || !user.isLogged() || !user.isRegistered()) {
            return new Result(false,"User has not logged in");
        }
        user.setLogged(false);
        KingLogger.logEvent(Level.INFO, "Domain.User " + user.getUserName() + " logged out of the system.");
        return new Result(true,true);
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

    public Result getAllStoresInfo(int userId) {
        User u = getUserById(userId);
        if (u != null && u.isLogged()) {
            return new Result(true,this.stores) ;
        }
        KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " tried to get stores info while logged out and failed.");

        return new Result(false,"User has not logged in");
    }

    public Result getProducts(Filter filter, int userId){
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
                return new Result(true,output); // data= Map<Integer, Integer>
            }
            return new Result(false,"User has not logged int");
        }catch (Exception e){
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " didn't succeed getting products by filter.");
            return new Result(false,"Can't get products by the given parameters");
        }
    }

    public Result addProductToBag(int userId, int storeId, int prodId,int amount){
        try {
            Bag b = getUserById(userId).getBagByStoreId(storeId);
            if (getUserById(userId).isLogged()){
                if( getStoreById(storeId).getInventory().prodExists(prodId)){
                    if (b != null) {
                        b.addProduct(prodId, amount);
                        KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                        return new Result(true,true);
                    }
                    getUserById(userId).createNewBag(getStoreById(storeId), prodId, amount);
                    KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                    return new Result(true,true);
                }
                else{
                    return new Result(false,"Given Product not exist");
                }
            }else{
                KingLogger.logEvent(Level.INFO, "Domain.Product number " + prodId + " was not added to bag for user " + userId);
                return new Result(false, "User has not logged in");
            }
        }
        catch (Exception e) {
            KingLogger.logEvent(Level.WARNING, "Domain.Product number " + prodId + " was not added to bag for user " + userId);
            return new Result(false, "Can't add product to bag");
        }
    }

    public Result getCart(int userId) {
        try {
            if (getUserById(userId) != null && getUserById(userId).isLogged()) {
                List<Bag> bags = getUserById(userId).getBags();
                return new Result(true,bags); //List<Bag>
            }
            return new Result(false,"User has not logged in");
        } catch (Exception e) {
            KingLogger.logEvent(Level.WARNING, "Domain.User with id " + userId + " couldn't view his cart.");
            return new Result(false,"Can't get cart");
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

    public Result buyProducts(int userId, int storeId,  String creditInfo){
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
                if(productsAmountBag.size()==productsAmountBuy.size()){
                    return new Result(true, "purchase confirmed successfully" );
                }else{
                    return new Result(true, "some product missing");
                }
            }
            else{
                store.abortPurchase(productsAmountBuy);
                KingLogger.logError(Level.INFO, "Domain.User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false,"payment failed");
            }
        }
        catch (Exception e) {
            KingLogger.logError(Level.WARNING, "Domain.User with id " + userId + " couldn't make a purchase in store " + storeId);
            return new Result(false,"purchase failed");
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

    public Result addProductToStore(int userId, int storeId , String name, List<Product.Category> categories, double price, String description, int quantity) {
        User user =getUserById(userId);
        Store store= getStoreById(storeId);
        int productId = productCounter.inc();
        if (user.addProductToStore(productId,store,name, categories, price, description, quantity)){
            return new Result(true,productId);
        }
        else{
            return new Result(false,"Can't add product to Store");
        }
    }


    public Result removeProductFromStore(int userId,int storeId, int productId){
        User user = getUserById(userId);
        Store store = getStoreById(storeId);
        return user.removeProductFromStore(store,productId);

    }

    //returns the new store id
    public Result openStore(int userId, String storeName){
        User user = getUserById(userId);
        if(user!=null && user.isRegistered()) {
            int newId = storeCounter.inc();
            Store store = new Store(newId, storeName, user);
            user.openStore(store);
            this.stores.add(store);
            return new Result(true,newId);
        }
        return new Result(false,"User has not registered");
    }

    public Result addStoreOwner(int ownerId, int userId,int storeId) {
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

    public Result addStoreManager(int ownerId, int userId, int storeId){
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
}
