package Persistence;

import Domain.*;
import Persistence.DAO.MemberStorePermissionsDAO;
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

public class MemberStorePermissionsWrapper {


    public void add(Permission p,int userId,int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<MemberStorePermissionsDAO, String> memberManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            MemberStorePermissionsDAO memberStorePermissionsDAO = new MemberStorePermissionsDAO(userId,storeId);

            memberStorePermissionsDAO.setAddPermissions(            p.getAddPermissions()!=null);
            memberStorePermissionsDAO.setAddProduct(                p.getAddProduct()!=null);
            memberStorePermissionsDAO.setAppointManager(            p.getAppointManager()!=null);
            memberStorePermissionsDAO.setAppointOwner(              p.getAppointOwner()!=null);
            memberStorePermissionsDAO.setCloseStore(                p.getCloseStore()!=null);
            memberStorePermissionsDAO.setDefineDiscountFormat(      p.getDefineDiscountFormat()!=null);
            memberStorePermissionsDAO.setDefineDiscountPolicy(      p.getDefineDiscountPolicy()!=null);
            memberStorePermissionsDAO.setDefinePurchaseFormat(      p.getDefinePurchaseFormat()!=null);
            memberStorePermissionsDAO.setDefinePurchasePolicy(      p.getDefinePurchasePolicy()!=null);
            memberStorePermissionsDAO.setEditDiscountFormat(        p.getEditDiscountFormat()!=null);
            memberStorePermissionsDAO.setEditDiscountPolicy(        p.getEditDiscountPolicy()!=null);
            memberStorePermissionsDAO.setEditProduct(               p.getEditProduct()!=null);
            memberStorePermissionsDAO.setEditPurchaseFormat(        p.getEditPurchaseFormat()!=null);
            memberStorePermissionsDAO.setEditPurchasePolicy(        p.getEditPurchasePolicy()!=null);
            memberStorePermissionsDAO.setGetWorkersInfo(            p.getGetWorkersInfo()!=null);
            memberStorePermissionsDAO.setOpenStore(                 p.getOpenStore()!=null);
            memberStorePermissionsDAO.setRemoveManagerAppointment(  p.getRemoveManagerAppointment()!=null);
            memberStorePermissionsDAO.setRemoveOwnerAppointment(    p.getRemoveOwnerAppointment()!=null);
            memberStorePermissionsDAO.setRemovePermission(          p.getRemovePermission()!=null);
            memberStorePermissionsDAO.setRemoveProduct(             p.getRemoveProduct()!=null);
            memberStorePermissionsDAO.setReopenStore(               p.getReopenStore()!=null);
            memberStorePermissionsDAO.setReplayMessages(            p.getReplayMessages()!=null);
            memberStorePermissionsDAO.setViewDiscountPolicies(      p.getViewDiscountPolicies()!=null);
            memberStorePermissionsDAO.setViewMessages(              p.getViewMessages()!=null);
            memberStorePermissionsDAO.setViewPurchaseHistory(       p.getViewPurchaseHistory()!=null);
            memberStorePermissionsDAO.setResponedToOffer(           p.getResponedToOffer()!=null);
            memberManager.create(memberStorePermissionsDAO);
            connectionSource.close();



        }catch (Exception e){

        }
    }


    public Member getMemberByUserId(int userId) {
        try {
            StoreWrapper storeWrapper = new StoreWrapper();
            Member member = new Member();
            ConnectionSource connectionSource = connect();

            List<Integer> stores = storeWrapper.getStoresByUserId(userId);


            Dao<MemberStorePermissionsDAO, String> MemberManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("userId",userId);
            List<MemberStorePermissionsDAO> permissions = MemberManager.queryForFieldValues(map);
            //get all the permissions of the user
//
            Map<Integer, MemberStorePermissionsDAO> storeId_Permissions = new ConcurrentHashMap<>();
            for (MemberStorePermissionsDAO perDAO : permissions) {
                storeId_Permissions.put(perDAO.getStoreId(), perDAO);
            }

            Map<Integer, Permission> result = new ConcurrentHashMap<>();

            for ( Map.Entry<Integer, MemberStorePermissionsDAO>  entry : storeId_Permissions.entrySet())
            {
                Store store = storeWrapper.getById(entry.getKey());
                Permission p = makePermissionFromList(member,store,entry.getValue());
                result.put(store.getStoreId(),p);

            }

            member.setMyStores(stores);
            member.setPermissions(result);


            connectionSource.close();
            return member;
        }
        catch (Exception e)
        {
            return null;
        }


    }

    private Permission makePermissionFromList(Member member, Store store, MemberStorePermissionsDAO permissionsDAO) {
        Permission p = new Permission(member,store);

            if(permissionsDAO.isAddPermissions())
                p.allowAddPermissions();
            if(permissionsDAO.isAddProduct())
                p.allowAddProduct();
            if(permissionsDAO.isAppointManager())
                p.allowAppointManager();
            if(permissionsDAO.isAppointOwner())
                p.allowAppointOwner();
            if(permissionsDAO.isCloseStore())
                p.allowCloseStore();
            if(permissionsDAO.isDefineDiscountFormat())
                p.allowDefineDiscountFormat();
            if(permissionsDAO.isDefineDiscountPolicy())
                p.allowDefineDiscountPolicy();
            if(permissionsDAO.isDefinePurchaseFormat())
                p.allowDefinePurchaseFormat();
            if(permissionsDAO.isDefinePurchasePolicy())
                p.allowDefinePurchasePolicy();
            if(permissionsDAO.isEditDiscountFormat())
                p.allowEditDiscountFormat();
            if(permissionsDAO.isDefineDiscountPolicy())
                p.allowEditDiscountPolicy();
            if(permissionsDAO.isEditProduct())
                p.allowEditProduct();
            if(permissionsDAO.isEditPurchaseFormat())
                p.allowEditPurchaseFormat();
            if(permissionsDAO.isEditPurchasePolicy())
                p.allowEditPurchasePolicy();
            if(permissionsDAO.isGetWorkersInfo())
                p.allowGetWorkersInfo();
            if(permissionsDAO.isOpenStore())
                p.allowOpenStore();
            if(permissionsDAO.isRemoveManagerAppointment())
                p.allowRemoveManagerAppointment();
            if(permissionsDAO.isRemoveOwnerAppointment())
                p.allowRemoveOwnerAppointment();
            if(permissionsDAO.isRemovePermission())
                p.allowRemovePermission();
            if(permissionsDAO.isRemoveProduct())
            p.allowRemoveProduct();
            if(permissionsDAO.isReopenStore())
                p.allowReopenStore();
            if(permissionsDAO.isReplayMessages())
                p.allowReplayMessages();
            if(permissionsDAO.isResponedToOffer())
                p.allowResponedToOffer();
            if(permissionsDAO.isViewDiscountPolicies())
                p.allowViewDiscountPolicies();
            if(permissionsDAO.isViewMessages())
                p.allowViewMessages();
            if(permissionsDAO.isViewPurchaseHistory())
                p.allowViewPurchaseHistory();
            if(permissionsDAO.isViewPurchasePolicies())
                p.allowViewPurchaseHistory();
            if(permissionsDAO.isResponedToOffer())
                p.allowResponedToOffer();

            return p;
    }


    public boolean enablePermissionOnUser(int userId,int storeID ,String permission_name){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<MemberStorePermissionsDAO, String> MemberManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            // create an instance of Account
            MemberManager.executeRaw("UPDATE MemberStorePermissions\n" +
                                    "SET "+permission_name+"= 1"+
                                    "WHERE userId = "+String.valueOf(userId)+"AND storeId= "+String.valueOf(storeID)+";");
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean disablePermissionOnUser(int userId,int storeID ,String permission_name){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<MemberStorePermissionsDAO, String> MemberManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            // create an instance of Account
            MemberManager.executeRaw("UPDATE MemberStorePermissions\n" +
                    "SET "+permission_name+"= 0"+
                    "WHERE userId = "+String.valueOf(userId)+"AND storeId= "+String.valueOf(storeID)+";");
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }


    public ConnectionSource connect() throws IOException, SQLException {
        Properties appProps = new Properties();
        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.properties");
        if(input != null)
            appProps.load(input);
        else
            throw new FileNotFoundException("Property file was not found.");

        boolean test = appProps.getProperty("forTests").equals("true");
        String url;
        String userName;
        String password;
        if(test)
        {
            url = appProps.getProperty("localDbURL");
            userName = appProps.getProperty("localDbUserName");
            password = appProps.getProperty("localDbPassword");
        }
        else{
            url = appProps.getProperty("dbURL");
            userName = appProps.getProperty("dbUsername");
            password = appProps.getProperty("dbPassword");
        }
        return new JdbcConnectionSource(url,userName,password);

    }


}
