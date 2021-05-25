package Persistence;

import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountByMinimalAmount;
import Domain.DiscountPolicies.DiscountByMinimalCost;
import Domain.Policy;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PurchaseOnStoresWrapper {

    private ImmediatePurchase value;

    public PurchaseOnStoresWrapper(ImmediatePurchase immediatePurchase) {
        this.value = immediatePurchase;
    }

    public void add(int storeId, ImmediatePurchase immediatePurchase) {
        try {
            ConnectionSource connectionSource = connect();
            if(this.value != null)
                remove(immediatePurchase.getId()); //Remove from DB
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
            Dao<StorePoliciesOnStoresDAO, String> storePoliciesOnStoresDAO = DaoManager.createDao(connectionSource, StorePoliciesOnStoresDAO.class);
            StorePoliciesOnStoresDAO storePoliciesOnStoresDaoObj = new StorePoliciesOnStoresDAO(storeId, immediatePurchase.getId());
            storePoliciesOnStoresDAO.create(storePoliciesOnStoresDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        this.value = immediatePurchase; //Overwrite old value
    }

    public void remove(int immediatePurchaseId) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAO = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            immediatePurchasesDAO.deleteById(String.valueOf(immediatePurchaseId));
            connectionSource.close();
        } catch (Exception e) {

        }
    }

    public ImmediatePurchase getValue() { return this.value; }

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
