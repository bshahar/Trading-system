package Domain;

import Persistance.HibernateUtil;
import Persistance.ReceiptEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MyWrapper {

    private Object value;
    private boolean updatedValue=false;

    public MyWrapper(Object obj){
        value =obj;
    }

    public Object get(String dbName) {
        if (!updatedValue) {

            switch(dbName){
                case "receipt": {
                    Session session2 = HibernateUtil.getSessionFactory().openSession();
                    session2.beginTransaction();
                    ReceiptEntity rec2 = new ReceiptEntity();
                    String str = "FROM receipt";
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

    public boolean add(User user){
        return false;
    }

    public boolean add(Receipt receipt){
        List<Receipt> list= (List<Receipt>) receipt;
        list.add(receipt);
        //adding to db
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ReceiptEntity rec = new ReceiptEntity();
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



}
