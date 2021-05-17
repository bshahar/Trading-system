package Service;

import Domain.*;
import Interface.TradingSystem;
import Persistance.User;
import javafx.util.Pair;
import org.eclipse.jetty.websocket.api.Session;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class API {

    private static TradingSystem tradingSystem;

    public static void initTradingSystem() throws IOException {

        Properties appProps = new Properties();

        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.properties");
        if(input != null)
            appProps.load(input);
        else
            throw new FileNotFoundException("Property file was not found.");

        String sysManagerName = appProps.getProperty("systemManagerName");
        String sysManagerId = appProps.getProperty("systemManagerId");
        String sysManagerAge = appProps.getProperty("systemManagerAge");
        String testing = appProps.getProperty("test");
        User sysManager = new User(sysManagerName, Integer.parseInt(sysManagerAge), Integer.parseInt(sysManagerId), 1);
        tradingSystem = new TradingSystem(sysManager,Integer.parseInt(testing));

        /*
        //String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //String appConfigPath = rootPath + "appConfig.properties";

        //Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
            String sysManagerName = appProps.getProperty("systemManagerName");
            String sysManagerId = appProps.getProperty("systemManagerId");
            String sysManagerAge = appProps.getProperty("systemManagerAge");
            User sysManager = new User(sysManagerName, Integer.parseInt(sysManagerAge), Integer.parseInt(sysManagerId), 1);
            tradingSystem = new TradingSystem(sysManager);
            return true;
        } catch (IOException e) {
            return false;
        }

         */
    }

    public static Result guestLogin(){
        return tradingSystem.guestLogin();
    }

    public static Result getAllStoreInfo(int userId){
        return tradingSystem.getAllStoresInfo(userId);
    }
    public static String getAllStoreNames(int userId){
        return tradingSystem.getAllStoresNames(userId);
    }

    public static Result searchProduct(Filter filter, int userId){
        return tradingSystem.getProducts(filter,userId);
    }

    public static Result addProductToCart(int userId, int storeId, int productId, int amount){
        return tradingSystem.addProductToBag(userId,storeId,productId,amount);
    }

    public static Result getCart(int userId){
        return tradingSystem.getCart(userId);
    }

    public static Result buyProduct(int userId, int storeId, String creditInfo) {
        return tradingSystem.buyProducts(userId, storeId, creditInfo);
    }

    public static Result registeredLogin(String username, String password){
        return tradingSystem.login(username, password) ;
    }

    public static Result registeredLogout(int userId){
        return tradingSystem.logout(userId) ;
    }

    public static Result openStore(int userId,String storeInfo){
        return tradingSystem.openStore(userId, storeInfo);
    }


    public static Result addProduct(int userId, int storeId, String name, List<String> categories, double price, String description, int amount){
        return tradingSystem.addProductToStore(userId,storeId,name,categories,price,description,amount);
    }

    public static Result removeProductFromStore(int userId, int storeId, int prodId){
        return tradingSystem.removeProductFromStore(userId,storeId,prodId);
    }

    public static Result addStoreOwner(int owner, int userId, int storeId){
        return tradingSystem.addStoreOwner(owner, userId, storeId);
    }

    public static Result addStoreManager(int owner, int userId, int storeId){
        return tradingSystem.addStoreManager(owner, userId, storeId);
    }


    public static Result removeManager(int ownerId,int managerId,int storeId){
        return tradingSystem.removeManager(ownerId, managerId, storeId);
    }

    public static Result getStoreWorkers(int ownerId,int storeId){
        return tradingSystem.getWorkersInformation(ownerId, storeId);
    }

    public static Result getStorePurchaseHistory(int ownerId,int storeId){
        return tradingSystem.getStorePurchaseHistory(ownerId,storeId);
    }

    public static Result getGlobalPurchaseUserHistory(int tradingSystemManager, int userId){  //system manager
            return tradingSystem.getGlobalPurchaseUserHistory(tradingSystemManager, userId);
    }

    public static Result getGlobalPurchaseStoreHistory(int tradingSystemManager, int storeId){  //system manager
        return getStorePurchaseHistory(tradingSystemManager, storeId);
    }


    public static Result register(String userName, String password, int age) {
        return tradingSystem.register(userName, age, password);
    }

    public static Result isLogged(int userId) {
        return tradingSystem.isLogged(userId);

    }

    public static Result getNumOfUsers() {

        return tradingSystem.getNumOfUsers();
    }

    public static Result guestRegister(int guestId, String userName, String password) {
        return tradingSystem.guestRegister(guestId, userName, password);
    }

    public static Result getAllStoreProducts(int storeId) {

        return tradingSystem.getProductsFromStore(storeId);
    }

    public static Result getNumOfStores() {
        return tradingSystem.getNumOfStores();

    }

    public static boolean isRegister(int userId)
    {
        return tradingSystem.isRegister(userId);
    }

    public static Result getUserPurchaseHistory(int registerId1) {
        return tradingSystem.getUserPurchaseHistory(registerId1);
    }
    public static Result getAllPurchases(int systemManager){
        return tradingSystem.getAllPurchases(systemManager);
    }

    public static Result getManagersAndOwnersOfStore(int storeId) {
        return tradingSystem.getManagersAndOwnersOfStore(storeId);
    }
    public static Result addSession(int userId, Session session) {
        return tradingSystem.addSession(userId,session);
    }
    public static Result removeSession(int userId) {
        return tradingSystem.removeSession(userId);
    }
    public static Result sendAlertsAfterPurchase(int storeId) {
        return tradingSystem.sendAlertsAfterPurchase(storeId);
    }
    public static Result sendAlert(int userId,String msg) {
        return tradingSystem.sendAlert(userId,msg);
    }
    public static void setSessionDemo(int userId)
    {
        tradingSystem.setSessionDemo(userId);
    }




    public static void forTest()
    {
        int registerId1;
        int registerId2;
        int registerId3;
        //guests
        int guestId1;
        int guestId2;
        //stores
        int storeId1;
        int storeId2;
        String userName1="elad";
        String password1= "123";
        String userName2="or";
        String password2= "123";
        String userName3="erez";
        String password3= "123";
        registerId1= (int)register(userName1,password1,20).getData();
        registerId2= (int) register(userName2,password2,20).getData();
        registerId3= (int)register(userName3,password3,20).getData();
        register("or1" ,password3,20);
        register("or2",password3,20);
        register("or3",password3,20);
        register("or4",password3,20);
        register("or5",password3,20);
        register("or6",password3,20);
        registerId1= (int)registeredLogin(userName1,password1).getData();
        storeId1=(int )openStore(registerId1,"kandabior store").getData();
        storeId2=(int)openStore(registerId1,"elad store").getData();
        addStoreOwner(registerId1,4,storeId1);
        addStoreOwner(registerId1,5,storeId1);
        addStoreOwner(registerId1,6,storeId1);
        addStoreOwner(registerId1,7,storeId1);
        addStoreOwner(registerId1,8,storeId1);
        addStoreOwner(registerId1,9,storeId1);


        LinkedList<String> catList= new LinkedList<>();
        catList.add("FOOD");
        LinkedList<String> catList2= new LinkedList<>();
        catList.add("FOOD2");
        addProduct(registerId1, storeId1,"Milk",catList2 ,10,"FOOD", 10 ).getData();
        addProduct(registerId1, storeId1,"Meat",catList ,40,"FOOD2", 2 ).getData();
        addProduct(registerId1, storeId1,"Banana",catList ,4,"Hello", 20 ).getData();
        addProduct(registerId1, storeId2,"Water",catList2 ,5,"FOOD", 13 ).getData();
        registeredLogout(registerId1);

        registerId3= (int)registeredLogin(userName3,password3).getData();
        addProductToCart(registerId3,1,1,2);
        addProductToCart(registerId3,2,2,2);
        addProductToCart(registerId3,2,4,2);
        buyProduct(registerId3,storeId1,"123456789");
        buyProduct(registerId3,storeId2,"123456789");
        registeredLogout(registerId3);


    }

    public static List<Store>  getMyStores(int id) {
       return tradingSystem.getMyStores(id);
    }



    public static String getStoreName(int storeId) {
        return tradingSystem.getStoreName(storeId);

    }

    public static Product getProductById(Integer productId) {
        return tradingSystem.getProductById(productId);
    }

    public static boolean checkPermissions(int userId ,int storeId ,int permissionId) {
        return tradingSystem.checkPermissions(userId,storeId,permissionId);
    }

    public static Result addPermissions(int ownerId ,int userId ,int storeId , List<Integer> opIndexes) {
        return tradingSystem.addPermissions(ownerId,userId,storeId,opIndexes);
    }

    public static Result RemovePermissions(int ownerId ,int userId ,int storeId , List<Integer> opIndexes) {
        return tradingSystem.removePermission(ownerId,userId,storeId,opIndexes);
    }
    public static Result notifyToSubscribers(int observableTypeId,String msg)
    {
        return tradingSystem.notifyToSubscribers(observableTypeId,msg);
    }
    public static Result addObservable(String name)
    {
        return tradingSystem.addObservable(name);
    }

    public static Result removeObservable(int observableTypeId)
    {
        return tradingSystem.removeObservable(observableTypeId);
    }

    public static Result subscribeToObservable(int observableId,int userId)
    {
        return tradingSystem.subscribeToObservable(observableId,userId);
    }

    public static Result unsubscribeToObservable(int observableId,int userId)
    {
        return tradingSystem.unsubscribeToObservable(observableId,userId);
    }

    public static Result getMessagesQueue(int userId)
    {
        return tradingSystem.getMessagesQueue(userId);
    }

    public static Result getLoginMessagesQueue(int userId)
    {
        return tradingSystem.getLoginMessagesQueue(userId);
    }

    public static Result getMessagesQueueAsArray(int userId)
    {
        return tradingSystem.getMessagesQueueAsArray(userId);
    }


    public static Result getNotificationIdByStoreId(int storeId)
    {
        return tradingSystem.getNotificationIdByStoreId(storeId);
    }

    public static Result getUserIdByName(String userName) {
        return tradingSystem.getUserIdByName(userName);
    }

    public static Result removeOwner(int ownerId, int userId, int storeId) {
        return tradingSystem.removeOwner(ownerId,userId,storeId);
    }

    public static Result editProduct(int userId, int storeId,int productId ,int price, int amount) {
        return tradingSystem.editProduct(userId, storeId, productId,price,amount);
    }

    public static int getProductAmount(Integer prodId) {
        return tradingSystem.getProductAmount(prodId);
    }

    public static Result getUserPermissionsMap(int ownerId, String managerName, int storeId) {
        return tradingSystem.getUserPermissionsMap(ownerId,managerName,storeId);
    }





    public static Result getUserPemissions(int id, int storeId) {
        return tradingSystem.getUserPermissions(id,storeId);


    }

    public static Result addDiscountOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.addDiscountPolicyOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result addDiscountPolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.addDiscountPolicyOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result addDiscountPolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.addDiscountPolicyOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result addPurchasePolicyOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.addPurchasePolicyOnProduct(storeId, userId, prodId, operator, policiesParams);
    }

    public static Result addPurchasePolicyOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.addPurchasePolicyOnCategory(storeId, userId, category, operator, policiesParams);
    }

    public static Result addPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.addPurchasePolicyOnStore(storeId, userId, operator, policiesParams);
    }


    public static Result getReceipt(int receiptId) {
        return tradingSystem.getReceipt(receiptId);
    }

    public static List<Integer> getpermissionsIndex(List<String> indexes) {
        return tradingSystem.getpermissionsIndex(indexes);
    }



    public static Result removeDiscountPolicy(int storeId, int userId, int prodId, String category) {
        return tradingSystem.removeDiscountPolicy(storeId, userId, prodId, category);
    }

    public static Result removePurchasePolicy(int storeId, int userId, int prodId, String category) {
        return tradingSystem.removePurchasePolicy(storeId, userId, prodId, category);
    }

    public static Result editDiscountOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.editDiscountPolicyOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
    }


    public static Result editDiscountOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.editDiscountPolicyOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result editDiscountOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, String begin, String end, int percentage, String mathOp) {
        return tradingSystem.editDiscountPolicyOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result editPurchaseOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.editPurchasePolicyOnProduct(storeId, userId, prodId, operator, policiesParams);
    }


    public static Result editPurchaseOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.editPurchasePolicyOnCategory(storeId, userId, category, operator, policiesParams);
    }

    public static Result editPurchaseOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.editPurchasePolicyOnStore(storeId, userId, operator, policiesParams);
    }

    public static Result getDiscountOnProduct(int storeId, int userId, int prodId) {
        return tradingSystem.getDiscountOnProduct(storeId, userId, prodId);
    }

    public static Result getDiscountOnCategory(int storeId, int userId, String category) {
        return tradingSystem.getDiscountOnCategory(storeId, userId, category);
    }

    public static Result getDiscountOnStore(int storeId, int userId) {
        return tradingSystem.getDiscountOnStore(storeId, userId);
    }

    public static Result getPurchaseOnProduct(int storeId, int userId, int prodId) {
        return tradingSystem.getPurchaseOnProduct(storeId, userId, prodId);
    }

    public static Result getPurchaseOnCategory(int storeId, int userId, String category) {
        return tradingSystem.getPurchaseOnCategory(storeId, userId, category);
    }

    public static Result getPurchaseOnStore(int storeId, int userId) {
        return tradingSystem.getPurchaseOnStore(storeId, userId);
    }


    public static boolean isSystemManager(int userId) {
        return tradingSystem.isSystemManager(userId);
    }
}
