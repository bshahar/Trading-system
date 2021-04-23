package Server;

import Server.Login.LoginWebSocket;
import Service.API;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class Server {

    public static void main(String []args){
//        Spark.secure("security/.keystore", "123456", null,null);
        Spark.webSocket("/Login", LoginWebSocket.class);
        API.initTradingSystem("ELAD");

        Spark.get("/Login",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Login"));
        }));



        Spark.init();
    }
}
