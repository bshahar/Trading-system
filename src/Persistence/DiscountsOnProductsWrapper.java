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
import Domain.Product;
import Domain.Store;
import Persistence.DAO.*;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountsOnProductsWrapper {

    private Map<Product, Discount> value;

    public DiscountsOnProductsWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, Product product, Discount discount) {
        try {
            ConnectionSource connectionSource = connect();
            if(this.value.containsKey(product))
                remove(discount); //Remove from DB
            //Discounts
            Dao<DiscountDAO, String> discountDAO = DaoManager.createDao(connectionSource, DiscountDAO.class);
            DiscountDAO discountDaoObj = new DiscountDAO(discount.getId(), discount.getMathOpStr(),
                    dateToString(discount.getBegin()), dateToString(discount.getEnd()), discount.getPercentage());
            discountDAO.create(discountDaoObj);
            //DiscountCondition
            if(discount instanceof ConditionalDiscount) {
                ConditionalDiscount tmp = (ConditionalDiscount) discount;
                Dao<DiscountConditionDAO, String> discountConditionDAO = DaoManager.createDao(connectionSource, DiscountConditionDAO.class);
                DiscountConditionDAO disConDaoObj = new DiscountConditionDAO(tmp.getConditions().getId(), tmp.getConditions().getOperatorStr());
                discountConditionDAO.create(disConDaoObj);
                //Conditions
                List<Policy> policies = tmp.getConditions().getDiscounts();
                for (Policy pol : policies) {
                    if(pol instanceof DiscountByMinimalAmount) {
                     Dao<DiscountByMinimalAmountDAO, String> discountByMinimalAmountDAO = DaoManager.createDao(connectionSource, DiscountByMinimalAmountDAO.class);
                        DiscountByMinimalAmountDAO minAmountDaoObj = new DiscountByMinimalAmountDAO(tmp.getConditions().getId(),
                                Integer.parseInt(pol.getPolicyParams().get(1)),
                                Integer.parseInt(pol.getPolicyParams().get(0)));
                        discountByMinimalAmountDAO.create(minAmountDaoObj);
                    }
                    else if(pol instanceof DiscountByMinimalCost) {
                        Dao<DiscountByMinimalCostDAO, String> discountByMinimalCostDAO = DaoManager.createDao(connectionSource, DiscountByMinimalCostDAO.class);
                        DiscountByMinimalCostDAO minCostDaoObj = new DiscountByMinimalCostDAO(tmp.getConditions().getId(),
                                Double.parseDouble(pol.getPolicyParams().get(0)));
                        discountByMinimalCostDAO.create(minCostDaoObj);
                    }
                    else { //DiscountByPurchaseTime
                        Dao<DiscountByPurchaseTimeDAO, String> discountByPurchaseTimeDAO = DaoManager.createDao(connectionSource, DiscountByPurchaseTimeDAO.class);
                        //params = boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour
                        DiscountByPurchaseTimeDAO purchaseTimeDaoObj = new DiscountByPurchaseTimeDAO (tmp.getConditions().getId(),
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
            //StoreDiscountsOnProducts
            Dao<StoreDiscountsOnProductsDAO, String> storeDiscountsOnProductsDAO = DaoManager.createDao(connectionSource, StoreDiscountsOnProductsDAO.class);
            StoreDiscountsOnProductsDAO storeDisOnProdDaoObj = new StoreDiscountsOnProductsDAO(storeId, product.getId(), discount.getId());
            storeDiscountsOnProductsDAO.create(storeDisOnProdDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        this.value.put(product, discount); //Overwrite old entry in map
    }

    public void remove(Discount discount) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<DiscountDAO, String> discountDAO = DaoManager.createDao(connectionSource, DiscountDAO.class);
            discountDAO.deleteById(String.valueOf(discount.getId()));
            if(discount instanceof ConditionalDiscount) {
                Dao<DiscountConditionDAO, String> conditionDAO = DaoManager.createDao(connectionSource, DiscountConditionDAO.class);
                conditionDAO.deleteById(String.valueOf(((ConditionalDiscount) discount).getConditions().getId()));
            }
            connectionSource.close();
            this.value.remove(discount);
        } catch (Exception e) {

        }
    }

    public boolean contains(Product product) {
        return this.value.containsKey(product);
    }

    public Discount get(Product product) {
        Discount d = this.value.get(product);
        if (d != null) return d;
        else {
            try {
                ConnectionSource connectionSource = connect();
                Dao<StoreDiscountsOnProductsDAO, String> storeDisOnProdDAOManager = DaoManager.createDao(connectionSource, StoreDiscountsOnProductsDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", product.getStoreId());
                fields.put("productId", product.getId());
                List<StoreDiscountsOnProductsDAO> storeDisOnProdDAOs = storeDisOnProdDAOManager.queryForFieldValues(fields);
                StoreDiscountsOnProductsDAO value = storeDisOnProdDAOs.get(0);
                //Check if conditional/simple discount
                Dao<ConditionalDiscountDAO, String> condDisDAOManager = DaoManager.createDao(connectionSource, ConditionalDiscountDAO.class);
                ConditionalDiscountDAO condDisDAO = condDisDAOManager.queryForId(String.valueOf(value.getDiscountId()));
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

                    DiscountCondition condition = new DiscountCondition(policies, stringToLogicOp(disCondDAO.getLogicOperator()));
                    ConditionalDiscount result = new ConditionalDiscount(discountDAO.getId(),
                            stringToDate(discountDAO.getBeginDate()), stringToDate(discountDAO.getEndDate()), condition,
                            discountDAO.getPercentage(), stringToMathOp(discountDAO.getMathOperator()));
                    return result;
                }
                else { //this is a simple discount
                    SimpleDiscount result = new SimpleDiscount(discountDAO.getId(), stringToDate(discountDAO.getBeginDate()),
                            stringToDate(discountDAO.getEndDate()), discountDAO.getPercentage(), stringToMathOp(discountDAO.getMathOperator()));
                    return result;
                }

               /* CloseableWrappedIterable<StoreDiscountsOnProductsDAO> itr = storeDisOnProdDAOManager.getWrappedIterable();
                while (itr.iterator().hasNext()) {
                    StoreDiscountsOnProductsDAO tmpDao = itr.iterator().next();
                    if (tmpDao.getStoreId() == product.getStoreId() && tmpDao.getProductId() == product.getId()) {
                        Dao<DiscountDAO, String> DiscountDAOManager = DaoManager.createDao(connectionSource, DiscountDAO.class);
                        DiscountDAO discountDAO = DiscountDAOManager.queryForId(Integer.toString(tmpDao.getDiscountId()));
                       // Discount result =
                       // result.setId(discountDAO.getId());
                       // result.setPercentage(discountDAO.getPercentage());
                       // result.setBegin(stringToDate(discountDAO.getBeginDate()));
                       // result.setEnd(stringToDate(discountDAO.getEndDate()));
                       // result.setMathOp(stringToMathOp(discountDAO.getMathOperator()));
                       // return result;
                    }
                }*/
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
