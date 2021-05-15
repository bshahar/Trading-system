package Server;

import Domain.Result;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class MakePurchase {

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
        if(type.equals("BUY_PRODUCT")){
            int userId=Integer.parseInt(jo.get("userId").toString());
            int storeId=Integer.parseInt(jo.get("storeId").toString());
            JSONObject paymentData= jo.getJSONObject("paymentData");
            JSONObject supplementData= jo.getJSONObject("supplementData");
            Map<String,String> paymentMap= new HashMap<>();
            Map<String,String> supplementMap= new HashMap<>();

            paymentMap.put("card_number", paymentData.getString("card_number"));
            paymentMap.put("month", paymentData.getString("month"));
            paymentMap.put("year", paymentData.getString("year"));
            paymentMap.put("holder", paymentData.getString("holder"));
            paymentMap.put("cvv", paymentData.getString("cvv"));
            paymentMap.put("id",paymentData.getString("id"));

            supplementMap.put("name", supplementData.getString("name"));
            supplementMap.put("address", supplementData.getString("address"));
            supplementMap.put("city",supplementData.getString("city"));
            supplementMap.put("country",supplementData.getString("country"));
            supplementMap.put("zip", supplementData.getString("zip"));


            Result result=API.buyProduct(userId,storeId,paymentMap,supplementMap);
            if(result.isResult())
                API.sendAlertsAfterPurchase(storeId);
            JSONObject jsonOut=new JSONObject();
            jsonOut.put("type","BUY_PRODUCT");
            jsonOut.put("result",result.isResult());
            jsonOut.put("message",result.getData());
            session.getRemote().sendString(jsonOut.toString());
            }
        }

    }

