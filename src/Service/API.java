package Service;

import Domain.*;
import Interface.TradingSystem;
import javafx.util.Pair;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class API {

    private static TradingSystem tradingSystem;
    public static void initTradingSystem(String userName){

        User sysManager= new User(userName,19,0,1);
        tradingSystem=new TradingSystem(sysManager);

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

    public static Result addManagerPermissions(){
        //TODO
        return new Result(true,true);
    }
    public static Result removeManagerPermissions(){
        //TODO
        return new Result(true,true);
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

    public static Result getGlobalPurchaseHistory(int tradingSystemManager){
        return tradingSystem.getAllPurchases(tradingSystemManager);
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

        registerId1= (int)registeredLogin(userName1,password1).getData();
        storeId1=(int )openStore(registerId1,"kandabior store").getData();
        storeId2=(int)openStore(registerId1,"elad store").getData();
        addStoreOwner(registerId1,registerId2,storeId1);

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

    public static Result addDiscountOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        return tradingSystem.addDiscountOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static void addDiscountOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        tradingSystem.addDiscountOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static void addDiscountOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        tradingSystem.addDiscountOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public static Result addPurchasePolicyOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.addPurchasePolicyOnStore(storeId, userId, operator, policiesParams);
    }

    public static Result getReceipt(int receiptId) {
        return tradingSystem.getReceipt(receiptId);
    }


    public Result editDiscountOnProduct(int storeId, int userId, int prodId, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        return tradingSystem.editDiscountOnProduct(storeId, userId, prodId, operator, policiesParams, begin, end, percentage, mathOp);
    }


    public Result editDiscountOnCategory(int storeId, int userId, String category, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        return tradingSystem.editDiscountOnCategory(storeId, userId, category, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public Result editDiscountOnStore(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams, Date begin, Date end, int percentage, String mathOp) {
        return tradingSystem.editDiscountOnStore(storeId, userId, operator, policiesParams, begin, end, percentage, mathOp);
    }

    public Result editPurchasePolicy(int storeId, int userId, String operator, List<Pair<String, List<String>>> policiesParams) {
        return tradingSystem.editPurchasePolicy(storeId, userId, operator, policiesParams);
    }

    //TODO add permissions for functions below then implement
    public Result getDiscountOnProduct(int storeId, int userId, int prodId) {
        return tradingSystem.getDiscountOnProduct(storeId, userId, prodId);
    }

    public Result getDiscountOnCategory(int storeId, int userId, String category) {
        return tradingSystem.getDiscountOnCategory(storeId, userId, category);
    }

    public Result getDiscountOnStore(int storeId, int userId) {
        return tradingSystem.getDiscountOnStore(storeId, userId);
    }

    public Result getPurchasePolicy(int storeId, int userId) {
        return tradingSystem.getPurchasePolicy(storeId, userId);
    }
}
