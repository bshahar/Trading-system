package Domain;

import Permissions.Permission;

import java.util.LinkedList;
import java.util.List;

public class User {
    private int registered ;
    private List<Bag> bags;
    private String userName;
    private boolean logged;
    private int id;
    private Member member;


    public User(String userName, int id,int registered) {
        this.registered = registered;
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.id = id;
        this.logged = true;
        this.member = new Member();
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
        this.member.openStore(store);
    }

    public boolean addStoreOwner(User user, Store store) {
        return this.member.addStoreOwner(user,store);
    }
    public void updateOwnerPermission(Store store)
    {
        this.member.updateOwnerPermission(store);
    }

    public boolean addStoreManager(User user, Store store) {
        return this.member.addStoreManager(user,store);
    }

    public void updateManagerPermission(Store store) {
         this.member.updateManagerPermission(store);
    }
}
