package Domain;

import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountFormat.SimpleDiscount;
import Domain.DiscountPolicies.*;
import Persistance.*;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public boolean add(User user){
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

    public boolean add(Product prod, Discount dis) {
        Map<Product, Discount> map = (Map<Product, Discount>) value;
        ((Map<Product, Discount>) value).put(prod, dis);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

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
        session.getTransaction().commit();
        session.close();
        return true;
    }

    private String dateToString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1) +"/" + calendar.get(Calendar.YEAR);
    }

}
