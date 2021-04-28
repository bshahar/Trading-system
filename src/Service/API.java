package Service;

import Domain.*;
import Interface.TradingSystem;


import java.util.LinkedList;
import java.util.List;

public class API {

    private static TradingSystem tradingSystem;
    public static void initTradingSystem(String userName){

        User sysManager= new User(userName,0,1);
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

    public static Result buyProduct(int userId, int storeId, String creditInfo){
        return tradingSystem.buyProducts(userId,storeId,creditInfo);
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


    public static Result register(String userName, String password) {
        return tradingSystem.register(userName, password);
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
        registerId1= (int)register(userName1,password1).getdata();
        registerId2= (int) register(userName2,password2).getdata();
        registerId3= (int)register(userName3,password3).getdata();

        registerId1= (int)registeredLogin(userName1,password1).getdata();
        storeId1=(int )openStore(registerId1,"kandabior store").getdata();
        storeId2=(int)openStore(registerId1,"elad store").getdata();
        addStoreOwner(registerId1,registerId2,storeId1);

        LinkedList<String> catList= new LinkedList<>();
        catList.add("FOOD");
        addProduct(registerId1, storeId1,"Milk",catList ,10,"FOOD", 10 ).getdata();
        addProduct(registerId1, storeId1,"Meat",catList ,40,"FOOD", 2 ).getdata();
        addProduct(registerId1, storeId1,"Banana",catList ,4,"FOOD", 20 ).getdata();
        addProduct(registerId1, storeId2,"Water",catList ,5,"DRINK", 13 ).getdata();
        registeredLogout(registerId1);

        registerId3= (int)registeredLogin(userName3,password3).getdata();
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


    public static Result getUserPemissions(int id, int storeId) {
        List<String> names=new LinkedList<>();
        for(Permission permission : Permission.values()){
            if(checkPermissions(id,storeId,permission.ordinal())){
                names.add(permissionsName[permission.ordinal()]);
            }
        }
        return new Result(true,names);


    }
}
