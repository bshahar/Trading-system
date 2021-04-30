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
    private static counter observableCounter;

    private PaymentAdapter paymentAdapter;
    private SupplementAdapter supplementAdapter;
    private UserAuth userAuth;
    private List<Store> stores;
    private User systemManager;
    private List<Receipt> receipts;
    private List<User> users;
    private List<ObservableType> observers;


    public static enum Permission {
        DEF,
        AddProduct,
        AppointManager,
        AppointOwner,
        CloseStore,
        DefineDiscountFormat,
        DefineDiscountPolicy,
        DefinePurchaseFormat,
        DefinePurchasePolicy,
        EditDiscountFormat,
        EditDiscountPolicy,
        EditProduct,
        EditPurchaseFormat,
        EditPurchasePolicy,
        GetWorkersInfo,
        OpenStore,
        RemoveManagerAppointment,
        RemoveOwnerAppointment,
        None,
        RemoveProduct,
        ReopenStore,
        ReplayMessages,
        ViewMessages,
        ViewPurchaseHistory
    }
    public static String[] permissionsName= {
            "DEF",
            "AddProduct",
            "AppointManager",
            "AppointOwner",
            "CloseStore",
            "DefineDiscountFormat",
            "DefineDiscountPolicy",
            "DefinePurchaseFormat",
            "DefinePurchasePolicy",
            "EditDiscountFormat",
            "EditDiscountPolicy",
            "EditProduct",
            "EditPurchaseFormat",
            "EditPurchasePolicy",
            "GetWorkersInfo",
            "OpenStore",
            "RemoveManagerAppointmen",
            "RemoveOwnerAppointment",
            "None",
            "RemoveProduct",
            "ReopenStore",
            "ReplayMessages",
            "ViewMessages",
            "ViewPurchaseHistory"};

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
        KingLogger.logEvent("Trading System initialized");
        observableCounter = new counter();
        this.observers = Collections.synchronizedList(new LinkedList<>());
    }

    public Result getUserIdByName(String userName) {

        for(User user : users){
            if(user.getUserName().equals(userName)){
                return new Result(true,user.getId());
            }
        }
        return new Result(false, "User Name not exist");
    }


    public Result register(String userName, String pass) {
        if(userAuth.register(userName,pass)){
            int userId=userCounter.inc();
            users.add(new User(userName, userId, 1));
            KingLogger.logEvent("REGISTER:  User " + userName + " register to the system");
            return new Result(true,userId);
        }
        else{
            KingLogger.logEvent("REGISTER:  User " + userName + ": username or password incorrect");
            return new Result(false,"Username or Password not correct");
        }

    }

    //if the user performed login successfully return his id. else return -1
    public Result login(String userName,String pass) {
        if(userAuth.loginAuthentication(userName,pass)) {
            for (User user : users) {
                if (user.getUserName().equals(userName) && !user.isLogged()) {
                    user.setLogged(true);
                    KingLogger.logEvent("LOGIN:  User " + userName + " logged into the system.");
                    return new Result( true,user.getId());
                }
            }
        }
        KingLogger.logEvent("LOGIN:  User " + userName + ": Username or Password not correct");
        return new Result(false, "Username or Password not correct");
    }


    public Result notifyToSubscribers(int observableTypeId,String msg)
    {
        Result result =getObservableTypeById(observableTypeId);
        if(result.isResult())
        {
            ObservableType o = (ObservableType) result.getdata();
            o.sendAll(msg);
            return new Result(true,"msg send susccefully");
        }
        return new Result(false,result.getdata());
    }

    public Result addObservable(String name)
    {
        int id = observableCounter.inc();
        this.observers.add(new ObservableType(name,id));
        return new Result(true,id);
    }

    public Result removeObservable(int observableTypeId)
    {
        Result result =getObservableTypeById(observableTypeId);
        if(result.isResult())
        {
            ObservableType Observable = (ObservableType) result.getdata();
            this.observers.remove(Observable);
            return new Result(true,"Observable remove successfully");
        }

        return new Result(false,result.getdata());
    }

    public Result subscribeToObservable(int observableId,int userId)
    {
        Result result =getObservableTypeById(observableId);
        if(result.isResult())
        {
            ObservableType Observable = (ObservableType) result.getdata();
            Observable.addObserver(getUserById(userId));
            return new Result(true,"user subscribe successfully");
        }
        return new Result(false,result.getdata());
    }

    public Result unsubscribeToObservable(int observableId,int userId)
    {
        Result result =getObservableTypeById(observableId);
        if(result.isResult() && checkValidUser(userId))
        {
            ObservableType Observable = (ObservableType) result.getdata();
            Observable.deleteObserver(getUserById(userId));
            return new Result(true,"user unsubscribe successfully");
        }
        return new Result(false,result.getdata());
    }


    public Result getObservableTypeById(int observableTypeId)
    {
        for(ObservableType observableType : observers)
        {
            if(observableType.getId() == observableTypeId)
                return new Result(true,observableType);
        }
        return new Result(false,"observableTypeId isn't exist");
    }


    public Result guestLogin() {
        User guest = new User("Guest", userCounter.inc(), 0);
        guest.setLogged(true);
        users.add(guest);
        int id = guest.getId();
        KingLogger.logEvent("GUEST_LOGIN: Guest logged into the system with id: " + id);
        return new Result(true,id);
    }

    public Result isLogged(int userId) {
        User user = getUserById(userId);
        if (user != null)
            return new Result(true,user.isLogged());
        return new Result(false,"User not exist");
    }


    public Result guestRegister (int userId, String userName, String password){
        try {
            if(userAuth.guestRegister(userName,password)){
                getUserById(userId).setRegistered();
                getUserById(userId).setName(userName);
                getUserById(userId).setLogged(false);
                KingLogger.logEvent("GUEST_REGISTER: User " + userName + " registered to the system.");
                return new Result(true,userId);

            }
            else{
                KingLogger.logEvent("GUEST_REGISTER: User " + userName + ": Can't Register with given Username and PassWord");
                return new Result(false,"Can't Register with given Username and PassWord");
            }
        }
        catch (Exception e) {
            KingLogger.logError("GUEST_REGISTER: User " + userName + ": failed registering to the system.");
            return new Result(false,"Can't Register with given Username and PassWord");
        }
    }

    public Result logout(int userId) {
        User user = getUserById(userId);
        if (user == null || !user.isLogged() || !user.isRegistered()) {
            KingLogger.logEvent("LOGOUT: User " + userId + ": User has not logged in");
            return new Result(false,"User has not logged in");
        }
        user.setLogged(false);
        KingLogger.logEvent("LOGOUT: User " + user.getUserName() + " logged out of the system.");
        return new Result(true,true);
    }

    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId)
                return user;
        }
        return null;
    }

    public Result getNumOfUsers(){
        return new Result(true,users.size());
    }

    public Result getAllStoresInfo(int userId) {
        User u = getUserById(userId);
        if (u != null && u.isLogged()) {
            return new Result(true,this.stores) ;
        }
        KingLogger.logEvent("GET_ALL_STORES_INFO: User with id " + userId + " tried to get stores info while logged out and failed.");
        return new Result(false,"User has not logged in");
    }
    public String getAllStoresNames(int userId) {
        User u = getUserById(userId);
        StringBuilder storesNames = new StringBuilder();
        if (u != null && u.isLogged()) {
            for(Store store : this.stores)
                storesNames.append(store.getName()+",");
            storesNames.deleteCharAt(storesNames.length()-1);
        }
        KingLogger.logEvent("GET_ALL_STORES_NAME: User with id " + userId + " get stores info.");
        return storesNames.toString();
    }




    public Result getProducts(Filter filter, int userId){
        try{
            if(getUserById(userId).isLogged()) {
                Map<Integer, Integer> output = new HashMap<>();
                switch (filter.searchType) {
                    case "Name":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByName(filter);
                            for (int productId : ps) {
                                output.put(productId,s.getStoreId());
                            }
                        }
                        break;
                    case "Category":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByCategory(filter);
                            for (int productId : ps) {
                                output.put(productId,s.getStoreId());
                            }
                        }
                        break;
                    case "Keywords":
                        for (Store s : stores) {
                            List<Integer> ps = s.getProductsByKeyWords(filter);
                            for (int productId : ps) {
                                output.put(productId,s.getStoreId());
                            }
                        }
                        break;

                    default:
                        break;
                }
                KingLogger.logEvent("GET_PRODUCTS: User with id " + userId + " getting products by filter " + filter.searchType);
                return new Result(true,output); // data= Map<Integer, Integer>
            }
            KingLogger.logEvent("GET_PRODUCTS: User with id " + userId + " didnt succeed getting products because not logged in");
            return new Result(false,"User has not logged int");
        }catch (Exception e){
            KingLogger.logError("GET_PRODUCTS: User with id " + userId + " didn't succeed getting products by filter.");
            return new Result(false,"Can't get products by the given parameters");
        }
    }

    public Result addProductToBag(int userId, int storeId, int prodId,int amount){
        try {
            Bag b = getUserById(userId).getBagByStoreId(storeId);
            if(amount>0){
                if (getUserById(userId).isLogged()){
                    if( getStoreById(storeId).getInventory().prodExists(prodId)){
                        if (b != null) {
                            b.addProduct(getProductById(prodId), amount);
                            KingLogger.logEvent("ADD_PRODUCT_TO_BAG: Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                            return new Result(true,true);
                        }
                        getUserById(userId).createNewBag(getStoreById(storeId), prodId, amount);
                        KingLogger.logEvent("ADD_PRODUCT_TO_BAG: Domain.Product number " + prodId + " was added to bag of store " + storeId + " for user " + userId);
                        return new Result(true,true);
                    }
                    else{
                        return new Result(false,"Given Product not exist");
                    }
                }else{
                    KingLogger.logEvent("ADD_PRODUCT_TO_BAG: Domain.Product number " + prodId + " was not added to bag for user " + userId);
                    return new Result(false, "User has not logged in");
                }
            }else{
                KingLogger.logEvent("ADD_PRODUCT_TO_BAG: Domain.Product - amount cant be negative");
                return new Result(false,"Amount can't be negative ");
            }

        }
        catch (Exception e) {
            KingLogger.logError("ADD_PRODUCT_TO_BAG: Domain.Product number " + prodId + " was not added to bag for user " + userId);
            return new Result(false, "Can't add product to bag");
        }
    }

    public Result getCart(int userId) {
        try {
            if (getUserById(userId) != null && getUserById(userId).isLogged()) {
                List<Bag> bags = getUserById(userId).getBags();
                KingLogger.logEvent("GET_CART: User with id " + userId + " got his cart.");
                return new Result(true,bags); //List<Bag>
            }
            KingLogger.logEvent("GET_CART: User with id " + userId + " not logged in so cant get his cart.");
            return new Result(false,"User has not logged in");
        } catch (Exception e) {
            KingLogger.logError("GET_CART: User with id " + userId + " couldn't view his cart.");
            return new Result(false,"Can't get cart");
        }
    }

    public boolean removeProductFromBag(int userId,int storeId, int prodId){
        try {
            Bag b = getUserById(userId).getBagByStoreId(storeId);
            if (b != null) {
                b.removeProduct(getProductById(prodId));
                if(b.getProdNum()==0){
                    getUserById(userId).removeBag(b);
                }
                KingLogger.logEvent("REMOVE_PRODUCT_FROM_BUG: Domain.Product number " + prodId + " was removed from bag of store " + storeId + " for user " + userId);
                return true;
            }
            KingLogger.logEvent("REMOVE_PRODUCT_FROM_BUG: User with id " + userId + " doesn't exist in the system.");
            return false;
        }
        catch (Exception e) {
            KingLogger.logError("REMOVE_PRODUCT_FROM_BUG: User with id " + userId + " doesn't exist in the system.");
            return false;
        }
    }

    public Result buyProducts(int userId, int storeId,  String creditInfo){
        try {
            Map<Product, Integer> productsIds = getBag(userId, storeId);
            Store store = getStoreById(storeId);
            Map<Product, Integer> productsAmountBag = new HashMap<>();
            for (Product p : productsIds.keySet()) {
                productsAmountBag.put(p, productsIds.get(p));
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
                getUserById(userId).removeProductFromCart(productsAmountBuy,storeId);
                Receipt rec = new Receipt(storeId, userId,getUserById(userId).getUserName(), productsAmountBuy);
                this.receipts.add(rec);
                store.addReceipt(rec);
                getUserById(userId).addReceipt(rec);
                if(productsAmountBag.size()==productsAmountBuy.size()){
                    KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " made purchase in store " + storeId);
                    notifyToSubscribers(getStoreById(storeId).getNotificationId(),"Some one buy from your store! you can go to your purchase to see more details");
                    return new Result(true, "purchase confirmed successfully" );
                }else{
                    KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " try to purchase in store " + storeId + "but soe product are missing");
                    return new Result(true, "some product missing");
                }
            }
            else{
                store.abortPurchase(productsAmountBuy);
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false,"payment failed");
            }
        }
        catch (Exception e) {
            KingLogger.logError("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
            return new Result(false,"purchase failed");
        }
    }

    private Map<Product, Integer> getBag(int userId, int storeId) {
        try {
            User user = getUserById(userId);
            Bag bag = user.getBagByStoreId(storeId);
            KingLogger.logEvent("GET_BAG: User with id " + userId + " view his bag from store " + storeId);
            return bag.getProductIds();
        }
        catch (Exception e) {
            KingLogger.logError("GET_BAG: User with id " + userId + " couldn't view his bag from store " + storeId);
            return null;
        }
    }

    public Result addProductToStore(int userId, int storeId , String name, List<String> categories, double price, String description, int quantity) {
        User user =getUserById(userId);
        Store store= getStoreById(storeId);
        int productId = productCounter.inc();
        if (user.addProductToStore(productId,store,name, categories, price, description, quantity)){
            KingLogger.logEvent("ADD_PRODUCT_TO_STORE: User with id " + userId + " add product to store " + storeId);
            return new Result(true,productId);
        }
        else{
            KingLogger.logEvent("ADD_PRODUCT_TO_STORE: User with id " + userId + " cant add product to store " + storeId);
            return new Result(false,"Can't add product to Store");
        }
    }


    public Result removeProductFromStore(int userId,int storeId, int productId){
        User user = getUserById(userId);
        Store store = getStoreById(storeId);
        KingLogger.logEvent("REMOVE_PRODUCT_FROM_STORE: User with id " + userId + " removed product from store " + storeId);
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
            KingLogger.logEvent("OPEN_STORE: User with id " + userId + " open the store " + storeName);
            Result result = addObservable(storeName);
            int subscribeId = (int)result.getdata();
            store.setNotificationId(subscribeId);
            subscribeToObservable(subscribeId,userId);
            return new Result(true,newId);
        }
        KingLogger.logEvent("OPEN_STORE: User with id " + userId + " cant open the store " + storeName + "because is not registered");
        return new Result(false,"User has not registered");
    }

    public Result addStoreOwner(int ownerId, int userId,int storeId) {
        if(!checkValidUser(ownerId) || !checkValidUser(userId)) return new Result(false,"User is not valid");
        User owner=getUserById(ownerId);
        User user=getUserById(userId);

        Result result = owner.addStoreOwner(owner,user,getStoreById(storeId));
        if(result.isResult())
        {
            KingLogger.logEvent("ADD_STORE_OWNER: User with id " + owner + " try to add store owner and" + result.getdata());

            subscribeToObservable(getStoreById(storeId).getNotificationId(),userId);
        }
        return result;

    }
    public boolean checkValidUser(int userId)
    {
        User user = getUserById(userId);
        if(user!=null && user.isRegistered())
            return true;
        return false;
    }

    public Result addStoreManager(int ownerId, int userId, int storeId){
        if(!checkValidUser(ownerId) || !checkValidUser(userId)){
            KingLogger.logEvent("ADD_STORE_EVENT: User with id " + ownerId + " cant appoint manager because user not valid");
            return new Result(false,"User is not valid");
        }

        Result result = getUserById(ownerId).addStoreManager(getUserById(userId),getStoreById(storeId));
        if(result.isResult())
        {
            KingLogger.logEvent("ADD_STORE_EVENT: User with id " + ownerId + " appoint " + userId + "to be manager of store " + storeId);

            subscribeToObservable(getStoreById(storeId).getNotificationId(),userId);
            getUserById(userId).addNotification("You are now manager in store: "+ getStoreName(storeId));
        }
        return result;
    }

    public Result addPermissions(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        if(!checkValidUser(ownerId)) {
            KingLogger.logEvent("ADD_PERMISSION: User with id " + ownerId + " cant add permission because not valid user");
            return new Result(false,"User isn't register");
        }
        KingLogger.logEvent("ADD_PERMISSION: User with id " + ownerId + " add permission to user " + managerId);
        return getUserById(ownerId).addPermissions(getUserById(managerId),getStoreById(storeId),opIndexes);
    }

    public Result removePermission(int ownerId, int managerId, int storeId, List<Integer> opIndexes){
        if(!checkValidUser(ownerId)) {
            KingLogger.logEvent("ADD_PERMISSION: User with id " + ownerId + " add permission to user " + managerId);
            return new Result(false,"User isn't register");
        }
        KingLogger.logEvent("ADD_PERMISSION: User with id " + ownerId + " remove permission to user " + managerId);
        return getUserById(ownerId).removePermissions(getUserById(managerId),getStoreById(storeId),opIndexes);
    }

    public Result removeManager(int ownerId, int managerId, int storeId){
        User user= getUserById(ownerId);
        Store store = getStoreById(storeId);
        Result result = user.removeManagerFromStore(getUserById(managerId),store);
        if(result.isResult())
        {
            KingLogger.logEvent("REMOVE_MANAGER: User with id " + ownerId + " remove manager and " + result.getdata());
            unsubscribeToObservable(getStoreById(storeId).getNotificationId(),managerId);
        }
        return result;

    }

    public Result removeOwner(int ownerId, int managerId, int storeId){
        User user= getUserById(ownerId);
        Store store = getStoreById(storeId);
        Result result = user.removeOwnerFromStore(getUserById(managerId),store);
        if(result.isResult())
        {
            unsubscribeToObservable(getStoreById(storeId).getNotificationId(),managerId);
        }
        return result;

    }




    public Result getWorkersInformation(int ownerId, int storeId){
        if(!checkValidUser(ownerId)) {
            KingLogger.logEvent("GET_WORKERS_INFORMATION: User with id " + ownerId + " is not a valid user");
            return null;
        }
        KingLogger.logEvent("GET_WORKERS_INFORMATION: User with id " + ownerId + " got workers information");
        return getUserById(ownerId).getWorkersInformation(getStoreById(storeId)); //List<User>
    }

    public Result getStorePurchaseHistory(int ownerId, int storeId){
        if(!checkValidUser(ownerId)) {
            KingLogger.logEvent("GET_STORE_PURCHASE_HISTORY: User with id " + ownerId + " is not a valid user");
            return new Result(false,"User not exist");
        }
        KingLogger.logEvent("GET_STORE_PURCHASE_HISTORY: User with id " + ownerId + " got store purchase history");
        return getUserById(ownerId).getStorePurchaseHistory(getStoreById(storeId));//List<Reciept>
    }

    public Result getAllPurchases(int systemManager){
        if(this.systemManager.getId() == systemManager)
        {
            KingLogger.logEvent("GET_ALL_PURCHASE: system manager with id " + systemManager + " got all purchases");
            return new Result(true,this.receipts);//List<Reciept>
        }
        KingLogger.logEvent("GET_ALL_PURCHASE: system manager with id " + systemManager + " dont have permission");
        return new Result(false,"User has no permissions");
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

    public Result getProductsFromStore(int storeId) {
        List<Product> products=new LinkedList<>();
        Map<Product , Integer> amounts= getStoreById(storeId).getInventory().getProductsAmounts();
        for(Product product :amounts.keySet()){
            products.add(product);
            product.setAmount(amounts.get(product));
        }
        return new Result(true,products);
    }

    public Result getNumOfStores() {
        return new Result(true,stores.size());
    }

    public Result getUserPurchaseHistory(int userId) {
        if(!getUserById(userId).isRegistered()){
            KingLogger.logEvent("GET_USER_PURCHASE_HISTORY: User with id " + userId + " is not registered");
            return new Result(false,"User not registered");
        }
        KingLogger.logEvent("GET_USER_PURCHASE_HISTORY: User with id " + userId + " got the user purchase history");
        return new Result(true,getUserById(userId).getPurchaseHistory());

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



    public String getStoreName(int storeId) {
        for(Store store:stores){
            if(store.getStoreId()==storeId){
                return store.getName();
            }
        }
        return "";
    }

    public Product getProductById(Integer productId) {
        for(Store store:stores){
            if(store.getProductById(productId)!=null){
                return store.getProductById(productId);
            }
        }
        return null;

    }

    public boolean checkPermissions(int userId,int storeId ,int permissionId) {

        if(getUserById(userId)!=null && getStoreById(storeId)!=null)
            return getUserById(userId).checkPermissions(getStoreById(storeId),permissionId);
        return false;
    }

    public Result getMessagesQueue (int userId) {
        if(getUserById(userId)!=null)
        {
            return new Result(true,getUserById(userId).getMessages());
        }
        return new Result(false,"user isnt exist");
    }

    public Result getNotificationIdByStoreId(int storeId) {
        if(getStoreById(storeId)!=null)
        {
            return new Result(true ,getStoreById(storeId).getNotificationId());
        }
        return new Result(false,"cant find the store");
    }

    public Result getMessagesQueueAsArray(int userId) {
        if(getUserById(userId)!=null)
        {
            return new Result(true,getUserById(userId).getMessages().toArray());
        }
        return new Result(false,"user isnt exist");
    }

    public Result getManagersAndOwnersOfStore(int storeId) {
        if(getStoreById(storeId)!=null)
        {
            return new Result(true,getStoreById(storeId).getManagersAndOwners());
        }
        return new Result(false,"no such store");
    }

    public Result editProduct(int userId, int storeId, int productId, int price, int amount) {
        User user=getUserById(userId);
        Product product=getProductById(productId);
        Store store =getStoreById(storeId);
        if(user!=null && product!=null){
            return user.editProduct(store,product,price,amount);

        }else{
            return new Result(false,"user or product not exist");
        }
    }

    public int getProductAmount(Integer prodId) {
        for(Store store: stores){
            if(store.getProductById(prodId)!=null){
                return store.getProductAmount(prodId);
            }
        }
        return -1;
    }

    public Result getUserPermissionsMap(int ownerId, String managerName, int storeId) {
        Result result=getUserIdByName(managerName);
        if(result.isResult()){
            int userId= (int)result.getdata();
            result= getUserPermissions(userId,storeId);
            if(result.isResult()){
                List<String> permissionsNames=(List<String>) result.getdata();
                Map<String,Boolean> permissionsBool=new HashMap<>();
                permissionsBool.put("AddProduct",permissionsNames.contains("AddProduct"));
                permissionsBool.put("AppointManager",permissionsNames.contains("AppointManager"));
                permissionsBool.put("RemoveManagerAppointmen",permissionsNames.contains("RemoveManagerAppointmen"));
                permissionsBool.put("AppointOwner",permissionsNames.contains("AppointOwner"));
                permissionsBool.put("RemoveOwnerAppointmen",permissionsNames.contains("RemoveOwnerAppointmen"));
                permissionsBool.put("EditProduct",permissionsNames.contains("EditProduct"));
                permissionsBool.put("ViewPurchaseHistory",permissionsNames.contains("ViewPurchaseHistory"));
                permissionsBool.put("GetWorkersInfo",permissionsNames.contains("GetWorkersInfo"));
                return new Result(true,permissionsBool);
            }else{
                return result;
            }
        }else{
            return result;
        }
    }

    public Result getUserPermissions(int id, int storeId) {
        List<String> names=new LinkedList<>();
        for(TradingSystem.Permission permission : TradingSystem.Permission.values()){
            if(checkPermissions(id,storeId,permission.ordinal())){
                names.add(permissionsName[permission.ordinal()]);
            }
        }
        return new Result(true,names);

    }
}
