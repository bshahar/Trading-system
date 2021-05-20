package Domain;

import Persistance.HibernateUtil;
import Persistance.ReceiptsEntity;
import Persistance.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.List;

public interface MyWrapperInterface {



    public Object get(String dbName) ;

    public boolean add(Store store);


    public boolean add(Receipt receipt);
    //User
    public boolean add(User user);

    public Object getOne(String name,int id) ;
    public boolean remove(String name , int id) ;
    public User searchUserByName(String name) ;

    public User getUserById(int id);

    public User getUserByName(String name);
    public Object size() ;

}
