package Interface;

import Domain.*;
import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.DiscountPolicies.PolicyCondition;
import Domain.Operators.AndOperator;
import Domain.Operators.NoneOperator;
import Domain.Operators.OrOperator;
import Domain.Operators.XorOperator;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.Sessions.SessionInterface;
import Domain.User;
import Persistence.StoreWrapper;
import Persistence.ReceiptWrapper;
import Persistence.UserWrapper;
import Service.*;
import javafx.util.Pair;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TradingSystem {

    private static counter userCounter;
    private static counter storeCounter;
    private static counter productCounter;
    private static counter receiptCounter;
    private static counter observableCounter;
    private static counter conditionCounter;

    private static PaymentInterface paymentAdapter;
    private static SupplementInterface supplementAdapter;
    private UserAuth userAuth;
    private StoreWrapper stores;
    private User systemManager;
    private ReceiptWrapper receipts;
    private UserWrapper users;
    private List<ObservableType> observers;
    public static Map<Integer , SessionInterface> sessionsMap ;

    public TradingSystem (User systemManager, String externalSystemsUrl, boolean testing) {
        if(testing) {
           // initializeSystemForTests();
        }
        this.users =  new UserWrapper();
        this.receipts =  new ReceiptWrapper();
        paymentAdapter = new PaymentAdapter(externalSystemsUrl);
        supplementAdapter = new SupplementAdapter(externalSystemsUrl);
        this.stores = Collections.synchronizedList(new LinkedList<>());
        this.stores = new StoreWrapper();

        this.userAuth = new UserAuth();
        userAuth.register(systemManager.getUserName(), "123");
        //users.add(systemManager);
        userCounter = new counter();
        storeCounter = new counter();
        productCounter=new counter();
        KingLogger.logEvent("Trading System initialized");
        conditionCounter = new counter();
        observableCounter = new counter();
        this.observers = Collections.synchronizedList(new LinkedList<>());
        receiptCounter = new counter();
        this.systemManager = systemManager;
        AppointSystemManager(systemManager);
        sessionsMap = new ConcurrentHashMap<>();
    }

//    public int getSystemManagerId() {
//        return systemManager.getId();
//    }
//
//    public List<Integer> getpermissionsIndex(List<String> names) {
//        List<Integer> indexes =new LinkedList<>();
//        for(String name: names){
//            indexes.add(getIndex(TradingSystem.permissionsName, name));
//        }
//        return indexes;
//    }
//
//    private Integer getIndex(String[] permissionsName, String name) {
//        for(int i=0; i<permissionsName.length;i++){
//            if(permissionsName[i].equals(name)){
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public Result getGlobalPurchaseUserHistory(int tradingSystemManager, int userId) {
//        if(this.systemManager.getId() == tradingSystemManager){
//            KingLogger.logEvent("GLOBAL_USER_HISTORY:  system manager " + tradingSystemManager + "got purchase history of user " + userId);
//            return getUserPurchaseHistory(userId);
//        }
//        KingLogger.logEvent("GLOBAL_USER_HISTORY:  user " + tradingSystemManager + "is not a system manager");
//        return new Result(false, "Username is not system manager");
//    }
//
//    public Result addSession(int userId, Session session) {
//        getUserById(userId).setSession(session);
//        return new Result(true,"session added\n");
//    }
//    public Result removeSession(int userId) {
//        getUserById(userId).setSession(null);
//        return new Result(true,"session remove\n");
//    }
//
//    public Result sendAlertsAfterPurchase(int storeId) {
//        try {
//            Result result2 = API.getManagersAndOwnersOfStore(storeId);
//            for (Integer id : (Set<Integer>) result2.getData()) {
//                sendAlert(id,"Someone buy from your store! go check it out");
//            }
//            return new Result(true,"send successfully alerts\n");
//        }
//        catch (Exception e)
//        {
//            return new Result(false,"Exception while sending msg\n");
//        }
//
//    }

    public static void setPaymentAdapterDemo() { paymentAdapter = new DemoPayment(); }

    public static void setSupplementAdapterDemo() { supplementAdapter = new DemoSupplement(); }

    public static void initializeSystemForTests() {
        setPaymentAdapterDemo();
        setSupplementAdapterDemo();
    }
    public boolean isSystemManager(int userId) {
        return getUserById(userId).isSystemManager();

    }

    public Result addPurchaseOffer(int storeId, int userId, int prodId, double offer, int numOfProd) {
        Store st = getStoreById(storeId);
        if(st != null && st.prodExists(prodId))  {
            int offerId = st.addPurchaseOffer(prodId, getUserById(userId), offer, numOfProd);
            List<User> ownersManagers = new LinkedList<>();
            ownersManagers.addAll(st.getOwners());
            ownersManagers.addAll(st.getManagers());
            for(User u:ownersManagers){
                sendAlert(u.getId(),"Someone has made an offer on " + getProductById(prodId).getName() +" with id " + prodId);
            }
            return new Result(true, offerId);
        }
        return new Result(false, -1);
    }



    public Result responedToOffer(int storeId, int userId, int prodId, int offerId, String responed, double counterOffer) {
        int informTo = getStoreById(storeId).getUserMadeTheOffer(prodId,offerId);
        Result r = getUserById(userId).responedToOffer(getStoreById(storeId),prodId,offerId,responed,counterOffer, "ACT");
        if(responed.equals("APPROVED") && r.isResult()){
            sendAlert(informTo,"Your offer had been approved!");
        }
        else if(responed.equals("DISAPPROVED") && r.isResult()){
            sendAlert(informTo,"Your offer had been disapproved.");
        }
        else if(responed.equals("COUNTEROFFER") && r.isResult()){
            sendAlert(informTo,"The store management sent you a counter offer.");
        }
        return r;
    }

    public Result responedToCounterPurchaseOffer(int storeId, int userId, int prodId, boolean approve) {
        Bag bag = getUserById(userId).getBagByStoreId(storeId);
        if(approve){
          bag.approveCounterOffer(getProductById(prodId));
        }
        bag.rejectCounterOffer(getProductById(prodId));
        return new Result(true, "the counter offer has been responed");
    }

    public Result getOffersForStore(int storeId, int userId) {
        return getUserById(userId).responedToOffer(getStoreById(storeId), -1, -1, "", -1,  "GET");
    }

    public Result getOffersForCostumer(int userId) {
        Map<PurchaseOffer, Product> output = new HashMap<>();
        User u = getUserById(userId);
        List<Bag> bags = u.getBags();
        for (Bag b: bags) {
           for(Map.Entry<Product, PurchaseOffer> entry : b.getCounterOffers().entrySet()){
               output.put(entry.getValue() , entry.getKey());
           }
        }
        return new Result(true, output);
    }


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
        ViewPurchaseHistory,
        ViewDiscountPolicies,
        ViewPurchasePolicies,
        ResponedToOffer

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
            "ViewPurchaseHistory",
            "ViewDiscountPolicies",
            "ViewPurchasePolicies",
            "ResponedToOffer"
    };



    public int getSystemManagerId() {
        return systemManager.getId();
    }

    public List<Integer> getpermissionsIndex(List<String> names) {
        List<Integer> indexes =new LinkedList<>();
        for(String name: names){
            indexes.add(getIndex(TradingSystem.permissionsName, name));
        }
        return indexes;
    }

    private Integer getIndex(String[] permissionsName, String name) {
        for(int i=0; i<permissionsName.length;i++){
            if(permissionsName[i].equals(name)){
                return i;
            }
        }
        return -1;
    }

    public Result getGlobalPurchaseUserHistory(int tradingSystemManager, int userId) {
        if(this.systemManager.getId() == tradingSystemManager){
            KingLogger.logEvent("GLOBAL_USER_HISTORY:  system manager " + tradingSystemManager + "got purchase history of user " + userId);
            return getUserPurchaseHistory(userId);
        }
        KingLogger.logEvent("GLOBAL_USER_HISTORY:  user " + tradingSystemManager + "is not a system manager");
        return new Result(false, "Username is not system manager");
    }

    public Result addSession(int userId, Session session) {
        getUserById(userId).setSession(session);
        return new Result(true,"session added\n");
    }
    public Result removeSession(int userId) {
        getUserById(userId).setSession((Session) null);
        return new Result(true,"session remove\n");
    }

    public Result sendAlertsAfterPurchase(int storeId) {
        try {
            Result result2 = API.getManagersAndOwnersOfStore(storeId);
            for (Integer id : (Set<Integer>) result2.getData()) {
                sendAlert(id,"Someone buy from your store! go check it out");
            }
            return new Result(true,"send successfully alerts\n");
        }
        catch (Exception e)
        {
            return new Result(false,"Exception while sending msg\n");
        }

    }

    public Result sendAlert(int userId, String msg) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "ALERT");
            json.put("data", msg);
            getUserById(userId).addNotification(json.toString());
            return new Result(true,"send successfully alerts\n");
        }
        catch (Exception e)
        {
            return new Result(false,"Exception while sending msg\n");
        }

    }

    public void setSessionDemo(int userId) {
        getUserById(userId).setSessionDemo();
    }



    private void AppointSystemManager(User systemManager) {
        systemManager.appointSystemManager(this.stores.getAllStores());
    }

    public Result getUserIdByName(String userName) {

//        for(User user : users){
//            if(user.getUserName().equals(userName)){
//                return new Result(true,user.getId());
//            }
//        }
        return new Result(false, "User Name not exist");
    }


    public Result register(String userName, int age, String pass) {
        if(userAuth.register(userName,pass)){
            int userId=userCounter.inc();
            users.add(new User(userName, age,  userId, true));
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
            User user = getUserByName(userName);
            if(user!=null && (!user.getLogged()))
            {
                    user.setLogged(true);
                    KingLogger.logEvent("LOGIN:  User " + userName + " logged into the system.");
                    return new Result( true,user.getId());
            }
        }
        KingLogger.logEvent("LOGIN:  User " + userName + ": Username or Password not correct");
        return new Result(false, "Username or Password not correct");
    }

    private User getUserByName(String userName) {
        return users.searchUserByName(userName);
    }


    public Result notifyToSubscribers(int observableTypeId,String msg)
    {
        Result result =getObservableTypeById(observableTypeId);
        if(result.isResult())
        {
            ObservableType o = (ObservableType) result.getData();
            o.sendAll(msg);
            return new Result(true,"msg send susccefully");
        }
        return new Result(false,result.getData());
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
            ObservableType Observable = (ObservableType) result.getData();
            this.observers.remove(Observable);
            return new Result(true,"Observable remove successfully");
        }

        return new Result(false,result.getData());
    }

    public Result subscribeToObservable(int observableId,int userId)
    {
        Result result =getObservableTypeById(observableId);
        if(result.isResult())
        {
            ObservableType Observable = (ObservableType) result.getData();
            Observable.addObserver(getUserById(userId));
            return new Result(true,"user subscribe successfully");
        }
        return new Result(false,result.getData());
    }

    public Result unsubscribeToObservable(int observableId,int userId)
    {
        Result result =getObservableTypeById(observableId);
        if(result.isResult() && checkValidUser(userId))
        {
            ObservableType Observable = (ObservableType) result.getData();
            Observable.deleteObserver(getUserById(userId));
            return new Result(true,"user unsubscribe successfully");
        }
        return new Result(false,result.getData());
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
        User guest = new User("Guest", 19,  userCounter.inc(), false);
        guest.setLogged(true);
        users.add(guest);
        int id = guest.getId();
        KingLogger.logEvent("GUEST_LOGIN: Guest logged into the system with id: " + id);
        return new Result(true,id);
    }

    public Result isLogged(int userId) {
        User user = getUserById(userId);
        if (user != null)
            return new Result(true,user.isLooged());
        return new Result(false,"User not exist");
    }


    public Result guestRegister (int userId, String userName, String password){
        try {
            if(userAuth.guestRegister(userName,password)){
                getUserById(userId).setRegistered(false);
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
        if (user == null || !user.isLooged()|| !user.isRegistered()) {
            KingLogger.logEvent("LOGOUT: User " + userId + ": User has not logged in");
            return new Result(false,"User has not logged in");
        }
        user.setLogged(false);
        KingLogger.logEvent("LOGOUT: User " + user.getUserName() + " logged out of the system.");
        return new Result(true,true);
    }

    public User getUserById(int userId) {
       return users.get(userId);
    }

    public Result getNumOfUsers(){
         return new Result(true,users.size());
    }

    public Result getAllStoresInfo(int userId) {
        User u = getUserById(userId);
        if (u != null && u.isLooged()) {
            return new Result(true,this.stores) ;
        }
        KingLogger.logEvent("GET_ALL_STORES_INFO: User with id " + userId + " tried to get stores info while logged out and failed.");
        return new Result(false,"User has not logged in");
    }
    public String getAllStoresNames(int userId) {
        User u = getUserById(userId);
        StringBuilder storesNames = new StringBuilder();
        if (u != null &&  u.getLogged()) {
            for(Store store : this.stores.getAllStores())
                storesNames.append(store.getName()+",");
            storesNames.deleteCharAt(storesNames.length()-1);
        }
        KingLogger.logEvent("GET_ALL_STORES_NAME: User with id " + userId + " get stores info.");
        return storesNames.toString();
    }




    public Result getProducts(Filter filter, int userId){
        try{
            if(getUserById(userId).isLooged()) {
                Map<Integer, Integer> output = new HashMap<>();
                List<Store> stores=this.stores.getAllStores();
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
                if (getUserById(userId).isLooged()){
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
            if (getUserById(userId) != null && getUserById(userId).isLooged()) {
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


    public Result buyProducts(int userId, int storeId, Map<String, String> paymentData, Map<String, String> supplementData) {
        try {
            Map<Product, Integer> products = getBag(userId, storeId);
            Store store = getStoreById(storeId);
            Map<Product, Integer> productsAmountBag = new HashMap<>();
            Map<Product,Double> offerPrices = getOfferPrices(userId,storeId);
            for (Product p : products.keySet()) {
                productsAmountBag.put(p, products.get(p));
            }
            Bag bag = new Bag(store);
            bag.setProducts(products);
            Map<Product, Integer> productsAmountBuy = new HashMap<>();
            double totalCost = 0;
            for (Product product : productsAmountBag.keySet()) {
                synchronized (product) {
                    if (store.validatePurchasePerProduct(product, getUserById(userId), new Date(), bag)) {
                        if (store.canBuyProduct(product, productsAmountBag.get(product))) {
                            store.removeProductAmount(product, productsAmountBag.get(product));
                            productsAmountBuy.put(product, productsAmountBag.get(product));
                            if(offerPrices.containsKey(product)) {
                                totalCost += ((offerPrices.get(product) - store.calcDiscountPerProduct(product, new Date(), getUserById(userId), bag)) * products.get(product));
                            }
                            else
                                totalCost += ((product.getPrice() - store.calcDiscountPerProduct(product, new Date(), getUserById(userId), bag)) * products.get(product));
                        }
                    } else
                        return new Result(false, "Purchase is not approved by store's policy.");
                }
            }
            if (!validatePaymentDetails(paymentData)) {
                store.abortPurchase(productsAmountBuy);
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false, "Payment details are invalid.");
            }
            if (!validateSupplementDetails(supplementData)) {
                store.abortPurchase(productsAmountBuy);
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false, "Supplement details are invalid.");
            }
            Result paymentResult = paymentAdapter.pay(paymentData);
            Result supplementResult = supplementAdapter.supply(supplementData);
            if (!paymentResult.isResult()
                    || ((int) paymentResult.getData()) > 100000
                    || ((int) paymentResult.getData()) < 10000) {
                store.abortPurchase(productsAmountBuy);
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false, "Payment has failed.");
            }
            if (!supplementResult.isResult()
                    || ((int) supplementResult.getData()) > 100000
                    || ((int) supplementResult.getData()) < 10000) {
                store.abortPurchase(productsAmountBuy);
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                return new Result(false, "Supplement has failed.");
            }
            getUserById(userId).removeProductFromCart(productsAmountBuy, storeId);
            Receipt rec = new Receipt(receiptCounter.inc(), storeId, userId, getUserById(userId).getUserName(), productsAmountBuy, (int) paymentResult.getData(), (int) supplementResult.getData());
            rec.setTotalCost(totalCost);
            this.receipts.add(rec);
            store.addReceipt(rec);
            getUserById(userId).addReceipt(rec);
            if (productsAmountBag.size() == productsAmountBuy.size()) {
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " made purchase in store " + storeId);
                notifyToSubscribers(getStoreById(storeId).getNotificationId(), "Some one buy from your store! you can go to your purchase to see more details");
            } else {
                KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " made purchase in store " + storeId + "but some products are missing");
            }
            return new Result(true, rec.getId());
        } catch (Exception e) {
            KingLogger.logError("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
            return new Result(false, "purchase failed");
        }
    }

    private boolean validatePaymentDetails(Map<String, String> paymentData) {
        return Integer.parseInt(paymentData.get("month")) > 0
                && Integer.parseInt(paymentData.get("month")) < 13
                && Integer.parseInt(paymentData.get("year")) >= 2021
                && Integer.parseInt(paymentData.get("year")) < 2035
                && paymentData.get("cvv").length() == 3
                && Integer.parseInt(paymentData.get("cvv")) > 0;
        //Not validating length of credit card number & holder id for easy testing
    }

    private boolean validateSupplementDetails(Map<String, String> supplementData) {
        return supplementData.get("zip").length() == 7
                && Integer.parseInt(supplementData.get("zip")) > 0;
    }

    private Map<Product, Integer> getBag(int userId, int storeId) {
        try {
            User user = getUserById(userId);
            Bag bag = user.getBagByStoreId(storeId);
            KingLogger.logEvent("GET_BAG: User with id " + userId + " view his bag from store " + storeId);
            return bag.getProductsAmounts();
        }
        catch (Exception e) {
            KingLogger.logError("GET_BAG: User with id " + userId + " couldn't view his bag from store " + storeId);
            return null;
        }
    }

    private Map<Product, Double> getOfferPrices(int userId, int storeId) {
        try {
            User user = getUserById(userId);
            Bag bag = user.getBagByStoreId(storeId);
            KingLogger.logEvent("GET_BAG: User with id " + userId + " view his offer prices from store " + storeId);
            return bag.getOfferPrices();
        }
        catch (Exception e) {
            KingLogger.logError("GET_BAG: User with id " + userId + " couldn't view his offer prices from store " + storeId);
            return null;
        }
    }

    public Result addProductToStore(int userId, int storeId , String name, List<String> categories, double price, String description, int quantity) {
        User user =getUserById(userId);
        Store store= getStoreById(storeId);
        int productId = productCounter.inc();
        if (user.addProductToStore(productId,store,name, categories, price, description, quantity)){
            KingLogger.logEvent("ADD_PRODUCT_TO_STORE: User with id " + userId + " add product to store " + storeId);
            return new Result(true, productId);
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
           // systemManager.addStoreToSystemManager(store);
            user.openStore(store);
            this.stores.add(store);

            KingLogger.logEvent("OPEN_STORE: User with id " + userId + " open the store " + storeName);
            Result result = addObservable(storeName);
            int subscribeId = (int)result.getData();
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
            KingLogger.logEvent("ADD_STORE_OWNER: User with id " + owner + " try to add store owner and" + result.getData());
            subscribeToObservable(getStoreById(storeId).getNotificationId(),userId);
            sendAlert(userId,"You are now owner in store: "+getStoreName(storeId));
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
            sendAlert(userId,"You are now manager in store: "+ getStoreName(storeId));
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

    public Result removeManager(int ownerId, int managerId, int storeId) {
        User user = getUserById(ownerId);
        Store store = getStoreById(storeId);
        Result result = user.removeManagerFromStore(getUserById(managerId),store);
        if(result.isResult())
        {
            KingLogger.logEvent("REMOVE_MANAGER: User with id " + ownerId + " remove manager and " + result.getData());
            unsubscribeToObservable(getStoreById(storeId).getNotificationId(),managerId);
            sendAlert(managerId,"You are no longer manager in store: "+ getStoreName(storeId));
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
            sendAlert(managerId,"You are no longer owner in store: "+ getStoreName(storeId));
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
        return stores.getById(storeId);
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
        return new Result(true,stores.getAllStores().size());
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
        return getStoreById(storeId).getName();

    }

    public Product getProductById(Integer productId) {
        for(Store store:stores.getAllStores()){
            if(store.getProductById(productId)!=null){
                return store.getProductById(productId);
            }
        }
        return null;
    }

    private Product getProductByName(String productName) {
        for (Store store : stores.getAllStores()) {
            if (store.getProductByName(productName) != null) {
                return store.getProductByName(productName);
            }
        }
        return null;
    }

    public Result addDiscountPolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, String beginStr, String endStr, int percentage, String mathOp) {
        Store st = getStoreById(storeId);
        Date begin = this.stringToDate(beginStr);
        Date end = this.stringToDate(endStr);
        if(st != null && st.prodExists(prodId) && percentage > 0 && percentage <= 100 && end.after(new Date())) {
            Discount.MathOp op = Discount.MathOp.SUM;
            if(mathOp.equals("Max"))
                op = Discount.MathOp.MAX;
            if (policiesParams == null || policiesParams.size() == 0) {
                return getUserById(userId).addDiscountOnProduct(st, "simple", "PRODUCT", prodId, begin, end, null, percentage, op);
            }
            else {
                DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addDiscountPolicy(pol);
                }
                setDiscountOperator(operator, conditions);
                return getUserById(userId).addDiscountOnProduct(st, "complex", "PRODUCT", prodId, begin, end, conditions, percentage, op);
            }
        }
        return new Result(false, "Could not add discount policy.");
    }

    public Result addDiscountPolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String beginStr, String endStr, int percentage, String mathOp) {
        Date begin = this.stringToDate(beginStr);
        Date end = this.stringToDate(endStr);
        Store st = getStoreById(storeId);
        if(st != null && percentage > 0 && percentage <= 100 && end.after(new Date())) {
            Discount.MathOp op = Discount.MathOp.SUM;
            if(mathOp.equals("Max"))
                op = Discount.MathOp.MAX;
            if (policiesParams == null || policiesParams.size() == 0) {
                return getUserById(userId).addDiscountOnCategory(st, "simple", "CATEGORY", category, begin, end, null, percentage, op);
            }
            else {
                DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addDiscountPolicy(pol);
                }
                setDiscountOperator(operator, conditions);
                return getUserById(userId).addDiscountOnCategory(st, "complex", "CATEGORY", category, begin, end, conditions, percentage, op);
            }
        }
        return new Result(false, "Could not add discount policy.");
    }

    public Result addDiscountPolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String beginStr, String endStr, int percentage, String mathOp) {
        Date begin = this.stringToDate(beginStr);
        Date end = this.stringToDate(endStr);
        Store st = getStoreById(storeId);
        if(st != null && percentage > 0 && percentage <= 100 && end.after(new Date())) {
            Discount.MathOp op = Discount.MathOp.SUM;
            if(mathOp.equals("Max"))
                op = Discount.MathOp.MAX;
            if (policiesParams == null || policiesParams.size() == 0) {
                return getUserById(userId).addDiscountOnStore(st, "simple", "STORE", begin, end, null, percentage, op);
            }
            else {
                DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addDiscountPolicy(pol);
                }
                setDiscountOperator(operator, conditions);
                return getUserById(userId).addDiscountOnStore(st, "complex", "STORE", begin, end, conditions, percentage, op);
            }
        }
        return new Result(false, "Could not add discount policy.");
    }

    public Result addPurchasePolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        Store st = getStoreById(storeId);
        if(st != null && st.prodExists(prodId))  {
                PurchaseCondition conditions = new PurchaseCondition();
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addPurchasePolicy(pol);
                }
                setPurchaseOperator(operator, conditions);
                return getUserById(userId).addPurchaseOnProduct(st,"PRODUCT", prodId, conditions);
        }
        return new Result(false, "Could not add purchase policy.");
    }

    public Result addPurchasePolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {
        Store st = getStoreById(storeId);
        if(st != null) {
                PurchaseCondition conditions = new PurchaseCondition();
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addPurchasePolicy(pol);
                }
                setPurchaseOperator(operator, conditions);
                return getUserById(userId).addPurchaseOnCategory(st, "CATEGORY", category, conditions);
            }
        return new Result(false, "Could not add discount policy.");
    }

    public Result addPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        Store st = getStoreById(storeId);
        if(st != null) {
                PurchaseCondition conditions = new PurchaseCondition();
                for (Pair<String, List<String>> pair: policiesParams) {
                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                    conditions.addPurchasePolicy(pol);
                }
                setPurchaseOperator(operator, conditions);
                return getUserById(userId).addPurchaseOnStore(st, "STORE", conditions);
        }
        return new Result(false, "Could not add discount policy.");
    }


    private void setDiscountOperator(String operator, DiscountCondition conditions) {
        switch (operator) {
            case "And":
                conditions.setOperator(new AndOperator());
                break;
            case "Or":
                conditions.setOperator(new OrOperator());
                break;
            case "Xor":
                conditions.setOperator(new XorOperator());
                break;
            default:
                conditions.setOperator(new NoneOperator());
                break;
        }
    }

    private void setPurchaseOperator(String operator, PurchaseCondition conditions) {
        switch (operator) {
            case "And":
                conditions.setOperator(new AndOperator());
                break;
            case "Or":
                conditions.setOperator(new OrOperator());
                break;
            case "Xor":
                conditions.setOperator(new XorOperator());
                break;
            default:
                conditions.setOperator(new NoneOperator());
                break;
        }
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
        return new Result(false,"user isn't exist");
    }
    public Result getLoginMessagesQueue (int userId) {
        if(getUserById(userId)!=null)
        {
            return new Result(true,getUserById(userId).getLoginMessages());
        }
        return new Result(false,"user isn't exist");
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
        for(Store store: stores.getAllStores()){
            if(store.getProductById(prodId)!=null){
                return store.getProductAmount(prodId);
            }
        }
        return -1;
    }

    public Result getUserPermissionsMap(int ownerId, String managerName, int storeId) {
        Result result=getUserIdByName(managerName);
        if(result.isResult()){
            int userId= (int)result.getData();
            result= getUserPermissions(userId,storeId);
            if(result.isResult()){
                List<String> permissionsNames=(List<String>) result.getData();
                Map<String,Boolean> permissionsBool=new HashMap<>();
                permissionsBool.put("AddProduct",permissionsNames.contains("AddProduct"));
                permissionsBool.put("AppointManager",permissionsNames.contains("AppointManager"));
                permissionsBool.put("RemoveManagerAppointmen",permissionsNames.contains("RemoveManagerAppointmen"));
                permissionsBool.put("AppointOwner",permissionsNames.contains("AppointOwner"));
                permissionsBool.put("RemoveOwnerAppointmen",permissionsNames.contains("RemoveOwnerAppointmen"));
                permissionsBool.put("EditProduct",permissionsNames.contains("EditProduct"));
                permissionsBool.put("ViewPurchaseHistory",permissionsNames.contains("ViewPurchaseHistory"));
                permissionsBool.put("GetWorkersInfo",permissionsNames.contains("GetWorkersInfo"));
                permissionsBool.put("DefineDiscountPolicy",permissionsNames.contains("DefineDiscountPolicy"));
                permissionsBool.put("DefinePurchasePolicy",permissionsNames.contains("DefinePurchasePolicy"));


                return new Result(true,permissionsBool);
            }else{
                return result;
            }
        }else{
            return result;
        }
    }

    public Result getUserPermissions(int id, int storeId) {
        List<String> names = new LinkedList<>();
        for (TradingSystem.Permission permission : TradingSystem.Permission.values()) {
            if (checkPermissions(id, storeId, permission.ordinal())) {
                names.add(permissionsName[permission.ordinal()]);
            }
        }
        return new Result(true, names);
    }

    public Result getReceipt(int receiptId) {
        Receipt r = receipts.get(receiptId);
        if(r!=null)
            return new Result(true, r);
        return new Result(false, "No receipt with this user id and store.");
    }

    public Result editDiscountPolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        if(getUserById(userId).removeDiscountOnProduct(getStoreById(storeId), prodId, null).isResult())
            return addDiscountPolicyOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
        return new Result(false, "Could ont edit discount on product.");
    }

    public Result editDiscountPolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        if(getUserById(userId).removeDiscountOnCategory(getStoreById(storeId), -1, category).isResult()) {
            return addDiscountPolicyOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
        }
        return new Result(false, "Could ont edit discount on category.");
    }

    public Result editDiscountPolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        if(getUserById(userId).removeDiscountOnStore(getStoreById(storeId), -1, null).isResult()) {
            return addDiscountPolicyOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
        }
        return new Result(false, "Could ont edit discount on store.");
    }

    public Result editPurchasePolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        if(getUserById(userId).removePurchaseOnProduct(getStoreById(storeId), prodId, null).isResult())
            return addPurchasePolicyOnProduct(storeId, userId, prodId, operator, policiesParams);
        return new Result(false, "Could ont edit purchase on product.");
    }

    public Result editPurchasePolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {
        if(getUserById(userId).removePurchaseOnCategory(getStoreById(storeId), -1, category).isResult())
            return addPurchasePolicyOnCategory(storeId, userId, category, operator, policiesParams);
        return new Result(false, "Could ont edit purchase on category.");
    }

    public Result editPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        if(getUserById(userId).removePurchaseOnStore(getStoreById(storeId), -1, null).isResult())
            return addPurchasePolicyOnStore(storeId, userId, operator, policiesParams);
        return new Result(false, "Could ont edit purchase on store.");
    }



    public Result getDiscountOnProduct(int storeId, int userId, int prodId) {
        return getUserById(userId).getDiscountOnProduct(getStoreById(storeId), prodId);
    }

    public Result getDiscountOnCategory(int storeId, int userId, String category) {
        return getUserById(userId).getDiscountOnCategory(getStoreById(storeId), category);
    }

    public Result getDiscountOnStore(int storeId, int userId) {
        return getUserById(userId).getDiscountOnStore(getStoreById(storeId), userId);
    }


    public Result getPurchaseOnProduct(int storeId, int userId, int prodId) {
        return getUserById(userId).getPurchaseOnProduct(getStoreById(storeId), prodId);
    }

    public Result getPurchaseOnCategory(int storeId, int userId, String category) {
        return getUserById(userId).getPurchaseOnCategory(getStoreById(storeId), userId, category);
    }

    public Result getPurchaseOnStore(int storeId, int userId) {
        return getUserById(userId).getPurchaseOnStore(getStoreById(storeId), userId);
    }

    public Result removeDiscountPolicy(int storeId, int userId, int prodId, String category) {
        if(getStoreById(storeId) != null && getStoreById(storeId).prodExists(prodId))
            return getUserById(userId).removeDiscountPolicy(getStoreById(storeId), prodId, category);
        return new Result(false, "Product id does not exist in this store.");
    }

    public Result removePurchasePolicy(int storeId, int userId, int prodId, String category) {
        if(getStoreById(storeId) != null && getStoreById(storeId).prodExists(prodId))
            return getUserById(userId).removePurchasePolicy(getStoreById(storeId), prodId, category);
        return new Result(false, "Product id does not exist in this store.");
    }

    private Date stringToDate(String date) {
        String[] parts = date.split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(parts[2]));
        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
        return cal.getTime();
    }

    public Result cancelPayment(String transactionId) {
        Result cancelPaymentResult = paymentAdapter.cancelPayment(Integer.parseInt(transactionId));
        if(cancelPaymentResult.isResult() && (Integer)cancelPaymentResult.getData() == 1)
            return new Result(true, "Payment was canceled successfully.");
        return new Result(false, "Could not cancel payment.");
    }

    public Result cancelSupplement(String transactionId) {
        Result cancelSupplementResult = supplementAdapter.cancelSupplement(Integer.parseInt(transactionId));
        if(cancelSupplementResult.isResult() && (Integer)cancelSupplementResult.getData() == 1)
            return new Result(true, "Supplement was canceled successfully.");
        return new Result(false, "Could not cancel payment.");
    }

    public Result cancelPurchase(int receiptId) {
        Receipt receipt = (Receipt)getReceipt(receiptId).getData();
        int paymentTransaction = receipt.getPaymentTransactionId();
        int supplementTransaction = receipt.getSupplementTransactionId();
        Result cancelPayResult = cancelPayment(String.valueOf(paymentTransaction));
        if(cancelPayResult.isResult()) {
            Result cancelSupplyResult = cancelSupplement(String.valueOf(supplementTransaction));
            if(cancelSupplyResult.isResult()) {
                Store st = getStoreById(receipt.getStoreId());
                Map<Product, Integer> purchaseBag = new HashMap<>();
                for(ReceiptLine rLine : receipt.getLines()) {
                    purchaseBag.put(st.getProductByName(rLine.getProdName()), rLine.getAmount());
                }
                st.abortPurchase(purchaseBag);
                this.receipts.delete(receipt.getId());
                st.removeReceipt(receipt);
                getUserById(receipt.getUserId()).removeReceipt(receipt);
                KingLogger.logEvent("CANCEL_PURCHASE: purchase that was made with receipt id " + receiptId + " was canceled.");
                return new Result(true, "Purchase was canceled successfully.");
            }
            return cancelSupplyResult;
        }
        else
            return cancelPayResult;
    }

}
