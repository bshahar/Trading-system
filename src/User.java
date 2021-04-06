import java.util.LinkedList;
import java.util.List;

public class User {
    private Registered registered = null;
    private List<Bag> bags;
    private String userName;
    private boolean looged;
    private int id;


    public User(String userName, int id,int registered) {
        if(registered == 1)
            this.registered = new Registered();
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.id = id;
        this.looged = true;
    }

    public Bag getBagByStoreId(int storeId){
        for (Bag bag : bags){
            if (bag.getStoreId() == storeId)
                return bag;
        }
        return null;
    }

    public void setRegistered() {
        this.registered = new Registered();
    }

    public void setLooged(boolean looged) {
        this.looged = looged;
    }

    public boolean isLooged() {
        return looged;
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

    public void setBags(List<Bag> bags) {
        this.bags = bags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
