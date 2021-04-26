package Server;

import Domain.Result;
import Domain.Store;
import Service.API;
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
    public static final Map<Integer ,Session> sessionsMap = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        int id  = Integer.parseInt(session.getUpgradeRequest().getParameterMap().get("userId").get(0));
        sessionsMap.put(id,session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {

        sessions.remove(session);
        int id  = Integer.parseInt(session.getUpgradeRequest().getParameterMap().get("userId").get(0));
        sessionsMap.remove(id);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        JSONObject jo = new JSONObject(message);
        String type = jo.get("type").toString();
        int id = Integer.parseInt(jo.get("id").toString());
        if (type.equals("GET_STORES")) {
            List<Store> stores = (List<Store>) API.getAllStoreInfo(id).getdata();
            JSONObject json = new JSONObject();
            json.put("type", "GET_STORES");
            JSONObject[] jsonStores = new JSONObject[stores.size()];
            int i = 0;
            for (Store store : stores) {
                System.out.println("GOT stores " + store.getName());
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("storeName", store.getName());
                jsonProduct.put("storeId", store.getStoreId());
                jsonStores[i] = jsonProduct;
                i++;
            }
            json.put("stores", jsonStores);
            System.out.println(json);
            session.getRemote().sendString(json.toString());
        } else if (type.equals("LOGOUT")) {
            boolean result = API.registeredLogout(id).isResult();
            JSONObject json = new JSONObject();
            json.put("type", "LOGOUT");
            json.put("result", result);
            session.getRemote().sendString(json.toString());
        } else if (type.equals("OPEN")) {
            boolean result = API.isRegister(id);
            JSONObject json = new JSONObject();
            json.put("type", "OPEN");
            json.put("result", result);
            session.getRemote().sendString(json.toString());
        } else if (type.equals("GUEST_REGISTER")) {
            Result result = API.guestRegister(id, jo.get("email").toString(), jo.get("password").toString());
            JSONObject json = new JSONObject();
            json.put("type", "GUEST_REGISTER");
            json.put("result", result.isResult());
            json.put("data", result.getdata());
            session.getRemote().sendString(json.toString());

        } else if (type.equals("GET_NOTIFICATIONS")) {
            Result result = API.getMessagesQueueAsArray(id);
            JSONObject json = new JSONObject();
            json.put("type", "GET_NOTIFICATIONS");
            json.put("result", result.isResult());
            json.put("data", result.getdata());
            session.getRemote().sendString(json.toString());

        }
    }


}