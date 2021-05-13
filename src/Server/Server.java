package Server;

import Permissions.AddProduct;
import Permissions.OpenStore;
import Server.Login.LoginWebSocket;
import Server.myStores.myStoresWebSocket;
import Service.API;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class Server {

    public static void main(String []args){
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






        API.initTradingSystem();
        API.forTest();

//        Spark.get("/Login",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Login"));
//        }));
////
//        Spark.get("/Main",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Main"));
//        }));
//
//        Spark.get("/myStores",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"myStores"));
//        }));
//
//        Spark.get("/StorePermissions",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"StorePermissions"));
//        }));
//
//        Spark.get("/Cart",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Cart"));
//        }));


//
//
//        Spark.get("/Store",((request, response) -> {
//            HashMap<String ,Object> model = new HashMap<>();
//
//            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Store"));
//        }));


        Spark.init();
    }
}
