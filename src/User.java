import java.util.LinkedList;
import java.util.List;

public class User {
    private State state;
    private List<Bag> bags;
    private String userName;
    private boolean looged;
    private int id;


    public User(String userName, int id) {
        this.state = new State();
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.id = id;
        this.looged = true;
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

    public State getState() {
        return state;
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

    public void setState(State state) {
        this.state = state;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
