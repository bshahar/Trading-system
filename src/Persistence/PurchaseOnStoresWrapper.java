package Persistence;

import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountByMinimalAmount;
import Domain.DiscountPolicies.DiscountByMinimalCost;
import Domain.Operators.AndOperator;
import Domain.Operators.LogicOperator;
import Domain.Operators.OrOperator;
import Domain.Operators.XorOperator;
import Domain.Policy;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchasePolicies.*;
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

public class PurchaseOnStoresWrapper {

    private ImmediatePurchase value;

 /*   public PurchaseOnStoresWrapper(ImmediatePurchase immediatePurchase) {
        this.value = immediatePurchase;
    }*/

    public PurchaseOnStoresWrapper(int storeId, ImmediatePurchase immediatePurchase) {
        try {
            ConnectionSource connectionSource = connect();
            if(this.value != null)
                remove(immediatePurchase); //Remove from DB
            Dao<PurchaseConditionDAO, String> purchaseConditions = DaoManager.createDao(connectionSource, PurchaseConditionDAO.class);
            PurchaseConditionDAO purchaseCondObj = new PurchaseConditionDAO(immediatePurchase.getId(), immediatePurchase.getConditions().getOperator());
            purchaseConditions.create(purchaseCondObj);
            //Conditions
            List<Policy> policies = immediatePurchase.getConditions().getPurchases();
            for (Policy pol : policies) {
                if(pol instanceof AgeLimitPolicy) {
                    Dao<AgeLimitPolicyDAO, String> ageLimitPolicyDAO = DaoManager.createDao(connectionSource, AgeLimitPolicyDAO.class);
                    AgeLimitPolicyDAO ageLimDaoObj = new AgeLimitPolicyDAO(immediatePurchase.getId(),
                            Integer.parseInt(pol.getPolicyParams().get(0)));
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
                            Integer.parseInt(pol.getPolicyParams().get(0)));
                    timeLimitPolicyDAO.create(timeLimDaoObj);
                }
            } //Finish inserting policies
            //ConditionalDiscount
            Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAO = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            ImmediatePurchasesDAO immediatePDaoObj = new ImmediatePurchasesDAO(immediatePurchase.getId(), immediatePurchase.getId());
            immediatePurchasesDAO.create(immediatePDaoObj);

            //StorePoliciesOnStores
            Dao<StorePoliciesOnStoresDAO, String> storePoliciesOnStoresDAO = DaoManager.createDao(connectionSource, StorePoliciesOnStoresDAO.class);
            StorePoliciesOnStoresDAO storePoliciesOnStoresDaoObj = new StorePoliciesOnStoresDAO(storeId, immediatePurchase.getId());
            storePoliciesOnStoresDAO.create(storePoliciesOnStoresDaoObj);

            connectionSource.close();
        } catch (Exception e) {
        }
        this.value = immediatePurchase; //Overwrite old value
    }

    public void remove(ImmediatePurchase purchase) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAO = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            immediatePurchasesDAO.deleteById(String.valueOf(purchase.getId()));
            Dao<PurchaseConditionDAO, String> purchaseConditionDAO = DaoManager.createDao(connectionSource, PurchaseConditionDAO.class);
            purchaseConditionDAO.deleteById(String.valueOf(purchase.getConditions().getId()));
            connectionSource.close();
        } catch (Exception e) {

        }
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

    public ImmediatePurchase getValue(int storeId) {
        ImmediatePurchase ip = this.value;
        if (ip != null) return ip;
        else {
            try {
                ConnectionSource connectionSource = connect();
                Dao<StorePoliciesOnStoresDAO, String> storePoliciesOnStoresDAOManager = DaoManager.createDao(connectionSource, StorePoliciesOnStoresDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", storeId);
                List<StorePoliciesOnStoresDAO> storePolOnStoresDAOs = storePoliciesOnStoresDAOManager.queryForFieldValues(fields);
                StorePoliciesOnStoresDAO value = storePolOnStoresDAOs.get(0);

                int immediatePurchaseId = value.getImmediatePurchaseId();

                Dao<ImmediatePurchasesDAO, String> immediatePurchasesDAOManager = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
                Map<String, Object> immpurFields = new HashMap<>();
                immpurFields.put("id", immediatePurchaseId);
                List<ImmediatePurchasesDAO> immpurDAOs = immediatePurchasesDAOManager.queryForFieldValues(immpurFields);
                ImmediatePurchasesDAO immpurDAO = immpurDAOs.get(0);

                int conditionId = immpurDAO.getConditionId();

                Dao<PurchaseConditionDAO, String> purCondDAOManager = DaoManager.createDao(connectionSource, PurchaseConditionDAO.class);
                PurchaseConditionDAO purCondDAO = purCondDAOManager.queryForId(String.valueOf(conditionId));

                String logicOperator = purCondDAO.getLogicOperator();

                List<Policy> policies = new LinkedList<>();
                Map<String, Object> condIdFilter = new HashMap<>();
                condIdFilter.put("conditionId", conditionId);
                //Age Limit policy
                Dao<AgeLimitPolicyDAO, String> ageLimitDAOManager = DaoManager.createDao(connectionSource, AgeLimitPolicyDAO.class);
                List<AgeLimitPolicyDAO> ageLimitDAO = ageLimitDAOManager.queryForFieldValues(condIdFilter);
                for (AgeLimitPolicyDAO tmp : ageLimitDAO) {
                    policies.add(new AgeLimitPolicy(tmp.getAgeLimit()));
                }
                //Time Limit policy
                Dao<TimeLimitPolicyDAO, String> timeLimitDAOManager = DaoManager.createDao(connectionSource, TimeLimitPolicyDAO.class);
                List<TimeLimitPolicyDAO> timeLimitDAO = timeLimitDAOManager.queryForFieldValues(condIdFilter);
                for (TimeLimitPolicyDAO tmp : timeLimitDAO) {
                    policies.add(new TimeLimitPolicy(tmp.getHourInDay()));
                }
                //Max Amount Policy
                Dao<MaxAmountPolicyDAO, String> maxAmountDAOManager = DaoManager.createDao(connectionSource, MaxAmountPolicyDAO.class);
                List<MaxAmountPolicyDAO> maxAmountDAO = maxAmountDAOManager.queryForFieldValues(condIdFilter);
                for (MaxAmountPolicyDAO tmp : maxAmountDAO) {
                    policies.add(new MaxAmountPolicy(tmp.getMaxAmount(), tmp.getProductId()));
                }
                //Min Amount Policy
                Dao<MinAmountPolicyDAO, String> minAmountDAOManager = DaoManager.createDao(connectionSource, MinAmountPolicyDAO.class);
                List<MinAmountPolicyDAO> minAmountDAO = minAmountDAOManager.queryForFieldValues(condIdFilter);
                for (MinAmountPolicyDAO tmp : minAmountDAO) {
                    policies.add(new MinAmountPolicy(tmp.getMinAmount(), tmp.getProductId()));
                }

                PurchaseCondition condition = new PurchaseCondition(immpurDAO.getConditionId(), policies, stringToLogicOp(logicOperator));
                ImmediatePurchase immediatePurchase = new ImmediatePurchase(immediatePurchaseId, condition);
                this.value = immediatePurchase;
                return immediatePurchase;
            } catch (Exception e) {
                return null;
            }
        }
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
