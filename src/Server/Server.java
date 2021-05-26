package Server;
import Server.Login.LoginWebSocket;
import Server.myStores.myStoresWebSocket;
import Service.API;
import spark.Spark;

import java.io.IOException;

public class Server {

    public static void main(String []args) throws IOException {
        //Spark.secure("security/version2/KeyStore.jks", "123456", null,null);
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
        Spark.webSocket("/myStores/bids", BidsWebSocket.class);



        try {
            API.initTradingSystem();
        } catch (IOException e) {
            //TODO deal with failure of getting config file
        }
//        API.forTest();


        Spark.init();
    }
}
