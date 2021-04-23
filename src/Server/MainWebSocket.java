package Server;

import Domain.Store;
import Service.API;
import netscape.javascript.JSObject;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class MainWebSocket {

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
        if(type.equals("GET_STORES")){
            int id = Integer.valueOf(jo.get("id").toString());
            String stores = API.getAllStoreNames(id);
            JSONObject json= new JSONObject();
            json.put("type", "STORES_NAMES");
            json.put("names",stores);
            session.getRemote().sendString(json.toString());
        }

    }

}