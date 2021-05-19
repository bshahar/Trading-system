package Domain;

import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountFormat.SimpleDiscount;
import Domain.DiscountPolicies.*;
import Persistance.*;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import javafx.util.Pair;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyWrapper implements MyWrapperInterface {


    List<User> users;
    List<Receipt> receipts;


    private Object value;
    private boolean updatedValue=false;
    private TradingSystem ts;

    public MyWrapper(Object obj){
        value =obj;
        this.users = Collections.synchronizedList(new LinkedList<>());
        this.receipts = Collections.synchronizedList(new LinkedList<>());
    }

    public Object getValue() { return this.value; }

    public Object get(String dbName) {
        if (!updatedValue) {
            switch(dbName){
//                case "receipt": {
//                    Session session2 = HibernateUtil.getSessionFactory().openSession();
//                    session2.beginTransaction();
//                    Receipt rec2 = new Receipt();
//                    String str = "FROM Receipts";
//                    Query query = session2.createQuery(str);
//                    this.value = query.list();
//                    //TODO insert the receipt line into the value receiptline
//                    session2.close();
//                    break;
//                }
            }
            updatedValue = true;
        }
        return value;
    }

    public Object getOne(String name,int id) {
        switch (name) {
            case "user": {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                return (User) session.get(User.class, id);
            }
            case "receipt": {
                for(Receipt r : this.receipts){
                    if(r.getId() == id)
                        return r;
                }
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Query q = session.createQuery("select * from ReceiptLines where id = :id").setParameter("id", id);
                List<ReceiptLine> reclines = q.list();
                Receipt rec = session.get(Receipt.class, id);
                rec.setLines(reclines);
                this.receipts.add(rec);
                return rec;
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
            case "receipt": {
                for(Receipt r : this.receipts){
                    if(r.getId() == id)
                        this.receipts.remove(r);
                }
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Receipt recToDelete = new Receipt();
                recToDelete.setId(id);
                session.delete(recToDelete);
                session.createQuery("delete from ReceiptLines where id = :id").setParameter("id", id).executeUpdate();
                session.getTransaction().commit();
            }
        }
        return true;
    }

//==========================================================================
//Store
    public boolean add(Store store){
        List<Store> list= (List<Store>) value;
        list.add(store);

        return false;
    }

//==========================================================================
//Receipt

    public boolean add(Receipt receipt){
        List<Receipt> list= (List<Receipt>) receipt;
        list.add(receipt);
        //adding to db
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ReceiptsEntity rec = new ReceiptsEntity();
        rec.setId(receipt.getId());
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

    public boolean add(Product prod, Discount dis) {
        Map<Product, Discount> map = (Map<Product, Discount>) value;
        ((Map<Product, Discount>) value).put(prod, dis);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        addDiscountPolicy(session, dis);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean add(String category, Discount dis) {
        Map<String, Discount> map = (Map<String, Discount>) value;
        ((Map<String, Discount>) value).put(category, dis);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        addDiscountPolicy(session, dis);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    private void addDiscountPolicy(Session session, Discount dis) {
        //Discount (simple & conditional)
        DiscountsEntity disEntity = new DiscountsEntity();
        disEntity.setId(dis.getId());
        disEntity.setPercentage(dis.getPercentage());
        disEntity.setMathOperator(dis.getMathOpStr());
        disEntity.setBeginDate(dateToString(dis.getBegin()));
        disEntity.setEndDate(dateToString(dis.getEnd()));

        //Conditional discount
        if(dis instanceof ConditionalDiscount) {
            DiscountConditionEntity disCondEntity = new DiscountConditionEntity();
            int condId = ((ConditionalDiscount) dis).getConditions().getId();
            disCondEntity.setId(condId);
            disCondEntity.setLogicOperator(((ConditionalDiscount) dis).getConditions().getOperatorStr());
            session.save(disCondEntity);

            List<Policy> policies = ((ConditionalDiscount) dis).getConditions().getDiscounts();
            for (Policy pol: policies) {
                if(pol instanceof DiscountByMinimalAmount) {
                    DiscountByMinimalAmountEntity entity = new DiscountByMinimalAmountEntity();
                    entity.setConditionId(condId);
                    entity.setMinAmount(Integer.parseInt(pol.getPolicyParams().get(0)));
                    entity.setProductId(Integer.parseInt(pol.getPolicyParams().get(1)));
                    session.save(entity);
                }
                else if(pol instanceof DiscountByMinimalCost) {
                    DiscountByMinimalCostEntity entity = new DiscountByMinimalCostEntity();
                    entity.setConditionId(condId);
                    entity.setMinCost(Double.parseDouble(pol.getPolicyParams().get(0)));
                    session.save(entity);
                }
                else { //Discount by purchase time
                    //params = boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour
                    DiscountByPurchaseTimeEntity entity = new DiscountByPurchaseTimeEntity();
                    entity.setConditionId(condId);
                    entity.setByDayInWeek(Boolean.parseBoolean(pol.getPolicyParams().get(0)));
                    entity.setByDayInMonth(Boolean.parseBoolean(pol.getPolicyParams().get(1)));
                    entity.setByHourInDay(Boolean.parseBoolean(pol.getPolicyParams().get(2)));
                    entity.setDayInWeek(Integer.parseInt(pol.getPolicyParams().get(3)));
                    entity.setDayInMonth(Integer.parseInt(pol.getPolicyParams().get(4)));
                    entity.setBeginHour(Integer.parseInt(pol.getPolicyParams().get(5)));
                    entity.setEndHour(Integer.parseInt(pol.getPolicyParams().get(6)));
                    session.save(entity);
                }
            } //Finish inserting to DB each policy in discount
            ConditionalDiscountEntity condDisEntity = new ConditionalDiscountEntity();
            condDisEntity.setConditionId(condId);
            condDisEntity.setDiscountId(dis.getId());
        }
    }

    public boolean remove(Product prod) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(prod);
        session.getTransaction().commit();
        session.close();
        //TODO fix implementation
        return true;
    }

    //====================================================================
    //User
    public boolean add(User user){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            Integer userId = user.getId();
            session.getTransaction().commit();
            return userId > 0;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public User searchUserByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria crit = session.createCriteria(User.class);
        crit.add(Restrictions.eq("userName","Elad"));
        if(crit.list().size()>0)
            return (User)crit.list().get(0);
        else
            return null;
    }
    public boolean remove(String category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
        //TODO fix implementation
        return true;
    }


    private String dateToString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1) +"/" + calendar.get(Calendar.YEAR);
    }
    public User getUserById(int id)
    {
        for(User user : users)
        {
            if(user.getId() == id)
                return user;
        }
        return null;
    }
    public User getUserByName(String name)
    {
        for(User user : users)
        {
            if(user.getUserName() == name)
                return user;
        }
        return null;
    }


    public Object size() {
        //TODO
            return 0;
    }
}
