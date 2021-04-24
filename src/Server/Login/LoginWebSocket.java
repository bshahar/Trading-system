package Server.Login;

import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class LoginWebSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {

            JSONObject jo = new JSONObject(message);
            String type = jo.get("type").toString();
            if(type.equals("REGISTER_GUEST")){
                int result = API.guestLogin();
                JSONObject json= new JSONObject();
                json.put("type", "LOGIN");
                json.put("result","true");
                json.put("message","guest login success");
                json.put("id", result);
                session.getRemote().sendString(json.toString());
            }else {
                String email = jo.get("email").toString();
                String pass = jo.get("password").toString();
                if (type.equals("REGISTER")) {
                    int result = API.register(email, pass);
                    if (result == -1) {
                        JSONObject json = new JSONObject();
                        json.put("result", "false");
                        json.put("message", "error with registration");
                        session.getRemote().sendString(json.toString());
                        System.out.println("error register");
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("type", "REGISTER");
                        json.put("result", "true");
                        json.put("message", "registered successfully");
                        session.getRemote().sendString(json.toString());

                        System.out.println("success register");

                    }
                } else if (type.equals("LOGIN")) {
                    int result = API.registeredLogin(email, pass);
                    if (result == -1) {
                        JSONObject json = new JSONObject();
                        json.put("result", "false");
                        json.put("message", "error with login");
                        session.getRemote().sendString(json.toString());
                        System.out.println("error login");
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("type", "LOGIN");
                        json.put("result", "true");
                        json.put("message", "login success");
                        json.put("id", result);
                        session.getRemote().sendString(json.toString());
                        System.out.println("success login");
                    }
                }
            }


    }

}