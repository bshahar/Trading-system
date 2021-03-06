package Domain;
import Persistence.ReceiptWrapper;
import Persistence.UserMessagesWrapper;
import Persistence.BagWrapper;
import Persistence.UserWrapper;
import Persistence.MemberStorePermissionsWrapper;
import Persistence.CounterWrapper;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.Sessions.DemoSession;
import Domain.Sessions.SessionInterface;
import Domain.Sessions.realSession;
import Service.counter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class User implements Observer {
    private CounterWrapper messageCounter;
    private boolean registered ;
    private List<Bag> bags;
    private String userName;
    private int age;
    private boolean logged;
    private int id;
    private Member member;
    private List<Receipt> receipts;
    private List<String> messages;
    private ObservableType observableType ;
    private boolean isSystemManager;
    private SessionInterface session;

    private BagWrapper bagWrapper;
    private UserMessagesWrapper messagesWrapper;
    private ReceiptWrapper receiptWrapper;
    private UserWrapper userWrapper;
    private MemberStorePermissionsWrapper memberStorePermissionsWrapper;



    private Queue<String> loginMessages;

    public User()
    {

    };

    public User(String userName, int age, int id,boolean registered) {
        this.registered = registered;
        this.bags = Collections.synchronizedList(new LinkedList<>());
        this.userName = userName;
        this.age = age;
        this.id = id;
        this.logged = false;
        this.member = new Member(id);
        this.receipts= Collections.synchronizedList(new LinkedList<>());
        this.messages = Collections.synchronizedList(new LinkedList<>());
        this.loginMessages = new ConcurrentLinkedDeque<>();
        this.isSystemManager = false;
        this.session = new realSession();
        messageCounter = new CounterWrapper();
        this.bagWrapper = new BagWrapper();
        this.messagesWrapper = new UserMessagesWrapper();
        this.receiptWrapper = new ReceiptWrapper();
        this.userWrapper = new UserWrapper();
        this.memberStorePermissionsWrapper= new MemberStorePermissionsWrapper();
    }



    public void setBags(List<Bag> bags) {
        this.bags = bags;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }



    public boolean isLogged() {
        return logged;
    }



    public ObservableType getObservableType() {
        return observableType;
    }

    public SessionInterface getSession() {
        return session;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public void setMember(Member member) {
        this.member = member;
    }


    public void setObservableType(ObservableType observableType) {
        this.observableType = observableType;
    }

    public void setSystemManager(boolean systemManager) {
        isSystemManager = systemManager;
    }

    public void setSession(SessionInterface session) {
        this.session = session;
    }

    public void setLoginMessages(Queue<String> loginMessages) {
        this.loginMessages = loginMessages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public boolean getLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        if(logged)
            userWrapper.setLogged(1,id);
        else
            userWrapper.setLogged(0,id);
        this.logged = logged;
    }
    public void setUserLogged(int logged) {
        if(logged==1)
            this.logged = true;
        else
            this.logged = false;
    }



    public boolean getIsSystemManger() {
        return isSystemManager;
    }

    public void setIsSystemManger(boolean isSystemManger) {
        this.isSystemManager = isSystemManger;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.id && Objects.equals(userName, that.userName) && Objects.equals(registered, that.registered) && Objects.equals(age, that.age) && Objects.equals(logged, that.logged) && Objects.equals(isSystemManager, that.isSystemManager);
    }


    public int hashCode() {
        return Objects.hash(id, userName, registered, age, logged, isSystemManager);
    }



    public boolean isRegistered()
    {
        return (this.registered);
    }
    public Member getMember() {
        return member;
    }

    public Bag getBagByStoreId(int storeId){
        List<Bag> bags = this.bagWrapper.getAllUserBags(id);
        for(Bag bag : bags)
        {
            if(bag.getStoreId() == storeId)
                return bag;
        }
       return null;
    }



    public List<Bag> getBags() {
        return bagWrapper.getAllUserBags(id);
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
        Bag b = new Bag(store, this.getId());
        b.addProduct(store.getProductById(prodId),amount,id);
        this.bags.add(b);
    }

    public void setName(String userName) {
        this.userName=userName;
    }

    public void openStore(Store store,int userId) {
        this.member.openStore(this,store);
      //  this.memberStorePermissionsWrapper.add(this.member.getPermissions().get(store.getStoreId()), userId,store.getStoreId());
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
        //return this.memberStorePermissionsWrapper.add(user.member.getPermissions().get(store.getStoreId()), user.getId(),store.getStoreId());
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
    @Transient
    public List<Receipt> getPurchaseHistory() {
        return receiptWrapper.getByUserId(id);
    }
    @Transient
    public List<Integer> getMyStores() {
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



    @Transient
    public void update(Observable observable, Object arg)
    {
        observableType = (ObservableType) observable;
        if(userWrapper.get(id).getLogged())
        {
            loginMessages.add(observableType.getMessage());
        }
        else
        {

            int msgId = this.messageCounter.incAndGet("messageCounter");
            messagesWrapper.add(id,msgId,observableType.getMessage());
        }

    }
    @Transient
    public Queue<String> getMessages()
    {
        Queue<String> new_megs;
        new_megs = this.messagesWrapper.getByUserId(id);
        this.messagesWrapper.deleteAll(id);
        return new_megs;
    }
    @Transient
    public Queue<String> getLoginMessages()
    {
        Queue<String> new_megs;
        new_megs = ((DemoSession)this.session).getMsgs();
        this.loginMessages = new ConcurrentLinkedDeque<>();
        return new_megs;
    }


    public void addNotificationToLogOutUser(String msg){
            JSONObject jo = new JSONObject(msg);
            String data = jo.get("data").toString();
            this.messagesWrapper.add(id,messageCounter.incAndGet("messageCounter"),data);
    }

    public void addNotification(String msg){
        if(logged)
        {
            session.send(msg);
        }
        else
        {
            JSONObject jo = new JSONObject(msg);
            String data = jo.get("data").toString();
            this.messagesWrapper.add(id,messageCounter.incAndGet("messageCounter"),data);
        }
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
        for(Bag bag: bagWrapper.getAllUserBags(id)){
            if(bag.getStoreId()==storeId){
                for(Product product : productsAmountBuy.keySet()){
                    bag.removeProduct(product,id,storeId);
                    if(bag.getProdNum(id,storeId)==0){
                        removeBag(bag);
                    }
                }
            }
        }

    }

    public void removeBag(Bag b) {
        bagWrapper.deleteBag(id,b.getStoreId());
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

    public boolean isLooged() {
        return this.logged;
    }

    public boolean removeReceipt(Receipt receipt) {
        return this.receiptWrapper.delete(receipt.getId());
    }

    public Result responedToOffer(Store store, int prodId, int offerId, String responed, double counterOffer, String option) {
        return member.responedToOffer(store, prodId, offerId, responed, counterOffer, option);
    }

    public void updateRegistered(String userName) {
        userWrapper.updateRegistered(userName,id);
    }
}
