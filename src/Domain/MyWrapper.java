package Domain;

import Interface.TradingSystem;
import Persistance.HibernateUtil;
import Persistance.ReceiptsEntity;

import Persistance.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyWrapper {


    List<User> users;


    private Object value;
    private boolean testing;
    private boolean updatedValue=false;
    private TradingSystem ts;

    public MyWrapper(Object obj,int testing){
        value =obj;
        this.testing = testing==1;
        this.users = Collections.synchronizedList(new LinkedList<>());
    }

    public Object get(String dbName) {
        if (!updatedValue) {
            switch(dbName){
                case "receipt": {
                    Session session2 = HibernateUtil.getSessionFactory().openSession();
                    session2.beginTransaction();
                    ReceiptsEntity rec2 = new ReceiptsEntity();
                    String str = "FROM Receipts";
                    Query query = session2.createQuery(str);
                    this.value = query.list();
                    //TODO insert the receipt line into the value receiptline
                    session2.close();
                    break;
                }
            }
            updatedValue = true;
        }
        return value;
    }

    public boolean add(Store store){
        List<Store> list= (List<Store>) value;
        list.add(store);

        return false;
    }



    public boolean add(Receipt receipt){
        List<Receipt> list= (List<Receipt>) receipt;
        list.add(receipt);
        //adding to db
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ReceiptsEntity rec = new ReceiptsEntity();
        rec.setId(receipt.getReceiptId());
        rec.setStoreId(receipt.getStoreId());
        rec.setUserId(receipt.getUserId());
        rec.setUserName(receipt.getUserName());
        rec.setTotalCost(receipt.getTotalCost());
        session.save(rec);
        //TODO for receiptLine
        session.getTransaction().commit();
        session.close();

        return true;
    }
    //User
    public boolean add(User user){
        if(testing) {
            this.users.add(user);
            return true;
        }
        else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            Integer userId = user.getId();
            session.getTransaction().commit();
            return userId > 0;
        }
    }

    public Object getOne(String name,int id) {
        switch (name) {
            case "user": {
                if(testing)
                    return getUserById(id);

                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                return (User) session.get(User.class, id);
            }
        }
        return null;
    }
    public boolean remove(String name , int id) {
        switch (name) {
            case "user": {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                User userToDelete = new User();
                userToDelete.setId(id);
                session.delete(userToDelete);
                session.getTransaction().commit();
            }
        }
        return true;
    }
    public User searchUserByName(String name) {
        if(testing)
            return getUserByName(name);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria crit = session.createCriteria(User.class);
        crit.add(Restrictions.eq("userName","Elad"));
        if(crit.list().size()>0)
            return (User)crit.list().get(0);
        else
            return null;
    }

    private User getUserById(int id)
    {
        for(User user : users)
        {
            if(user.getId() == id)
                return user;
        }
        return null;
    }
    private User getUserByName(String name)
    {
        for(User user : users)
        {
            if(user.getUserName() == name)
                return user;
        }
        return null;
    }


    public Object size() {
        if(testing)
            return users.size();
        else
        {
            //TODO!
            return 0;
        }
    }
}
