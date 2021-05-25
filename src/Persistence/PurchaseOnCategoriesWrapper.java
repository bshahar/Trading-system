package Persistence;

import Domain.Policy;
import Domain.Product;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchasePolicies.AgeLimitPolicy;
import Domain.PurchasePolicies.MaxAmountPolicy;
import Domain.PurchasePolicies.MinAmountPolicy;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseOnCategoriesWrapper {

    private Map<String, ImmediatePurchase> value;

    public PurchaseOnCategoriesWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, String category, ImmediatePurchase immediatePurchase) {
        try {
            ConnectionSource connectionSource = connect();
            if(this.value.containsKey(category))
                remove(immediatePurchase.getId()); //Remove from DB
            //Discounts
           /* Dao<DiscountDAO, String> discountDAO = DaoManager.createDao(connectionSource, DiscountDAO.class);
            DiscountDAO discountDaoObj = new DiscountDAO(discount.getId(), discount.getMathOpStr(),
                    dateToString(discount.getBegin()), dateToString(discount.getEnd()), discount.getPercentage());
            discountDAO.create(discountDaoObj);*/
            //DiscountCondition
            // if(discount instanceof ConditionalDiscount) {
            Dao<PurchaseConditionDAO, String> purchaseConditions = DaoManager.createDao(connectionSource, PurchaseConditionDAO.class);
            PurchaseConditionDAO purchaseCondObj = new PurchaseConditionDAO(immediatePurchase.getId(), immediatePurchase.getConditions().getOperator());
            purchaseConditions.create(purchaseCondObj);
            //Conditions
            List<Policy> policies = immediatePurchase.getConditions().getPurchases();
            for (Policy pol : policies) {
                if(pol instanceof AgeLimitPolicy) {
                    Dao<AgeLimitPolicyDAO, String> ageLimitPolicyDAO = DaoManager.createDao(connectionSource, AgeLimitPolicyDAO.class);
                    AgeLimitPolicyDAO ageLimDaoObj = new AgeLimitPolicyDAO(immediatePurchase.getId(),
                            Integer.parseInt(pol.getPolicyParams().get(1)));
                    ageLimitPolicyDAO.create(ageLimDaoObj);
                }
                else if(pol instanceof MaxAmountPolicy) {
                    Dao<MaxAmountPolicyDAO, String> maxAmountPolicyDAO = DaoManager.createDao(connectionSource, MaxAmountPolicyDAO.class);
                    MaxAmountPolicyDAO maxAmountDaoObj = new MaxAmountPolicyDAO(immediatePurchase.getId(),
                            Integer.parseInt(pol.getPolicyParams().get(0)), Integer.parseInt(pol.getPolicyParams().get(1)));
                    maxAmountPolicyDAO.create(maxAmountDaoObj);
                }
                else if(pol instanceof MinAmountPolicy) {
                    Dao<MinAmountPolicyDAO, String> minAmountPolicyDAO = DaoManager.createDao(connectionSource, MinAmountPolicyDAO.class);
                    MinAmountPolicyDAO minAmountDaoObj = new MinAmountPolicyDAO(immediatePurchase.getId(),
                            Integer.parseInt(pol.getPolicyParams().get(0)), Integer.parseInt(pol.getPolicyParams().get(1)));
                    minAmountPolicyDAO.create(minAmountDaoObj);
                }
                else { //Time Limit Policy
                    Dao<TimeLimitPolicyDAO, String> timeLimitPolicyDAO = DaoManager.createDao(connectionSource, TimeLimitPolicyDAO.class);
                    TimeLimitPolicyDAO timeLimDaoObj = new TimeLimitPolicyDAO(immediatePurchase.getId(),
                            Integer.parseInt(pol.getPolicyParams().get(1)));
                    timeLimitPolicyDAO.create(timeLimDaoObj);
                }
            } //Finish inserting policies
            //ConditionalDiscount
            Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAO = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            ImmediatePurchasesDAO immediatePDaoObj = new ImmediatePurchasesDAO(immediatePurchase.getId(), immediatePurchase.getId());
            immediatePurchasesDAO.create(immediatePDaoObj);

            //StorePoliciesOnProducts
            Dao<StorePoliciesOnCategoriesDAO, String> storePoliciesOnCategoriessDAO = DaoManager.createDao(connectionSource, StorePoliciesOnCategoriesDAO.class);
            StorePoliciesOnCategoriesDAO storePoliciesOnCatDaoObj = new StorePoliciesOnCategoriesDAO(storeId, category, immediatePurchase.getId());
            storePoliciesOnCategoriessDAO.create(storePoliciesOnCatDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        this.value.put(category, immediatePurchase); //Overwrite old entry in map
    }

    public void remove(int immediateId) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAO = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            immediatePurchasesDAO.deleteById(String.valueOf(immediateId));
            connectionSource.close();
        } catch (Exception e) {

        }
    }

    public boolean contains(String category) {
        return this.value.containsKey(category);
    }

    public ImmediatePurchase get(String category) {
        return this.value.get(category);
    }

    /*private String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }*/

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
