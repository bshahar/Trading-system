package Server.Login;

import Domain.Result;
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
            if(type.equals("GUEST_LOGIN")){

                Result result = API.guestLogin();
                JSONObject json= new JSONObject();
                json.put("type", "GUEST_LOGIN");
                json.put("result",result.isResult()? "true": "false");
                json.put("message",result.getData());
                json.put("id", result.getData());
                session.getRemote().sendString(json.toString());
            }else if(type.equals("LOGGED_GUEST_LOGIN")) {
                String email = jo.get("email").toString();
                String pass = jo.get("password").toString();
                int userId= jo.getInt("userId");
                Result result=API.loggedGuestLogin(userId,email,pass);
                if(result.isResult()){
                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("type","LOGGED_GUEST_LOGIN");
                    jsonObject.put("result","true");
                    jsonObject.put("data",result.getData());
                    session.getRemote().sendString(jsonObject.toString());

                }else{
                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("type","LOGGED_GUEST_LOGIN");
                    jsonObject.put("result","false");
                    jsonObject.put("message",result.getData());
                    session.getRemote().sendString(jsonObject.toString());
                }

            }else{
                    String email = jo.get("email").toString();
                    String pass = jo.get("password").toString();
                    if (type.equals("REGISTER")) {
                        int age = jo.getInt("age");
                        Result result = API.register(email, pass, age);
                        if (!result.isResult()) {
                            JSONObject json = new JSONObject();
                            json.put("result", "false");
                            json.put("message", result.getData());
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
                        Result result = API.registeredLogin(email, pass);
                        if (!result.isResult()) {
                            JSONObject json = new JSONObject();
                            json.put("result", "false");
                            json.put("message", result.getData());
                            session.getRemote().sendString(json.toString());
                            System.out.println("error login");
                        } else {
                            JSONObject json = new JSONObject();
                            json.put("type", "LOGIN");
                            json.put("result", "true");
                            json.put("message", "login success");
                            json.put("id", result.getData());
                            json.put("systemManager",API.isSystemManager((int)result.getData()));
                            session.getRemote().sendString(json.toString());
                            System.out.println("success login");
                        }
                    }else if(type.equals("GUEST_REGISTER"))
                    {
                        int id=Integer.parseInt(jo.get("userId").toString());
                        Result result = API.guestRegister(id,jo.get("email").toString(),jo.get("password").toString());
                        JSONObject json= new JSONObject();
                        json.put("type", "GUEST_REGISTER");
                        json.put("result",result.isResult());
                        json.put("message",result.getData());
                        session.getRemote().sendString(json.toString());

                    }
            }
    }


}

