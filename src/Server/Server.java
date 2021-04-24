package Server;

import Server.Login.LoginWebSocket;
import Server.myStores.myStoresWebSocket;
import Service.API;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class Server {

    public static void main(String []args){
        Spark.secure("security/version2/KeyStore.jks", "123456", "security/version2/truststore.jks","123456");
        Spark.webSocket("/Login", LoginWebSocket.class);
        Spark.webSocket("/Main",MainWebSocket.class);
        Spark.webSocket("/Store/currentStore",StoreWebSocket.class);


        Spark.webSocket("/myStores", myStoresWebSocket.class);
        Spark.webSocket("/myStores/StorePermissions", myStoresWebSocket.class);
        API.initTradingSystem("ELAD");
        API.forTest();

        Spark.get("/Login",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();
            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Login"));
        }));

        Spark.get("/Main",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Main"));
        }));

        Spark.get("/myStores",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"myStores"));
        }));

        Spark.get("/StorePermissions",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"StorePermissions"));
        }));




        Spark.get("/Store",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Store"));
        }));


        Spark.init();
    }
}