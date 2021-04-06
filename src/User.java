import java.util.LinkedList;
import java.util.List;

public class User {
    private int registered ;
    private List<Bag> bags;
    private String userName;
    private boolean logged;
    private int id;


    public User(String userName, int id,int registered) {
        this.registered = registered;
        this.bags = new LinkedList<>();
        this.userName = userName;
        this.id = id;
        this.logged = true;
    }

    public boolean isRegistered()
    {
        return (1==this.registered);
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
            info.append("User Name: ");
            info.append(this.userName);
            info.append(" ID: ");
            info.append(this.id);
            return info.toString();
    }
}
