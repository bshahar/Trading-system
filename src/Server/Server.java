package Server;
import Persistance.HibernateUtil;
import Persistance.ReceiptEntity;
import org.hibernate.Session;
import Server.Login.LoginWebSocket;
import Server.myStores.myStoresWebSocket;
import Service.API;
import spark.Spark;

import java.io.IOException;

public class Server {

    public static void main(String []args) throws IOException {
        Spark.secure("security/version2/KeyStore.jks", "123456", null,null);
        Spark.webSocket("/Login", LoginWebSocket.class);
        Spark.webSocket("/Main/*",MainWebSocket.class);
        Spark.webSocket("/Store/currentStore",StoreWebSocket.class);
        Spark.webSocket("/Cart",MyCartWebSocket.class);
        Spark.webSocket("/Cart/purchase",MakePurchase.class);
        Spark.webSocket("/myStores", myStoresWebSocket.class);
        Spark.webSocket("/myStores/openStore", OpenStoreWebServer.class);

        Spark.webSocket("/myStores/StorePermissions", myStoresWebSocket.class);
        Spark.webSocket("/myPurchases", myPurchases.class);
        Spark.webSocket("/myStores/StorePermissions/action", PermissionActionWebSocket.class);
        Spark.webSocket("/search", SearchProductsWebSocket.class);
        Spark.webSocket("/myStores/UpdatePermissions", PermissionsWebSocket.class);
        Spark.webSocket("/discountAndPurchasesPolicies", PolicyWebSocket.class);
        Spark.webSocket("/getDiscountAndPurchasesPolicies", GetPolicyWebSocket.class);
        Spark.webSocket("/deletePolicyAndPurchase", RemovePolicyAndPurchases.class);


        //test for inserting to database
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ReceiptEntity rec = new ReceiptEntity();
        rec.setId(101);
        rec.setStoreId(2);
        rec.setUserId(3);
        rec.setUserName("ErezTest");
        rec.setTotalCost(10.00);
        session.save(rec);
        session.getTransaction().commit();
        session.close();

        //test for fetching from database
//        Session session2 = HibernateUtil.getSessionFactory().openSession();
//        session2.beginTransaction();
//        ReceiptEntity rec2 = new ReceiptEntity();
//        session2.load(rec2,1);
//        System.out.println(rec2.getId() + " - " +rec2.getStoreId() + " - " + rec2.getUserName());
//        session.getTransaction().commit();
//        session.close();


        try {
            API.initTradingSystem();
        } catch (IOException e) {
            //TODO deal with failure of getting config file
        }
        API.forTest();


        Spark.init();
    }
}
