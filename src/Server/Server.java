package Server;

import Server.Login.LoginWebSocket;
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

        Spark.get("/Store",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Store"));
        }));


        Spark.init();
    }
}
