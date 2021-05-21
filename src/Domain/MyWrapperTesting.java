package Domain;

import Interface.TradingSystem;
import Persistance.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyWrapperTesting implements MyWrapperInterface {
    List<User> users;
    List<Receipt> receipts;

    private Object value;
    private boolean testing;
    private boolean updatedValue=false;
    private TradingSystem ts;

    public MyWrapperTesting(Object obj){
        value =obj;
        this.users = Collections.synchronizedList(new LinkedList<>());
        this.receipts = Collections.synchronizedList(new LinkedList<>());
    }


    @Override
    public Object get(String dbName) {
        return null;
    }


    @Override
    public boolean add(Store store) {
        return false;
    }

    @Override
    public boolean add(Receipt receipt) {
        return false;
    }

    @Override
    public boolean add(User user) {
            this.users.add(user);
            return true;
    }

    @Override
    public Object getOne(String name, int id) {
        return getUserById(id);
    }

    @Override
    public boolean remove(String name, int id) {
        return false;
    }

    @Override
    public User searchUserByName(String name) {
        return getUserByName(name);
    }

    @Override
    public User getUserById(int id) {
        for(User user : users)
        {
            if(user.getId() == id)
                return user;
        }
        return null;
    }

    @Override
    public User getUserByName(String name) {
        for(User user : users)
        {
            if(user.getUserName() == name)
                return user;
        }
        return null;
    }

    @Override
    public Object size() {
        return users.size();
    }
}
