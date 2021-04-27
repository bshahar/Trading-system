package Server;

import Domain.Bag;
import Domain.Product;
import Domain.Result;
import Domain.Store;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;
import org.thymeleaf.expression.Ids;

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
            String creditInfo= jo.get("creditInfo").toString();
            Result result=API.buyProduct(userId,storeId,creditInfo);
            JSONObject jsonOut=new JSONObject();
            jsonOut.put("type","BUY_PRODUCT");
            jsonOut.put("result",result.isResult());
            jsonOut.put("message",result.getdata());
            session.getRemote().sendString(jsonOut.toString());
            if(result.isResult())
            {
                Result result2=API.getManagersAndOwnersOfStore(storeId);
                JSONObject json= new JSONObject();
                json.put("type", "ALERTS_USERS");
                json.put("data", "Someone buy from your store! go check it out");
                for(Integer id : (Set<Integer>)result2.getdata())
                {
                    if(MainWebSocket.sessionsMap.containsKey(id) && MainWebSocket.sessionsMap.get(id).isOpen())
                    {
                        MainWebSocket.sessionsMap.get(id).getRemote().sendString(json.toString());
                    }
                }
            }
            }
        }

    }

