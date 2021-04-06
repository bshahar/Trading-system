import java.util.*;

import java.util.logging.Level;

public class TradingSystem {

    private static counter userCounter;
    private static counter storeCounter;

    private PaymentAdapter paymentAdapter;
    private SupplementAdapter supplementAdapter;
    private List<Store> stores;
    private User systemManager;
    private List<Receipt> receipts;
    private List<User> users; //TODO every user is a thread
    private HashMap<String,String> userPass;
    private Encryptor encryptor;



    public TradingSystem (User systemManager) {
        this.stores = new LinkedList<>();
        this.receipts = new LinkedList<>();
        this.systemManager =systemManager;
        this.users = new LinkedList<>();
        this.userPass = new LinkedHashMap<>();
        this.encryptor = new Encryptor();
        this.userCounter = new counter();
        this.storeCounter = new counter();
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
            return user.isLogged();
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
            getUserById(userId).setLogged(false);
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

    public List<Store> getAllStoresInfo() {
        return this.stores;
        //TODO in the GUI we need to use the comments code.
//        String output = "";
//        Store[] storesArr = (Store[]) stores.toArray();
//        for (int i = 0; i < storesArr.length ; i ++){
//            output += storesInfo(i);
//        }
//        return output;
    }
    //TODO return the store id, product id
    public Map<Integer,Integer> getProducts(Filter filter){
        try{

            Map<Integer,Integer> output = new HashMap<>();
            switch (filter.searchType){
                case "NAME":
                    for (Store s: stores) {
                        List<Integer> ps = s.getProductsByName(filter);
                        for (int productId: ps) {
                            output.put(s.getStoreId(),productId);
                        }
                    }
                    break;
                case "CATEGORY":
                    for (Store s: stores) {
                        List<Integer> ps = s.getProductsByCategory(filter);
                        for (int productId: ps) {
                            output.put(s.getStoreId(),productId);
                        }
                    }
                    break;
                case "KEYWORDS":
                    for (Store s: stores) {
                        List<Integer> ps = s.getProductsByKeyWords(filter);
                        for (int productId: ps) {
                            output.put(s.getStoreId(),productId);
                        }
                    }
                    break;

                default:
                    break;
            }

            return output;
        }catch (Exception e){
            return new HashMap<>();
        }
    }

    public boolean addProductToBag(int userId, int storeId, int prodId){
        Bag b = getUserById(userId).getBagByStoreId(storeId);
        if(b != null) {
            b.addProduct(prodId);
            KingLogger.logEvent(Level.INFO, "Product number " + prodId + " was added to Bag of store " + storeId + " for user " + userId);
            return true;
        }
        getUserById(userId).createNewBag(getStoreById(storeId), prodId);
        return true;
    }
    public Map<Integer, List<Integer>> getCart(int userId){
        Map<Integer, List<Integer>> m = new HashMap<>();
        List<Bag> bags = getUserById(userId).getBags();
        for (Bag b : bags){
            m.put(b.getStoreId(), b.getProductIds());
            KingLogger.logEvent(Level.INFO, "added Bag of Store number " + b.getStoreId() + " to the cart");
        }
        return m;
    }

    public boolean removeProductFromBag(int userId,int storeId, int prodId){
        Bag b = getUserById(userId).getBagByStoreId(storeId);
        if(b != null) {
            b.removeProduct(prodId);
            KingLogger.logEvent(Level.INFO, "Product number " + prodId + " was remove from Bag of store " + storeId + " for user " + userId);
            return true;
        }
        KingLogger.logError(Level.WARNING,  "user Id " + userId + " didnt found");
        return false;
    }

    public boolean buyProducts(int userId, int storeId, Map<Integer,Integer> productsIds, String creditInfo){
        Store store =getStoreById(storeId);
        double totalCost = 0;
        Map<Product,Integer> products = new HashMap<>();
        for (int id: productsIds.keySet()) {
            Product p=store.getProductById(id);
            products.put(p,productsIds.get(id));
            totalCost += p.getPrice()*productsIds.get(id);
        }
        synchronized (store) {
            boolean canBuy = true;
            for (Integer prodId : productsIds.keySet()) {
                if (!store.canBuyProduct(prodId, productsIds.get(prodId))) {
                    canBuy = false;
                }
            }
            if (canBuy && paymentAdapter.pay(totalCost, creditInfo)) {
                for (Integer prodId : productsIds.keySet()) {
                    store.buyProduct(prodId, productsIds.get(prodId));//remove amount from product
                }
                this.receipts.add(new Receipt(storeId,getUserById(userId).getUserName(),products));
            }
        }
        return false;
    }

    public boolean addProductToStore(int userId, int productId, int storeId ,String name, List<Product.Category> categories,double price, String description, int quantity){
        Store store = getStoreById(storeId);
        if(store != null && store.addProductToStore(getUserById(userId),productId, name, categories, price, description,quantity)){
            KingLogger.logEvent(Level.INFO, "Product number " + productId + " was added to store " + storeId + " by user " + userId);
            return true;
        }
        KingLogger.logError(Level.WARNING, "Product number " + productId + " was !!not!! added to store " + storeId + " by user " + userId);
        return false;
    }


    public boolean removeProductFromStore(int userId,int storeId, int productId){
        Store store = getStoreById(storeId);
        if(store != null && store.removeProductFromStore(getUserById(userId),productId)){
            KingLogger.logEvent(Level.INFO, "Product number " + productId + " was remove to store " + storeId + " by user " + userId);
            return true;
        }
        KingLogger.logError(Level.WARNING, "Product number " + productId + " was !!not!! remove to store " + storeId + " by user " + userId);
        return false;
    }

    //returns the new store id
    public int openStore(int userId, String storeName){
        if(getUserById(userId).isRegistered()) {
            int newId = storeCounter.inc();
            Store store = new Store(newId, storeName, getUserById(userId));
            this.stores.add(store);
            return newId;
        }
        return -1;
    }

    public boolean addStoreOwner(int ownerId, int userId,int storeId){
       //return getStoreById(storeId).appointOwner(ownerId,userId);
    return true;
    }

    public boolean addStoreManager(int ownerId, int userId, int storeId){
        return getStoreById(storeId).appointManager(ownerId,userId);
    }

    public boolean addPermissions(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        Store s = getStoreById(storeId);
        return s.addPermissions(ownerId, managerId, opIndexes);
    }

    public boolean removePermission(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        Store s = getStoreById(storeId);
        return s.removePermissions(ownerId, managerId, opIndexes);
    }

    public boolean removeManager(int ownerId, int managerId, int storeId){
        Store s = getStoreById(storeId);
        return s.removeAppointment(ownerId, managerId);
    }

    public String getWorkersInformation(int ownerId, int storeId){
        Store s = getStoreById(storeId);
        return s.getWorkersInformation(ownerId);
    }

    public List<Receipt> getStorePurchaseHistory(int ownerId, int storeId){
        List<Receipt> purchaseHistory = new LinkedList<>();
        if(getStoreById(storeId).getStorePurchaseHistory(ownerId))
        {
            for(Receipt receipt : receipts)
            {
                if(receipt.getStoreId() == storeId)
                    purchaseHistory.add(receipt);
            }
        }
        return purchaseHistory;
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

    public List<Integer> getProductsFromStore(int storeId) {
        return getStoreById(storeId).getInventory().getProductsIds();
    }
}
