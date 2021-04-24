package Service;

import Domain.*;
import Interface.TradingSystem;


import java.util.List;
import java.util.Map;

public class API {

    private static TradingSystem tradingSystem;
    public static void initTradingSystem(String userName){

        User sysManager= new User(userName,0,1);
        tradingSystem=new TradingSystem(sysManager);

    }

    public static int guestLogin(){
        return tradingSystem.guestLogin();
    }

    public static List<Store> getAllStoreInfo(int userId){
        return tradingSystem.getAllStoresInfo(userId);
    }
    public static String getAllStoreNames(int userId){
        return tradingSystem.getAllStoresNames(userId);
    }

    public static Map<Integer,Integer> searchProduct(Filter filter, int userId){
        return tradingSystem.getProducts(filter,userId);
    }

    public static boolean addProductToCart(int userId, int storeId, int productId, int amount){
        return tradingSystem.addProductToBag(userId,storeId,productId,amount);
    }

    public static List<Bag> getCart(int userId){
        return tradingSystem.getCart(userId);
    }

    public static boolean buyProduct(int userId, int storeId, String creditInfo){
        return tradingSystem.buyProducts(userId,storeId,creditInfo);
    }

    public static int registeredLogin(String username, String password){
        return tradingSystem.login(username, password) ;
    }

    public static boolean registeredLogout(int userId){
        return tradingSystem.logout(userId) ;
    }

    public static int openStore(int userId,String storeInfo){
        return tradingSystem.openStore(userId, storeInfo);
    }

    public static List<Receipt> getPurchaseHistory(int userId){
        return tradingSystem.getUserPurchaseHistory(userId);
    }

    public static int addProduct(int userId, int storeId, String name, List<Product.Category> categories, double price, String description, int amount){
        return tradingSystem.addProductToStore(userId,storeId,name,categories,price,description,amount);
    }

    public static boolean removeProductFromStore(int userId, int storeId, int prodId){
        return tradingSystem.removeProductFromStore(userId,storeId,prodId);
    }

    public static boolean addStoreOwner(int owner, int userId, int storeId){
        return tradingSystem.addStoreOwner(owner, userId, storeId);
    }

    public static boolean addStoreManager(int owner, int userId, int storeId){
        return tradingSystem.addStoreManager(owner, userId, storeId);
    }

    public static boolean addManagerPermissions(){
        //TODO
        return false;
    }
    public static boolean removeManagerPermissions(){
        //TODO
        return false;
    }

    public static boolean removeManager(int ownerId,int managerId,int storeId){
        return tradingSystem.removeManager(ownerId, managerId, storeId);
    }

    public static List<User> getStoreWorkers(int ownerId,int storeId){
        return tradingSystem.getWorkersInformation(ownerId, storeId);
    }

    public static List<Receipt> getStorePurchaseHistory(int ownerId,int storeId){
        return tradingSystem.getStorePurchaseHistory(ownerId,storeId);
    }

    public static List<Receipt> getGlobalPurchaseHistory(int tradingSystemManager){
        return tradingSystem.getAllPurchases(tradingSystemManager);
    }


    public static int register(String userName, String password) {
        return tradingSystem.register(userName, password);
    }

    public static boolean isLogged(int userId) {
        return tradingSystem.isLogged(userId);

    }

    public static int getNumOfUsers() {

        return tradingSystem.getNumOfUsers();
    }

    public static int guestRegister(int guestId, String userName, String password) {
        return tradingSystem.guestRegister(guestId, userName, password);
    }

    public static List<Product> getAllStoreProducts(int storeId) {
        return tradingSystem.getProductsFromStore(storeId);
    }

    public static int getNumOfStores() {
        return tradingSystem.getNumOfStores();

    }

    public static boolean isRegister(int userId)
    {
        return tradingSystem.isRegister(userId);
    }

    public static List<Receipt> getUserPurchaseHistory(int registerId1) {
        return tradingSystem.getUserPurchaseHistory(registerId1);
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
        String userName1="elad@gmail.com";
        String password1= "123";
        String userName2="elad";
        String password2= "elad321654";
        String userName3="erez";
        String password3= "erez321654";
        register(userName1,password1);
        register(userName2,password2);
        register(userName3,password3);
        registerId1= registeredLogin(userName1,password1);
        registerId2= registeredLogin(userName2,password2);
        registerId3= registeredLogin(userName3,password3);
        storeId1=openStore(registerId1,"kandabior store");
        storeId2=openStore(registerId1,"elad store");
        registeredLogout(registerId1);
    }

    public static List<Store>  getMyStores(int id) {
       return tradingSystem.getMyStores(id);
    }

    public static List<Permission> getPermissionsOfStore(int userId , int storeId)
    {
        return tradingSystem.getPermissionsOfStore(userId,storeId);
    }
}
