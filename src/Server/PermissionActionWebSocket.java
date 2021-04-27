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
public class PermissionActionWebSocket {

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
        if (type.equals("ADD_PRODUCT")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            String prodName = jo.get("prodName").toString();
            String categories = jo.get("categories").toString();
            int price = Integer.parseInt(jo.get("price").toString());
            String description = jo.get("description").toString();
            int amount = Integer.parseInt(jo.get("amount").toString());
            List<String> arr = Arrays.asList(categories.split(","));
            Result result = API.addProduct(userId, storeId, prodName, arr, price, description, amount);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "ADD_PRODUCT");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getdata());
            session.getRemote().sendString(jsonOut.toString());

        } else if (type.equals("ADD_MANAGER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getdata();
                result = API.addStoreManager(ownerId, userId, storeId);
            }
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "ADD_MANAGER");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getdata());
            session.getRemote().sendString(jsonOut.toString());

        } else if (type.equals("ADD_OWNER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getdata();
                result = API.addStoreOwner(ownerId, userId, storeId);
            }
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "ADD_OWNER");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getdata());
            session.getRemote().sendString(jsonOut.toString());

        }
    }

}

