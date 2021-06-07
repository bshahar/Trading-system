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
import Domain.Sessions.DemoSession;
import Domain.Sessions.SessionInterface;
import Domain.Sessions.realSession;
import Domain.User;
import Persistence.*;
import Persistence.DAO.AdminTableDAO;
import Persistence.DAO.CounterDAO;
import Service.*;
import com.j256.ormlite.misc.TransactionManager;
import javafx.util.Pair;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class TradingSystem {

    private static counter userCounter;
    private static counter storeCounter;
    private static counter productCounter;
    private static counter receiptCounter;
    private static counter observableCounter;
    private static counter conditionCounter;
    private static counter offerCounter;
    private static counter policyCounter;

    public static Map<Integer , DemoSession> demoSessionMap ;
    private static PaymentInterface paymentAdapter;
    private static SupplementInterface supplementAdapter;
    private UserAuth userAuth;
    private StoreWrapper stores;
    private User systemManager;
    private ReceiptWrapper receipts;
    private UserWrapper users;
    private List<ObservableType> observers;
    public static Map<Integer , SessionInterface> sessionsMap ;
    public static Map<Integer , Session> adminSessionMap ;
    private CounterWrapper counterWrapper;
    private AdminTableWrapper adminTable;

    //LiveStat for systemManager
    private int GuestsCounter;
    private int NormalUsersCounter;
    private int ManagersCounter;
    private int OwnersCounter;
    private LocalDate CurrentDate;


    public TradingSystem (User systemManager, String externalSystemsUrl, boolean testing) {
   if(testing) {
            paymentAdapter = new DemoPayment();
            supplementAdapter = new DemoSupplement();
        }else{
            paymentAdapter = new PaymentAdapter(externalSystemsUrl);
            supplementAdapter = new SupplementAdapter(externalSystemsUrl);

        }
        //paymentAdapter = new PaymentAdapter(externalSystemsUrl);
        //supplementAdapter = new SupplementAdapter(externalSystemsUrl);
        this.users =  new UserWrapper();
        this.receipts =  new ReceiptWrapper();

        this.stores = new StoreWrapper();
        this.adminTable= new AdminTableWrapper();
        this.userAuth = new UserAuth();
        userAuth.register(systemManager.getUserName(), "123");
        users.add(systemManager);

        initiateCounters();
        initiateStats();
        this.counterWrapper = new CounterWrapper();
        this.observers = Collections.synchronizedList(new LinkedList<>());
        this.systemManager = systemManager;
        AppointSystemManager(systemManager);
        sessionsMap = new ConcurrentHashMap<>();
        adminSessionMap= new HashMap<>();
        demoSessionMap = new ConcurrentHashMap<>();
        KingLogger.logEvent("Trading System initialized");

    }

    private void initiateStats() {
        AdminTableWrapper AdminWrapper=new AdminTableWrapper();
        AdminTableDAO adminTableDAO= AdminWrapper.get(java.time.LocalDate.now());
        if(adminTableDAO == null)
        {
            AdminWrapper.add(java.time.LocalDate.now());
            GuestsCounter = 0;
            ManagersCounter =0;
            NormalUsersCounter=0;
            OwnersCounter = 0;
        }
        else {
            GuestsCounter = adminTableDAO.getGuestsCounter();
            ManagersCounter = adminTableDAO.getManagers();
            NormalUsersCounter = adminTableDAO.getNormalUsers();
            OwnersCounter = adminTableDAO.getOwners();
        }
    }

    public void loadScenario() throws Exception {
        //TODO here we need to write the json function parsing.
        org.json.simple.JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("resources\\requestedTests.json");
            jsonObject = (org.json.simple.JSONObject) jsonParser.parse(reader);
        }

        catch(Exception e){
            KingLogger.logError("LOAD_SCENARIO: json file was not found.");
            throw new FileNotFoundException("json file was not found.");
        }

        JSONArray registerArray = (JSONArray)jsonObject.get("register");
        for (int i = 0; registerArray != null && i< registerArray.size(); i++){
            String userName = (String) ((JSONObject)registerArray.get(i)).get("username");
            String password = (String) ((JSONObject)registerArray.get(i)).get("password");
            int age = Math.toIntExact((long)((JSONObject)registerArray.get(i)).get("age"));
            API.register(userName,password,age);
        }

        JSONArray loginArray = (JSONArray)jsonObject.get("login");
        for (int i = 0;loginArray != null && i< loginArray.size(); i++){
            String userName = (String) ((JSONObject)loginArray.get(i)).get("username");
            String password = (String) ((JSONObject)loginArray.get(i)).get("password");
            API.registeredLogin(userName,password);
        }

        JSONArray openStoreArray = (JSONArray)jsonObject.get("openStore");
        for (int i = 0;openStoreArray != null && i< openStoreArray.size(); i++){
            int userOwnerId = Math.toIntExact((long)((JSONObject)openStoreArray.get(i)).get("userOwnerId"));
            String storeName = (String) ((JSONObject)openStoreArray.get(i)).get("storeName");
            API.openStore(userOwnerId,storeName);
        }

        JSONArray addProduct = (JSONArray)jsonObject.get("addProduct");
        for (int i = 0;addProduct != null && i< addProduct.size(); i++){
            int storeOwnerId = Math.toIntExact((long)((JSONObject)addProduct.get(i)).get("storeOwnerId"));
            int storeId = Math.toIntExact((long)((JSONObject)addProduct.get(i)).get("storeId"));
            String name = (String) ((JSONObject)addProduct.get(i)).get("name");
            List <String> categories = new LinkedList();
            JSONArray catList = (JSONArray)((JSONObject)addProduct.get(i)).get("categories");
            for (int j = 0; j < catList.size(); j++){
                categories.add((String)catList.get(j));
            }
            int price = Math.toIntExact((long)((JSONObject)addProduct.get(i)).get("price"));
            String description = (String) ((JSONObject)addProduct.get(i)).get("description");
            int quantity = Math.toIntExact((long)((JSONObject)addProduct.get(i)).get("quantity"));
            API.addProduct(storeOwnerId, storeId, name,categories, price, description, quantity);
        }

        JSONArray addStoreManager = (JSONArray)jsonObject.get("addStoreManager");
        for(int i = 0;addStoreManager != null && i< addStoreManager.size(); i++){
            int appointerUserId = Math.toIntExact((long)((JSONObject)addStoreManager.get(i)).get("appointerUserId"));
            int appointeeUserId = Math.toIntExact((long)((JSONObject)addStoreManager.get(i)).get("appointeeUserId"));


            int storeId = Math.toIntExact((long)((JSONObject)addStoreManager.get(i)).get("storeId"));
            List<Integer> permission = new LinkedList<>();
            JSONArray permissionArray = (JSONArray) ((JSONObject)addStoreManager.get(i)).get("permission");
            for(int j = 0; j < permissionArray.size(); j++){
                permission.add(Math.toIntExact((long)(permissionArray.get(j))));
            }
            if(addStoreManager(appointerUserId, appointeeUserId, storeId).isResult()){
                addPermissions(appointerUserId, appointeeUserId, storeId, permission);
            }
        }

        JSONArray logout = (JSONArray)jsonObject.get("logout");
        for(int i = 0;logout != null && i< logout.size(); i++) {
            int userId = Math.toIntExact((long)logout.get(i));
            API.registeredLogout(userId);
        }

        JSONArray removeProduct = (JSONArray)jsonObject.get("removeProduct");
        for(int i = 0;removeProduct != null && i< removeProduct.size(); i++) {
            int managerUserId = Math.toIntExact((long)((JSONObject)removeProduct.get(i)).get("managerUserId"));
            int storeId = Math.toIntExact((long)((JSONObject)removeProduct.get(i)).get("storeId"));
            int productId = Math.toIntExact((long)((JSONObject)removeProduct.get(i)).get("prodId"));
            API.removeProductFromStore(managerUserId, storeId, productId);
        }
    }

    public static counter getPolicyCounter() {
        return policyCounter;
    }

    public static counter getOfferCounter() {
        return offerCounter;
    }

    private void initiateCounters() {
        CounterWrapper counterWrapper=new CounterWrapper();
        CounterDAO counterDAO= counterWrapper.getAll();
        userCounter = new counter(counterDAO.getUserCounter(),"userCounter");
        storeCounter = new counter(counterDAO.getStoreCounter(),"storeCounter");
        productCounter=new counter(counterDAO.getProductCounter(),"productCounter");
        receiptCounter = new counter(counterDAO.getReceiptCounter(),"receiptCounter");
        conditionCounter = new counter(counterDAO.getConditionCounter(),"conditionCounter");
        offerCounter= new counter(counterDAO.getOfferCounter(),"offerCounter");
        policyCounter = new counter(counterDAO.getPolicyCounter(),"policyCounter");
        observableCounter = new counter(counterDAO.getObservableCounter(),"observableCounter");

    }


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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Store st = getStoreById(storeId);
                            if(st != null && st.prodExists(prodId))  {
                                int offerId = st.addPurchaseOffer(prodId, getUserById(userId), offer, numOfProd);
                                List<User> ownersManagers = new LinkedList<>();
                                ownersManagers.addAll(st.getOwners());
                                ownersManagers.addAll(st.getManagers());
                                for(User u:ownersManagers){
                                    sendAlert(u.getId(),"Someone has made an offer on " + getProductById(prodId).getName() +" with id " + prodId);
                                }
                                result[0] =  new Result(true, offerId);
                            }
                            else
                                result[0] =  new Result(false, -1);
                        return null;
                        }

                    });
        }catch (Exception e)
        {

        }
        return result[0];

    }






    public Result responedToOffer(int storeId, int userId, int prodId, int offerId, String responed, double counterOffer) {

        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            int informTo = getStoreById(storeId).getUserMadeTheOffer(prodId,offerId);
                            result[0] = getUserById(userId).responedToOffer(getStoreById(storeId),prodId,offerId,responed,counterOffer, "ACT");
                            if(responed.equals("APPROVED") && result[0].isResult()){
                                sendAlert(informTo,"Your offer had been approved!");
                            }
                            else if(responed.equals("DISAPPROVED") && result[0].isResult()){
                                sendAlert(informTo,"Your offer had been disapproved.");
                            }
                            else if(responed.equals("COUNTEROFFER") && result[0].isResult()){
                                sendAlert(informTo,"The store management sent you a counter offer.");
                            }
                            return null;
                        }
                    });
        }catch (Exception e)
        {
                return new Result(false,"Error!");
        }
        return result[0];

    }

    public Result respondToCounterPurchaseOffer(int storeId, int userId, int prodId, boolean approve) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Product prod = getProductById(prodId);
                            Store store = getStoreById(storeId);
                            UserCounterOffersWrapper counterOffers = new UserCounterOffersWrapper();
                            UserApprovedOffersWrapper productsApproved = new UserApprovedOffersWrapper();
                            BagWrapper productsAmounts = new BagWrapper();
                            if(approve){

                                double priceOfOffer = counterOffers.get(store, userId, prod).getPriceOfOffer();
                                int amountOfProd = counterOffers.get(store, userId, prod).getNumOfProd();
                                int userId2 =  counterOffers.get(store, userId, prod).getUser().getId();
                                productsAmounts.add(prod,amountOfProd,storeId,userId2);
                                counterOffers.remove(storeId,prod,counterOffers.get(store, userId, prod));
                                productsApproved.add(storeId,userId2, prod,priceOfOffer);
                                result[0]= new Result(true, "the counter offer has been approved");
                            }
                            else{
                                counterOffers.remove(storeId,prod,counterOffers.get(store, userId, prod));
                                result[0]= new Result(true, "the counter offer has been rejected");
                            }

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

        /*
        Bag bag = getUserById(userId).getBagByStoreId(storeId);
        if(approve){
          bag.approveCounterOffer(getProductById(prodId),storeId);
        }
        bag.rejectCounterOffer(getProductById(prodId));
        return new Result(true, "the counter offer has been responed");

         */
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

    public void addAdminSession(Session session) {
        adminSessionMap.put(0,session);
    }

    public Result loggedGuestLogin(int guestId, String userName, String password) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            //login with the given details
                            result[0]=login(userName,password);

                            //delete guest data
                            if(result[0].isResult()){
                                deleteGuest(guestId);
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    private void deleteGuest(int guestId) {
        users.delete(guestId);
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
        realSession realSession = new realSession();
        realSession.set(session);
        sessionsMap.put(userId,realSession);
        //getUserById(userId).setSession(session);
        return new Result(true,"session added\n");
    }
    public Result removeSession(int userId) {
        sessionsMap.remove(userId);
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
            User user = getUserById(userId);
            if(user!=null )
            {
                if(user.isLooged())
                    sessionsMap.get(userId).send(json.toString());
                else
                    user.addNotificationToLogOutUser(json.toString());
            }

            //getUserById(userId).addNotification(json.toString());
            return new Result(true,"send successfully alerts\n");
        }
        catch (Exception e)
        {
            return new Result(false,"Exception while sending msg\n");
        }

    }


    public void setSessionDemo(int userId) {
        sessionsMap.put(userId,new DemoSession());
        //getUserById(userId).setSessionDemo();
    }



    private void AppointSystemManager(User systemManager) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            systemManager.appointSystemManager(stores.getAllStores());

                            return null;
                        }

                    });
        }catch (Exception e)
        {
           return;
        }
        return ;

    }

    public Result getUserIdByName(String userName) {

        User user = this.users.searchUserByName(userName);
        if(user!=null)
            return new Result(true,user.getId());
        return new Result(false, "User Name not exist");
    }


    public Result register(String userName, int age, String pass) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            if(userAuth.register(userName,pass)){
                                int userId=userCounter.inc();
                                users.add(new User(userName, age,  userId, true));
                                adminTable.increaseCounter("NormalUsersCounter");
                                result[0]= new Result(true,userId);
                            }
                            else{
                                KingLogger.logEvent("REGISTER:  User " + userName + ": username or password incorrect");
                                result[0]= new Result(false,"Username or Password not correct");
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    //if the user performed login successfully return his id. else return -1
    public Result login(String userName,String pass) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            if(userAuth.loginAuthentication(userName,pass)) {
                                User user = getUserByName(userName);
                                if(user!=null && (!user.getLogged()))
                                {
                                    user.setLogged(true);
                                    KingLogger.logEvent("LOGIN:  User " + userName + " logged into the system.");
                                    updateAdminCounter(user.getId());

                                    result[0]= new Result( true,user.getId());
                                }
                            }
                            else {
                                KingLogger.logEvent("LOGIN:  User " + userName + ": Username or Password not correct");
                                result[0] = new Result(false, "Username or Password not correct");
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    private void updateAdminCounter(int id) {
        List<Store> tempStores = this.stores.getAllStores();
        boolean owner = false;
        boolean manager = false;
        for(Store store : tempStores)
        {
            List<User> Owners = store.getOwners();
            List<User> Managers = store.getManagers();
            for(User CurrentOtowner : Owners)
            {
                if(CurrentOtowner.getId() == id)
                {
                    owner = true;
                    break;
                }
            }
            for(User CurrentManager : Managers)
            {
                if(CurrentManager.getId() == id)
                {
                    manager = true;
                    break;
                }
            }
        }
        if(!owner && !manager)
        {
            this.adminTable.increaseCounter("NormalUsersCounter");
            this.NormalUsersCounter++;
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("type","UPDATE_REGISTERED");
            jsonObject.put("number",NormalUsersCounter);
            try {
                adminSessionMap.get(0).getRemote().sendString(jsonObject.toString());
            }catch(Exception e){
                System.out.println(e);
            }
            return;
        }
        if(owner && !manager)
        {
            this.adminTable.increaseCounter("OwnersCounter");
            this.OwnersCounter++;
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("type","UPDATE_OWNERS");
            jsonObject.put("number",OwnersCounter);
            try {
                adminSessionMap.get(0).getRemote().sendString(jsonObject.toString());
            }catch(Exception e){
                System.out.println(e);
            }
            return;
        }
        if(!owner && manager)
        {
            this.ManagersCounter++;
            this.adminTable.increaseCounter("ManagersCounter");
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("type","UPDATE_MANAGERS");
            jsonObject.put("number",ManagersCounter);
            try {
                adminSessionMap.get(0).getRemote().sendString(jsonObject.toString());
            }catch(Exception e){
                System.out.println(e);
            }
            return;
        }
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
        final int[] id = new int[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User guest = new User("Guest", 19,  userCounter.inc(), false);
                            guest.setLogged(true);
                            users.add(guest);
                            id[0] = guest.getId();
                            adminTable.increaseCounter("GuestsCounter");
                            GuestsCounter++;
                            JSONObject jsonObject= new JSONObject();
                            jsonObject.put("type","UPDATE_GUESTS");
                            jsonObject.put("number",GuestsCounter);
                            try {
                                adminSessionMap.get(0).getRemote().sendString(jsonObject.toString());
                            }catch(Exception e){
                                System.out.println(e);
                            }
                            KingLogger.logEvent("GUEST_LOGIN: Guest logged into the system with id: " + id[0]);
                            return null;
                        }
                    });
        }catch (Exception e)
        {

        }
        return new Result(true, id[0]);

    }

    public Result getSystemManagerStats()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("GuestsCounter",GuestsCounter);
        jsonObject.put("NormalUsersCounter",NormalUsersCounter);
        jsonObject.put("ManagersCounter",ManagersCounter);
        jsonObject.put("OwnersCounter",OwnersCounter);
        return new Result(true,jsonObject);
    }

    public Result isLogged(int userId) {
        User user = getUserById(userId);
        if (user != null)
            return new Result(true,user.isLooged());
        return new Result(false,"User not exist");
    }





    public Result guestRegister (int userId, String userName, String password){
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            try {
                                if(userAuth.guestRegister(userName,password)){
                                    User user= getUserById(userId);
                                    user.updateRegistered(userName);
//                getUserById(userId).setRegistered(false);
//                getUserById(userId).setName(userName);
//                getUserById(userId).setLogged(false);
                                    KingLogger.logEvent("GUEST_REGISTER: User " + userName + " registered to the system.");
                                    adminTable.increaseCounter("NormalUsersCounter");
                                    result[0]=new Result(true,userId);

                                }
                                else{
                                    KingLogger.logEvent("GUEST_REGISTER: User " + userName + ": Can't Register with given Username and PassWord");
                                    result[0]= new Result(false,"Can't Register with given Username and PassWord");
                                }
                            }
                            catch (Exception e) {
                                KingLogger.logError("GUEST_REGISTER: User " + userName + ": failed registering to the system.");
                                result[0]= new Result(false,"Can't Register with given Username and PassWord");
                            }

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User u = getUserById(userId);
                            if (u != null && u.isLooged()) {
                                result[0]= new Result(true, stores.getAllStores()) ;
                            }
                            else {

                                KingLogger.logEvent("GET_ALL_STORES_INFO: User with id " + userId + " tried to get stores info while logged out and failed.");
                                result[0]= new Result(false,"User has not logged in");
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


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
                    if( getStoreById(storeId).getInventory().prodExists(prodId,storeId)){
                        if (b != null) {
                            b.addProduct(getProductById(prodId), amount,userId);
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
                b.removeProduct(getProductById(prodId),userId,storeId);
                if(b.getProdNum(userId,storeId)==0){
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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            try {
                                Map<Product, Integer> products = getBag(userId, storeId);
                                Store store = getStoreById(storeId);
                                Map<Product, Integer> productsAmountBag = new HashMap<>();
                                Map<Product,Double> offerPrices = getOfferPrices(userId,storeId);
                                for (Product p : products.keySet()) {
                                    productsAmountBag.put(p, products.get(p));
                                }
                                Bag bag = new Bag(store, userId);
                                bag.setProducts(products,userId,storeId);
                                Map<Product, Integer> productsAmountBuy = new HashMap<>();
                                double totalCost = 0;
                                for (Product product : productsAmountBag.keySet()) {
                                    synchronized (product) {
                                        if (store.validatePurchasePerProduct(product, getUserById(userId), new Date(), bag)) {
                                            if (store.canBuyProduct(product, productsAmountBag.get(product))) {
                                                store.removeProductAmount(product, productsAmountBag.get(product));
                                                productsAmountBuy.put(product, productsAmountBag.get(product));
                                                if(offerContains(offerPrices, product.getId())) {
                                                    totalCost += ((offerPrice(offerPrices, product.getId()) - store.calcDiscountPerProduct(product, new Date(), getUserById(userId), bag)) * products.get(product));
                                                    UserApprovedOffersWrapper userApprovedOffersWrapper = new UserApprovedOffersWrapper();
                                                    userApprovedOffersWrapper.remove(storeId,product,userId, offerPrice(offerPrices, product.getId()));
                                                }
                                                else
                                                    totalCost += ((product.getPrice() - store.calcDiscountPerProduct(product, new Date(), getUserById(userId), bag)) * products.get(product));
                                            }
                                        } else
                                        {
                                            result[0]=new Result(false, "Purchase is not approved by store's policy.");
                                            return null;
                                        }
                                    }
                                }
                                if (!validatePaymentDetails(paymentData)) {
                                    store.abortPurchase(productsAmountBuy);
                                    KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                                    result[0] = new Result(false, "Payment details are invalid.");
                                    return null;
                                }
                                if (!validateSupplementDetails(supplementData)) {
                                    store.abortPurchase(productsAmountBuy);
                                    KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                                    result[0] =  new Result(false, "Supplement details are invalid.");
                                    return null;
                                }
                                if(totalCost>0){
                                    Result paymentResult = paymentAdapter.pay(paymentData);
                                    int paymentTransactionId = Integer.parseInt(paymentResult.getData().toString());
                                    if (!paymentResult.isResult()
                                            || paymentTransactionId > 100000
                                            || paymentTransactionId < 10000) {
                                        store.abortPurchase(productsAmountBuy);
                                        KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't make a payment in store " + storeId);
                                        result[0] =  new Result(false, "Payment has failed.");
                                        return  null;
                                    }
                                    Result supplementResult = supplementAdapter.supply(supplementData);
                                    int supplementTransactionId = Integer.parseInt(supplementResult.getData().toString());
                                    if (!supplementResult.isResult()
                                            || supplementTransactionId > 100000
                                            || supplementTransactionId < 10000) {
                                        store.abortPurchase(productsAmountBuy);
                                        KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " couldn't get a supplement in store " + storeId);
                                        result[0] =  new Result(false, "Supplement has failed.");
                                         return  null;
                                    }
                                    getUserById(userId).removeProductFromCart(productsAmountBuy, storeId);
                                    Receipt rec = new Receipt(receiptCounter.inc(), storeId, userId, getUserById(userId).getUserName(), productsAmountBuy, paymentTransactionId, supplementTransactionId);
                                    rec.setTotalCost(totalCost);
                                    receipts.add(rec);
                                    store.addReceipt(rec);
                                    getUserById(userId).addReceipt(rec);
                                    if (productsAmountBag.size() == productsAmountBuy.size()) {
                                        KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " made purchase in store " + storeId);
                                        notifyToSubscribers(getStoreById(storeId).getNotificationId(), "Some one buy from your store! you can go to your purchase to see more details");
                                    } else {
                                        KingLogger.logEvent("BUY_PRODUCTS: User with id " + userId + " made purchase in store " + storeId + "but some products are missing");
                                    }
                                    result[0]= new Result(true, rec.getId());
                                    return  null;
                                }else{
                                    result[0] =  new Result(false,"can't complete the purchase");
                                    return null;
                                }
                            } catch (Exception e) {
                                KingLogger.logError("BUY_PRODUCTS: User with id " + userId + " couldn't make a purchase in store " + storeId);
                                result[0] =  new Result(false, "purchase failed");
                                return null;
                            }

                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];





    }

    private boolean offerContains(Map<Product,Double> offers, int productId) {
        for (Product p: offers.keySet()) {
            if (p.getId() == productId)
                return true;
        }
        return false;
    }

    private double offerPrice(Map<Product,Double> offers, int productId) {
        for (Product p: offers.keySet()) {
            if (p.getId() == productId)
                return offers.get(p);
        }
        return -1;
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
            return bag.getProductsAmounts(userId);
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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User user =getUserById(userId);
                            Store store= getStoreById(storeId);
                            int productId = productCounter.inc();
                            if (user.addProductToStore(productId,store,name, categories, price, description, quantity)){
                                KingLogger.logEvent("ADD_PRODUCT_TO_STORE: User with id " + userId + " add product to store " + storeId);
                                result[0]= new Result(true, productId);
                            }
                            else{
                                KingLogger.logEvent("ADD_PRODUCT_TO_STORE: User with id " + userId + " cant add product to store " + storeId);
                                result[0]= new Result(false,"Can't add product to Store");
                            }

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }


    public Result removeProductFromStore(int userId,int storeId, int productId){
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            User user = getUserById(userId);
                            Store store = getStoreById(storeId);
                            KingLogger.logEvent("REMOVE_PRODUCT_FROM_STORE: User with id " + userId + " removed product from store " + storeId);
                            result[0]=user.removeProductFromStore(store,productId);

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    //returns the new store id
    public Result openStore(int userId, String storeName){
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User user = getUserById(userId);
                            if(user!=null && user.isRegistered()) {
                                int newId = storeCounter.inc();
                                Store store = new Store(newId, storeName,offerCounter,policyCounter);
                                // systemManager.addStoreToSystemManager(store);
                                result[0] = addObservable(storeName);
                                int subscribeId = (int)result[0].getData();
                                store.setNotificationId(subscribeId);
                                stores.add(store,user);
                                user.openStore(store,user.getId());

                                KingLogger.logEvent("OPEN_STORE: User with id " + userId + " open the store " + storeName);

                                store.setNotificationId(subscribeId);
                                subscribeToObservable(subscribeId,userId);
                                adminTable.increaseCounter("OwnersCounter");
                                OwnersCounter++;
                                result[0]= new Result(true,newId);
                            }
                            else {
                                KingLogger.logEvent("OPEN_STORE: User with id " + userId + " cant open the store " + storeName + "because is not registered");
                                result[0] = new Result(false, "User has not registered");
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }

    public Result addStoreOwner(int ownerId, int userId,int storeId) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            if (!checkValidUser(ownerId) || !checkValidUser(userId))
                                result[0] = new Result(false, "User is not valid");

                            else {
                                User owner = getUserById(ownerId);
                                User user = getUserById(userId);

                                result[0] = owner.addStoreOwner(owner, user, getStoreById(storeId));
                                if (result[0].isResult()) {
                                    KingLogger.logEvent("ADD_STORE_OWNER: User with id " + owner + " try to add store owner and" + result[0].getData());
                                    subscribeToObservable(getStoreById(storeId).getNotificationId(), userId);
                                    sendAlert(userId, "You are now owner in store: " + getStoreName(storeId));
                                }
                            }
                                return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }
    public boolean checkValidUser(int userId)
    {
        User user = getUserById(userId);
        if(user!=null && user.isRegistered())
            return true;
        return false;
    }

    public Result addStoreManager(int ownerId, int userId, int storeId){
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            if(!checkValidUser(ownerId) || !checkValidUser(userId)){
                                KingLogger.logEvent("ADD_STORE_EVENT: User with id " + ownerId + " cant appoint manager because user not valid");
                                result[0]= new Result(false,"User is not valid");
                            }
                            else {
                                result[0] = getUserById(ownerId).addStoreManager(getUserById(userId), getStoreById(storeId));
                                if (result[0].isResult()) {
                                    KingLogger.logEvent("ADD_STORE_EVENT: User with id " + ownerId + " appoint " + userId + "to be manager of store " + storeId);

                                    subscribeToObservable(getStoreById(storeId).getNotificationId(), userId);
                                    sendAlert(userId, "You are now manager in store: " + getStoreName(storeId));
                                    adminTable.increaseCounter("ManagersCounter");
                                    ManagersCounter++;

                                }
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User user = getUserById(ownerId);
                            Store store = getStoreById(storeId);
                            result[0] = user.removeManagerFromStore(getUserById(managerId),store);
                            if(result[0].isResult())
                            {
                                KingLogger.logEvent("REMOVE_MANAGER: User with id " + ownerId + " remove manager and " + result[0].getData());
                                sendAlert(managerId,"You are no longer manager in store: "+ getStoreName(storeId));
                                unsubscribeToObservable(getStoreById(storeId).getNotificationId(),managerId);

                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    public Result removeOwner(int ownerId, int managerId, int storeId){
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            User user= getUserById(ownerId);
                            Store store = getStoreById(storeId);
                             result[0] = user.removeOwnerFromStore(getUserById(managerId),store);
                            if(result[0].isResult())
                            {
                                unsubscribeToObservable(getStoreById(storeId).getNotificationId(),managerId);
                                sendAlert(managerId,"You are no longer owner in store: "+ getStoreName(storeId));
                            }
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



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
        Map<Integer , Integer> amounts= getStoreById(storeId).getInventory().getProductsAmounts(storeId);
        ProductWrapper productWrapper= new ProductWrapper();
        for(Integer productId :amounts.keySet()){
            Product product=productWrapper.getById(productId);
            products.add(product);
            product.setAmount(amounts.get(product.getId()));
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
        List<Store> out = new LinkedList<>();
        if (checkValidUser(id)) {
            for (Integer storeId : getUserById(id).getMyStores()) {
                out.add(getStoreById(storeId));
            }
            return out;
        }else {
            return new LinkedList<>();
        }
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

        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Store st = getStoreById(storeId);
                            Date begin = stringToDate(beginStr);
                            Date end = stringToDate(endStr);
                            if(st != null && st.prodExists(prodId) && percentage > 0 && percentage <= 100 && end.after(new Date())) {
                                Discount.MathOp op = Discount.MathOp.SUM;
                                if(mathOp.equals("Max"))
                                    op = Discount.MathOp.MAX;
                                if (policiesParams == null || policiesParams.size() == 0) {
                                    result[0]= getUserById(userId).addDiscountOnProduct(st, "simple", "PRODUCT", prodId, begin, end, null, percentage, op);
                                    return null;
                                }
                                else {
                                    DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                                    for (Pair<String, List<String>> pair: policiesParams) {
                                        PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                        conditions.addDiscountPolicy(pol);
                                    }
                                    setDiscountOperator(operator, conditions);
                                    result[0]= getUserById(userId).addDiscountOnProduct(st, "complex", "PRODUCT", prodId, begin, end, conditions, percentage, op);
                                    return null;
                                }
                            }
                            result[0]= new Result(false, "Could not add discount policy.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    public Result addDiscountPolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String beginStr, String endStr, int percentage, String mathOp) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Date begin = stringToDate(beginStr);
                            Date end = stringToDate(endStr);
                            Store st = getStoreById(storeId);
                            if(st != null && percentage > 0 && percentage <= 100 && end.after(new Date())) {
                                Discount.MathOp op = Discount.MathOp.SUM;
                                if(mathOp.equals("Max"))
                                    op = Discount.MathOp.MAX;
                                if (policiesParams == null || policiesParams.size() == 0) {
                                    result[0]= getUserById(userId).addDiscountOnCategory(st, "simple", "CATEGORY", category, begin, end, null, percentage, op);
                                    return null;
                                }
                                else {
                                    DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                                    for (Pair<String, List<String>> pair: policiesParams) {
                                        PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                        conditions.addDiscountPolicy(pol);
                                    }
                                    setDiscountOperator(operator, conditions);
                                    result[0]= getUserById(userId).addDiscountOnCategory(st, "complex", "CATEGORY", category, begin, end, conditions, percentage, op);
                                        return null;
                                }
                            }
                            result[0]= new Result(false, "Could not add discount policy.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    public Result addDiscountPolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String beginStr, String endStr, int percentage, String mathOp) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            Date begin = stringToDate(beginStr);
                            Date end = stringToDate(endStr);
                            Store st = getStoreById(storeId);
                            if(st != null && percentage > 0 && percentage <= 100 && end.after(new Date())) {
                                Discount.MathOp op = Discount.MathOp.SUM;
                                if(mathOp.equals("Max"))
                                    op = Discount.MathOp.MAX;
                                if (policiesParams == null || policiesParams.size() == 0) {
                                    result[0]= getUserById(userId).addDiscountOnStore(st, "simple", "STORE", begin, end, null, percentage, op);
                                    return null;
                                }
                                else {
                                    DiscountCondition conditions = new DiscountCondition(conditionCounter.inc());
                                    for (Pair<String, List<String>> pair: policiesParams) {
                                        PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                        conditions.addDiscountPolicy(pol);
                                    }
                                    setDiscountOperator(operator, conditions);
                                    result[0]= getUserById(userId).addDiscountOnStore(st, "complex", "STORE", begin, end, conditions, percentage, op);
                                    return null;
                                }
                            }
                            result[0]= new Result(false, "Could not add discount policy.");
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    public Result addPurchasePolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Store st = getStoreById(storeId);
                            if(st != null && st.prodExists(prodId))  {
                                PurchaseCondition conditions = new PurchaseCondition(conditionCounter.inc());
                                for (Pair<String, List<String>> pair: policiesParams) {
                                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                    conditions.addPurchasePolicy(pol);
                                }
                                setPurchaseOperator(operator, conditions);
                                result[0]= getUserById(userId).addPurchaseOnProduct(st,"PRODUCT", prodId, conditions);
                            return null;
                            }
                            result[0]= new Result(false, "Could not add purchase policy.");
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];



    }

    public Result addPurchasePolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {

        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            Store st = getStoreById(storeId);
                            if(st != null) {
                                PurchaseCondition conditions = new PurchaseCondition(conditionCounter.inc());
                                for (Pair<String, List<String>> pair: policiesParams) {
                                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                    conditions.addPurchasePolicy(pol);
                                }
                                setPurchaseOperator(operator, conditions);
                                result[0]= getUserById(userId).addPurchaseOnCategory(st, "CATEGORY", category, conditions);
                            return null;
                            }
                            result[0]= new Result(false, "Could not add discount policy.");
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }

    public Result addPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            Store st = getStoreById(storeId);
                            if(st != null) {
                                PurchaseCondition conditions = new PurchaseCondition(conditionCounter.inc());
                                for (Pair<String, List<String>> pair: policiesParams) {
                                    PolicyCondition pol = new PolicyCondition(pair.getKey(), pair.getValue());
                                    conditions.addPurchasePolicy(pol);
                                }
                                setPurchaseOperator(operator, conditions);
                                result[0]= getUserById(userId).addPurchaseOnStore(st, "STORE", conditions);
                                return null;
                            }
                            result[0]= new Result(false, "Could not add discount policy.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


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
        if(sessionsMap.containsKey(userId))
            return new Result(true,sessionsMap.get(userId).getMsgs());
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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            if(getUserById(userId).removeDiscountOnProduct(getStoreById(storeId), prodId, null).isResult())
                                result[0] =  addDiscountPolicyOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
                            else
                                result[0] = new Result(false, "Could ont edit discount on product.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }

    public Result editDiscountPolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            if(getUserById(userId).removeDiscountOnCategory(getStoreById(storeId), -1, category).isResult()) {
                                result[0] = addDiscountPolicyOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
                            }
                            else
                            result[0] = new Result(false, "Could ont edit discount on category.");
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }

    public Result editDiscountPolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            if(getUserById(userId).removeDiscountOnStore(getStoreById(storeId), -1, null).isResult()) {
                                result[0] = addDiscountPolicyOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
                            }
                            else
                                result[0] = new Result(false, "Could ont edit discount on store.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    public Result editPurchasePolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            if(getUserById(userId).removePurchaseOnProduct(getStoreById(storeId), prodId, null).isResult())
                                result[0]= addPurchasePolicyOnProduct(storeId, userId, prodId, operator, policiesParams);
                            else
                                result[0]= new Result(false, "Could ont edit purchase on product.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    public Result editPurchasePolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {


                            if(getUserById(userId).removePurchaseOnCategory(getStoreById(storeId), -1, category).isResult())
                                result[0]= addPurchasePolicyOnCategory(storeId, userId, category, operator, policiesParams);
                            else
                                result[0]= new Result(false, "Could ont edit purchase on category.");
                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

    }

    public Result editPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            if(getUserById(userId).removePurchaseOnStore(getStoreById(storeId), -1, null).isResult())
                                result[0]= addPurchasePolicyOnStore(storeId, userId, operator, policiesParams);
                            else
                                result[0]= new Result(false, "Could ont edit purchase on store.");

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];

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
        final Result[] result = new Result[1];
        try {
            TransactionManager.callInTransaction(DataBaseHelper.connect(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
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
                                    receipts.delete(receipt.getId());
                                    st.removeReceipt(receipt);
                                    getUserById(receipt.getUserId()).removeReceipt(receipt);
                                    KingLogger.logEvent("CANCEL_PURCHASE: purchase that was made with receipt id " + receiptId + " was canceled.");
                                    result[0]= new Result(true, "Purchase was canceled successfully.");
                                }
                                else
                                    result[0]= cancelSupplyResult;
                            }
                            else
                                result[0] = cancelPayResult;

                            return null;
                        }

                    });
        }catch (Exception e)
        {
            return new Result(false,"error!");
        }
        return result[0];


    }

}
