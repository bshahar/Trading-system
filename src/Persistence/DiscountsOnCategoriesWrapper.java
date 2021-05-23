package Persistence;
import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.*;
import Domain.Policy;
import Persistence.DAO.*;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountsOnCategoriesWrapper {

    private Map<String, Discount> value;

    public DiscountsOnCategoriesWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, String category, Discount discount) {
        try {
            ConnectionSource connectionSource = connect();
            if(this.value.containsKey(category))
                remove(discount.getId()); //Remove from DB
            //Discounts
            Dao<DiscountDAO, String> discountDAO = DaoManager.createDao(connectionSource, DiscountDAO.class);
            DiscountDAO discountDaoObj = new DiscountDAO(discount.getId(), discount.getMathOpStr(),
                    dateToString(discount.getBegin()), dateToString(discount.getEnd()), discount.getPercentage());
            discountDAO.create(discountDaoObj);
            //DiscountCondition
            if(discount instanceof ConditionalDiscount) {
                ConditionalDiscount tmp = (ConditionalDiscount) discount;
                Dao<DiscountConditionDAO, String> discountConditionDAO = DaoManager.createDao(connectionSource, DiscountConditionDAO.class);
                DiscountConditionDAO disConDaoObj = new DiscountConditionDAO(tmp.getId(), tmp.getConditions().getOperatorStr());
                discountConditionDAO.create(disConDaoObj);
                //Conditions
                List<Policy> policies = tmp.getConditions().getDiscounts();
                for (Policy pol : policies) {
                    if(pol instanceof DiscountByMinimalAmount) {
                        Dao<DiscountByMinimalAmountDAO, String> discountByMinimalAmountDAO = DaoManager.createDao(connectionSource, DiscountByMinimalAmountDAO.class);
                        DiscountByMinimalAmountDAO minAmountDaoObj = new DiscountByMinimalAmountDAO(tmp.getId(),
                                Integer.parseInt(pol.getPolicyParams().get(1)),
                                Integer.parseInt(pol.getPolicyParams().get(0)));
                        discountByMinimalAmountDAO.create(minAmountDaoObj);
                    }
                    else if(pol instanceof DiscountByMinimalCost) {
                        Dao<DiscountByMinimalCostDAO, String> discountByMinimalCostDAO = DaoManager.createDao(connectionSource, DiscountByMinimalCostDAO.class);
                        DiscountByMinimalCostDAO minCostDaoObj = new DiscountByMinimalCostDAO(tmp.getId(),
                                Integer.parseInt(pol.getPolicyParams().get(0)));
                        discountByMinimalCostDAO.create(minCostDaoObj);
                    }
                    else { //DiscountByPurchaseTime
                        Dao<DiscountByPurchaseTimeDAO, String> discountByPurchaseTimeDAO = DaoManager.createDao(connectionSource, DiscountByPurchaseTimeDAO.class);
                        //params = boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour
                        DiscountByPurchaseTimeDAO purchaseTimeDaoObj = new DiscountByPurchaseTimeDAO (tmp.getId(),
                                Boolean.parseBoolean(pol.getPolicyParams().get(0)),
                                Boolean.parseBoolean(pol.getPolicyParams().get(1)),
                                Boolean.parseBoolean(pol.getPolicyParams().get(2)),
                                Integer.parseInt(pol.getPolicyParams().get(3)),
                                Integer.parseInt(pol.getPolicyParams().get(4)),
                                Integer.parseInt(pol.getPolicyParams().get(5)),
                                Integer.parseInt(pol.getPolicyParams().get(6)));
                        discountByPurchaseTimeDAO.create(purchaseTimeDaoObj);
                    }
                } //Finish inserting policies
                //ConditionalDiscount
                Dao<ConditionalDiscountDAO, String> conditionalDiscountDAO = DaoManager.createDao(connectionSource, ConditionalDiscountDAO.class);
                ConditionalDiscountDAO condDisDaoObj = new ConditionalDiscountDAO(tmp.getId(), discount.getId());
                conditionalDiscountDAO.create(condDisDaoObj);
            }
            //StoreDiscountsOnCategories
            Dao<StoreDiscountsOnCategoriesDAO, String> storeDiscountsOnCategoriesDAO = DaoManager.createDao(connectionSource, StoreDiscountsOnCategoriesDAO.class);
            StoreDiscountsOnCategoriesDAO storeDisOnCatDaoObj = new StoreDiscountsOnCategoriesDAO(storeId, category, discount.getId());
            storeDiscountsOnCategoriesDAO.create(storeDisOnCatDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        this.value.put(category, discount); //Overwrite old entry in map
    }

    public void remove(int discountId) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<DiscountDAO, String> discountDAO = DaoManager.createDao(connectionSource, DiscountDAO.class);
            discountDAO.deleteById(String.valueOf(discountId));
            connectionSource.close();
        } catch (Exception e) {

        }
    }

    private String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public ConnectionSource connect() throws IOException, SQLException {
        Properties appProps = new Properties();
        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.properties");
        if (input != null)
            appProps.load(input);
        else
            throw new FileNotFoundException("Property file was not found.");

        boolean test = appProps.getProperty("forTests").equals("true");
        String url;
        String userName;
        String password;
        if (test) {
            url = appProps.getProperty("localDbURL");
            userName = appProps.getProperty("localDbUserName");
            password = appProps.getProperty("localDbPassword");
        } else {
            url = appProps.getProperty("dbURL");
            userName = appProps.getProperty("dbUsername");
            password = appProps.getProperty("dbPassword");
        }
        return new JdbcConnectionSource(url, userName, password);
    }
}
