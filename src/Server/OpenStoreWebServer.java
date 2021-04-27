package Server;

import Domain.*;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;
import org.thymeleaf.expression.Ids;

@WebSocket
public class OpenStoreWebServer {

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
        if (type.equals("OPEN_STORE")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            String storeName =jo.get("storeName").toString();
            Result result = API.openStore(userId,storeName);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "OPEN_STORE");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getdata());
            session.getRemote().sendString(jsonOut.toString());


        }
    }

}

