package Service;

import Domain.*;
import Interface.TradingSystem;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


    public static Result addProduct(int userId, int storeId, String name, List<Product.Category> categories, double price, String description, int amount){
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
        registerId1= (int)registeredLogin(userName1,password1).getdata();
        registerId2=(int) registeredLogin(userName2,password2).getdata();
        registerId3= (int)registeredLogin(userName3,password3).getdata();
        storeId1=(int )openStore(registerId1,"kandabior store").getdata();
        storeId2=(int)openStore(registerId1,"elad store").getdata();
        LinkedList<Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        int productId1=(int ) addProduct(registerId1, storeId1,"Milk",catList ,10,"FOOD", 10 ).getdata();

        int productId2=(int) addProduct(registerId1, storeId1,"Meat",catList ,40,"FOOD", 2 ).getdata();

        int productId3= (int)addProduct(registerId1, storeId1,"Banana",catList ,4,"FOOD", 20 ).getdata();
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
