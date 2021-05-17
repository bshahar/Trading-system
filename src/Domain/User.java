package Domain;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchaseFormat.Purchase;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.Sessions.DemoSession;
import Domain.Sessions.SessionInterface;
import Domain.Sessions.realSession;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class User implements Observer {
    private int registered ;
    private List<Bag> bags;
    private String userName;
    private int age;
    private boolean logged;
    private int id;
    private Member member;
    private List<Receipt> receipts;
    private Queue<String> messages;
    private ObservableType observableType ;
    private boolean isSystemManager;
    private SessionInterface session;

    private Queue<String> loginMessages;


    public User(String userName, int age, int id,int registered) {
        this.registered = registered;
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.age = age;
        this.id = id;
        this.logged = false;
        this.member = new Member();
        this.receipts=new LinkedList<>();
        this.messages = new ConcurrentLinkedDeque<>();
        this.loginMessages = new ConcurrentLinkedDeque<>();
        this.isSystemManager = false;
        this.session = new realSession();
    }

    public boolean isRegistered()
    {
        return (1==this.registered);
    }

    public Member getMember() {
        return member;
    }

    public Bag getBagByStoreId(int storeId){
        for (Bag bag : bags){
            if (bag.getStoreId() == storeId)
                return bag;
        }

        return null;
    }

    public void setRegistered() {
        this.registered = 1;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public List<Bag> getBags() {
        return bags;
    }

    public String getUserName() {
        return userName;
    }


    public String toString()
        {
            StringBuilder info = new StringBuilder();
            info.append("Domain.User Name: ");
            info.append(this.userName);
            info.append(" Age: ");
            info.append(this.age);
            info.append(" ID: ");
            info.append(this.id);
            return info.toString();
    }

    public void createNewBag(Store store, int prodId, int amount) {
        Bag b = new Bag(store);
        b.addProduct(store.getProductById(prodId),amount);
        this.bags.add(b);
    }

    public void setName(String userName) {
        this.userName=userName;
    }

    public void openStore(Store store) {
        this.member.openStore(this,store);
    }

    public void addStoreToSystemManager(Store store) {
        this.member.addStoreToSystemManager(store);
    }

    public void removeStoreToSystemManager(Store store) {
        this.member.removeStoreToSystemManager(store);
    }

    public Result addStoreOwner(User owner, User user, Store store) {
        return this.member.addStoreOwner(owner,user,store);
    }
    public void updateOwnerPermission(Store store)
    {
        this.member.updateOwnerPermission(store);
    }

    public Result addStoreManager(User user, Store store) {
        return this.member.addStoreManager(this,user,store);
    }

    public void updateManagerPermission(Store store) {
         this.member.updateManagerPermission(store);
    }

    public Result addPermissions(User user, Store store, List<Integer> opIndexes) {
        return this.member.addPermissions(user,store,opIndexes);
    }

    public void updateMyPermissions(Store store, List<Integer> opIndexes) {
        this.member.updateMyPermissions(store,opIndexes);
    }

    public Result removePermissions(User user, Store store, List<Integer> opIndexes) {
        return this.member.removePermissions(user,store,opIndexes);
    }
    public void disableMyPermissions(Store store ,List<Integer> opIndexes )
    {
        this.member.disableMyPermissions(store,opIndexes);
    }

    public Result getWorkersInformation(Store store) {
        return this.member.getWorkersInformation(store);
    }

    public Result getStorePurchaseHistory(Store store) {
        return this.member.getStorePurchaseHistory(store);
    }

    public boolean addProductToStore(int productId,Store store, String name, List<String> categories, double price, String description, int quantity) {
        return member.addProductToStore(productId,store,name, categories, price, description, quantity);
    }

    public Result removeProductFromStore(Store store, int productId) {
        return member.removeProductFromStore(store,productId);
    }

    public Result removeManagerFromStore(User manager, Store store) {
        return member.removeMangerFromStore(this,manager,store);
    }

    public boolean addReceipt(Receipt receipt){
        return receipts.add(receipt);
    }

    public List<Receipt> getPurchaseHistory() {
        return receipts;
    }

    public List<Store> getMyStores() {
        return this.member.getMyStores();
    }
    public void addToMyStores(Store store)
    {
        this.member.addToMyStores(store);
    }

    public void removeFromMyStores(Store store)
    {
        this.member.removeFromMyStores(store);
    }

    public List<Permission> getPermissionsOfStore(int storeId) {
        return this.member.getPermissionsOfStore(storeId);
    }

    public boolean checkPermissions(Store store ,int permissionId) {
        if(member != null)
            return member.checkPermissions(store,permissionId);
        return false;
    }



    @Override
    public void update(Observable observable, Object arg)
    {
        observableType = (ObservableType) observable;
        if(logged)
        {
            loginMessages.add(observableType.getMessage());
        }
        else
            messages.add(observableType.getMessage());
    }

    public Queue<String> getMessages()
    {
        Queue<String> new_megs;
        new_megs = this.messages;
        this.messages = new ConcurrentLinkedDeque<>();
        return new_megs;
    }

    public Queue<String> getLoginMessages()
    {
        Queue<String> new_megs;
        new_megs = ((DemoSession)this.session).getMsgs();
        this.loginMessages = new ConcurrentLinkedDeque<>();
        return new_megs;
    }



    public void addNotification(String msg){
        if(!logged)
        {
            JSONObject jo = new JSONObject(msg);
            String data = jo.get("data").toString();
            this.messages.add(data);
        }
        else
           session.send(msg);
    }
    public void setSession(Session s)
    {
        this.session.set(s);
    }
    public void setSessionDemo()
    {
        this.session = new DemoSession();
    }


    public Result removeOwnerFromStore(User owner, Store store) {
        return member.removeOwnerFromStore(this,owner,store);
    }

    public void removeProductFromCart(Map<Product, Integer> productsAmountBuy,int storeId) {
        for(Bag bag: bags){
            if(bag.getStoreId()==storeId){
                for(Product product : productsAmountBuy.keySet()){
                    bag.removeProduct(product);
                    if(bag.getProdNum()==0){
                        removeBag(bag);
                    }
                }
            }
        }

    }

    public void removeBag(Bag b) {
        bags.remove(b);
    }

    public Result editProduct(Store store, Product product,int price,int amount) {
        return member.editProduct(store,product,price,amount);
    }

    public Result addDiscountOnProduct(Store store, String condition, String param, int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        return member.addDiscountPolicy(store, condition, param, null, prodId, begin, end, conditions, percentage, op);
    }

    public Result removeDiscountOnProduct(Store store, int prodId, String category) {
        return member.removeDiscountPolicy(store, prodId, category);
    }

    public Result removeDiscountOnCategory(Store store, int prodId, String category) {
        return member.removeDiscountPolicy(store, prodId, category);
    }

    public Result removeDiscountOnStore(Store store, int prodId, String category) {
        return member.removeDiscountPolicy(store, prodId, category);
    }

    public Result addDiscountOnCategory(Store store, String condition, String param, String category, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        return member.addDiscountPolicy(store, condition, param, category, -1, begin, end, conditions, percentage, op);
    }

    public Result addDiscountOnStore(Store store, String condition, String param, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        return member.addDiscountPolicy(store, condition, param, null, -1, begin, end, conditions, percentage, op);
    }


    public Result getDiscountOnProduct(Store store, int prodId) {
        return member.getDiscountPolicies(store,prodId, "");
    }

    public Result getDiscountOnCategory(Store store, String category) {
        return member.getDiscountPolicies(store, -1, category);
    }

    public Result getDiscountOnStore(Store store, int userId) {
        return member.getDiscountPolicies(store,-1, "");
    }

    public Result addPurchaseOnProduct(Store store, String param, int prodId, PurchaseCondition conditions) {
        return member.addPurchasePolicy(store, param,null,prodId,conditions);
    }

    public Result removePurchaseOnProduct(Store store, int prodId, String category) {
        return member.removePurchasePolicy(store, prodId,category);
    }

    public Result removePurchaseOnCategory(Store store, int prodId, String category) {
        return member.removePurchasePolicy(store, prodId, category);
    }

    public Result removePurchaseOnStore(Store store, int prodId, String category) {
        return member.removePurchasePolicy(store, prodId, category);
    }

    public Result addPurchaseOnCategory(Store store, String param, String category, PurchaseCondition conditions) {
        return member.addPurchasePolicy(store, param, category, -1,conditions);
    }

    public Result addPurchaseOnStore(Store store, String param, PurchaseCondition conditions) {
        return member.addPurchasePolicy(store, param, null, -1,conditions);
    }


    public Result getPurchaseOnProduct(Store store, int prodId) {
        return member.getPurchasePolicy(store,prodId, "");
    }

    public Result getPurchaseOnCategory(Store store, int userId, String category) {
        return member.getPurchasePolicy(store, -1, category);
    }

    public Result getPurchaseOnStore(Store store, int userId) {
        return member.getPurchasePolicy(store,-1, "");
    }


    public Result removeDiscountPolicy(Store store, int prodId, String category) {
        return member.removeDiscountPolicy(store, prodId, category);
    }

    public Result removePurchasePolicy(Store store, int prodId, String category) {
        return this.member.removePurchasePolicy(store, prodId, category);
    }

    public void appointSystemManager(List<Store> stores) {
        this.isSystemManager = true;
        this.member.setSystemManagerPermission(stores);
    }

    public boolean isSystemManager() {
        return isSystemManager;

    }
}
