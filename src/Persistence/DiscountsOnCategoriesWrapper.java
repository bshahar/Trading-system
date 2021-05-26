package Persistence;
import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountFormat.SimpleDiscount;
import Domain.DiscountPolicies.*;
import Domain.Operators.AndOperator;
import Domain.Operators.LogicOperator;
import Domain.Operators.OrOperator;
import Domain.Operators.XorOperator;
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

    public boolean contains(String category) {
        return this.value.containsKey(category);
    }

    public Discount get(String category, int storeId) {
        Discount d = this.value.get(category);
        if (d != null) return d;
        else {
            try {
                ConnectionSource connectionSource = connect();
                Dao<StoreDiscountsOnCategoriesDAO, String> storeDisOnCatDAOManager = DaoManager.createDao(connectionSource, StoreDiscountsOnCategoriesDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", storeId);
                fields.put("category", category);
                List<StoreDiscountsOnCategoriesDAO> storeDisOnCatDAOs = storeDisOnCatDAOManager.queryForFieldValues(fields);
                StoreDiscountsOnCategoriesDAO value = storeDisOnCatDAOs.get(0);
                //Check if conditional/simple discount
                Dao<ConditionalDiscountDAO, String> condDisDAOManager = DaoManager.createDao(connectionSource, ConditionalDiscountDAO.class);
                Map<String, Object> condDisFields = new HashMap<>();
                condDisFields.put("discountId", value.getDiscountId());
                List<ConditionalDiscountDAO> condDisDAOs = condDisDAOManager.queryForFieldValues(condDisFields);
                ConditionalDiscountDAO condDisDAO = condDisDAOs.get(0);
                //Create Discount object
                Dao<DiscountDAO, String> DiscountDAOManager = DaoManager.createDao(connectionSource, DiscountDAO.class);
                DiscountDAO discountDAO = DiscountDAOManager.queryForId(String.valueOf(value.getDiscountId()));
                if(condDisDAO != null) { //this is a conditional discount
                    //Create the DiscountConditionDAO
                    Dao<DiscountConditionDAO, String> DisCondDAOManager = DaoManager.createDao(connectionSource, DiscountConditionDAO.class);
                    DiscountConditionDAO disCondDAO = DisCondDAOManager.queryForId(String.valueOf(condDisDAO.getConditionId()));
                    //Create the policies
                    List<Policy> policies = new LinkedList<>();
                    Map<String, Object> condIdFilter = new HashMap<>();
                    condIdFilter.put("conditionId", condDisDAO.getConditionId());
                    //Minimal Amount policy
                    Dao<DiscountByMinimalAmountDAO, String> disByMinAmountDAOManager = DaoManager.createDao(connectionSource, DiscountByMinimalAmountDAO.class);
                    List<DiscountByMinimalAmountDAO> disByMinAmountDAO = disByMinAmountDAOManager.queryForFieldValues(condIdFilter);
                    for (DiscountByMinimalAmountDAO tmp: disByMinAmountDAO) {
                        policies.add(new DiscountByMinimalAmount(tmp.getMinAmount(), tmp.getProductId()));
                    }
                    //Minimal Cost policy
                    Dao<DiscountByMinimalCostDAO, String> disByMinCostDAOManager = DaoManager.createDao(connectionSource, DiscountByMinimalCostDAO.class);
                    List<DiscountByMinimalCostDAO> disByMinCostDAO = disByMinCostDAOManager.queryForFieldValues(condIdFilter);
                    for (DiscountByMinimalCostDAO tmp: disByMinCostDAO) {
                        policies.add(new DiscountByMinimalCost(tmp.getMinCost()));
                    }
                    //Purchase Time policy
                    Dao<DiscountByPurchaseTimeDAO, String> disByTimeDAOManager = DaoManager.createDao(connectionSource, DiscountByPurchaseTimeDAO.class);
                    List<DiscountByPurchaseTimeDAO> disByTimeDAO = disByTimeDAOManager.queryForFieldValues(condIdFilter);
                    for (DiscountByPurchaseTimeDAO tmp: disByTimeDAO) {
                        policies.add(new DiscountByPurchaseTime(tmp.isByDayInWeek(), tmp.isByDayInMonth(), tmp.isByHourInDay(),
                                tmp.getDayInWeek(), tmp.getDayInMonth(), tmp.getBeginHour(), tmp.getEndHour()));
                    }

                    DiscountCondition condition = new DiscountCondition(disCondDAO.getId(), policies, stringToLogicOp(disCondDAO.getLogicOperator()));
                    ConditionalDiscount result = new ConditionalDiscount(discountDAO.getId(),
                            stringToDate(discountDAO.getBeginDate()), stringToDate(discountDAO.getEndDate()), condition,
                            discountDAO.getPercentage(), stringToMathOp(discountDAO.getMathOperator()));
                    this.value.put(category, result);
                    connectionSource.close();
                    return result;
                }
                else { //this is a simple discount
                    SimpleDiscount result = new SimpleDiscount(discountDAO.getId(), stringToDate(discountDAO.getBeginDate()),
                            stringToDate(discountDAO.getEndDate()), discountDAO.getPercentage(), stringToMathOp(discountDAO.getMathOperator()));
                    this.value.put(category, result);
                    connectionSource.close();
                    return result;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }
    private Discount.MathOp stringToMathOp(String str) {
        if(str.equals("Max")) return Discount.MathOp.MAX;
        else return Discount.MathOp.SUM;
    }

    private LogicOperator stringToLogicOp(String str) {
        switch (str) {
            case "Or":
                return new OrOperator();
            case "And":
                return new AndOperator();
            default: return new XorOperator();
        }
    }

    private Date stringToDate(String date) {
        String[] parts = date.split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(parts[2]));
        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
        return cal.getTime();
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
