package Persistence;

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

    private LogicOperator stringToLogicOp(String str) {
        switch (str) {
            case "Or":
                return new OrOperator();
            case "And":
                return new AndOperator();
            default: return new XorOperator();
        }
    }

    public boolean contains( String category) {
        return this.value.containsKey(category);
    }

    public ImmediatePurchase get(int storeId,String category) {
        ImmediatePurchase ip = this.value.get(category);
        if (ip != null) return ip;
        else {
            try {
                ConnectionSource connectionSource = connect();
                Dao<StorePoliciesOnCategoriesDAO, String> storePoliciesOnCategoriesDAOManager = DaoManager.createDao(connectionSource, StorePoliciesOnCategoriesDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", storeId);
                fields.put("category", category);
                List<StorePoliciesOnCategoriesDAO> storePolOnCatDAOs = storePoliciesOnCategoriesDAOManager.queryForFieldValues(fields);
                StorePoliciesOnCategoriesDAO value = storePolOnCatDAOs.get(0);

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
                this.value.put(category, immediatePurchase);
                connectionSource.close();
                return immediatePurchase;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /*private String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }*/

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }
}
