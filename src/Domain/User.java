package Domain;

import java.util.LinkedList;
import java.util.List;

public class User {
    private int registered ;
    private List<Bag> bags;
    private String userName;
    private boolean logged;
    private int id;
    private Member member;
    private List<Receipt> receipts;


    public User(String userName, int id,int registered) {
        this.registered = registered;
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.id = id;
        this.logged = true;
        this.member = new Member();
        this.receipts=new LinkedList<>();
    }

    public boolean isRegistered()
    {
        return (1==this.registered);
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
            info.append(" ID: ");
            info.append(this.id);
            return info.toString();
    }

    public void createNewBag(Store store, int prodId, int amount) {
        Bag b = new Bag(store);
        b.addProduct(prodId,amount);
        this.bags.add(b);
    }

    public void setName(String userName) {
        this.userName=userName;
    }

    public void openStore(Store store) {
        this.member.openStore(this,store);

    }

    public boolean addStoreOwner(User owner, User user, Store store) {
        return this.member.addStoreOwner(owner,user,store);
    }
    public void updateOwnerPermission(Store store)
    {
        this.member.updateOwnerPermission(store);
    }

    public boolean addStoreManager(User user, Store store) {
        return this.member.addStoreManager(this,user,store);
    }

    public void updateManagerPermission(Store store) {
         this.member.updateManagerPermission(store);
    }

    public boolean addPermissions(User user, Store store, List<Integer> opIndexes) {
        return this.member.addPermissions(user,store,opIndexes);
    }

    public void updateMyPermissions(Store store, List<Integer> opIndexes) {
        this.member.updateMyPermissions(store,opIndexes);
    }

    public boolean removePermissions(User user, Store store, List<Integer> opIndexes) {
        return this.member.removePermissions(user,store,opIndexes);
    }
    public void disableMyPermissions(Store store ,List<Integer> opIndexes )
    {
        this.member.disableMyPermissions(store,opIndexes);
    }

    public List<User> getWorkersInformation(Store store) {
        return this.member.getWorkersInformation(store);
    }

    public List<Receipt> getStorePurchaseHistory(Store store) {
        return this.member.getStorePurchaseHistory(store);
    }

    public boolean addProductToStore(int productId,Store store, String name, List<Product.Category> categories, double price, String description, int quantity) {
        return member.addProductToStore(productId,store,name, categories, price, description, quantity);
    }

    public boolean removeProductFromStore(Store store, int productId) {
        return member.removeProductFromStore(store,productId);
    }

    public boolean removeManagerFromStore(User manager, Store store) {
        return member.removeMangerFromStore(this,manager,store);
    }

    public boolean addReceipt(Receipt receipt){
        return receipts.add(receipt);
    }

    public List<Receipt> getPurchaseHistory() {
        return receipts;
    }
}