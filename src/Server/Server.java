package Server;
import Persistance.HibernateUtil;
import Persistance.ReceiptEntity;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import Domain.Receipt;
import Permissions.AddProduct;
import Permissions.OpenStore;
import Server.Login.LoginWebSocket;
import Server.myStores.myStoresWebSocket;
import Service.API;
import org.hibernate.Session;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.io.IOException;
import java.util.HashMap;

public class Server {

    public static void main(String []args) throws IOException {
//        Spark.secure("security/version2/KeyStore.jks", "123456", null,null);
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
        Spark.webSocket("/AdminWebSocket", AdminWebSocket.class);



        //test for inserting to database
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ReceiptEntity rec = new ReceiptEntity();
        rec.setId(1);
        rec.setStoreId(2);
        rec.setUserId(3);
        rec.setUserName("ErezTest");
        rec.setTotalCost(10.00);
        session.save(rec);
        session.getTransaction().commit();
        session.close();
        //


        try {
            API.initTradingSystem(true);
        } catch (IOException e) {
            //TODO deal with failure of getting config file
        }
        API.forTest();


        Spark.init();
    }
}
