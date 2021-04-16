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

    public static int registerLogin(String username, String password){
        return tradingSystem.login(username, password) ;
    }

    public static boolean registerLogout(int userId){
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







}
