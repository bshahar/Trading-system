package Server;

import Server.Login.LoginWebSocket;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class EladHOmo {

    public static void main(String []args){
        Spark.webSocket("/Login", LoginWebSocket.class);


        Spark.get("/Login",((request, response) -> {
            HashMap<String ,Object> model = new HashMap<>();

            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"Login"));
        }));



        Spark.init();
    }
}
