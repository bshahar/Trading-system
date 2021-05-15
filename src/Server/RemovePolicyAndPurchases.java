package Server;

        import Domain.Result;
        import Service.API;
        import javafx.util.Pair;
        import org.eclipse.jetty.websocket.api.Session;
        import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
        import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
        import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
        import org.eclipse.jetty.websocket.api.annotations.WebSocket;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.Queue;
        import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class RemovePolicyAndPurchases {

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
        try {
            JSONObject jo = new JSONObject(message);
            String type = jo.get("type").toString();
            if (type.equals("REMOVE_DISCOUNT_PRODUCT")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
                int prodId = jo.getInt("productId");
                Result result = API.removeDiscountPolicy(storeId, userId, prodId,"");
                JSONObject out=new JSONObject();
                out.put("type","REMOVE_DISCOUNT_PRODUCT");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            } else if (type.equals("REMOVE_DISCOUNT_CATEGORY")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
                String category = jo.getString("category");
                Result result = API.removeDiscountPolicy(storeId, userId, -1, category);
                JSONObject out = new JSONObject();
                out.put("type", "REMOVE_DISCOUNT_CATEGORY");
                out.put("result", result.isResult());
                out.put("message", result.getData());
                session.getRemote().sendString(out.toString());
            } else if (type.equals("REMOVE_DISCOUNT_STORE")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
//                String category = jo.getString("category");
                Result result = API.removeDiscountPolicy(storeId, userId, -1,"");
                JSONObject out = new JSONObject();
                out.put("type", "REMOVE_DISCOUNT_STORE");
                out.put("result", result.isResult());
                out.put("message", result.getData());
                session.getRemote().sendString(out.toString());
            } else if (type.equals("REMOVE_PURCHASE_PRODUCT")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
                int productId= jo.getInt("productId");
                Result result = API.removePurchasePolicy(storeId, userId,productId,"");
                JSONObject out = new JSONObject();
                out.put("type", "REMOVE_PURCHASE_PRODUCT");
                out.put("result", result.isResult());
                out.put("message", result.getData());
                session.getRemote().sendString(out.toString());
            }else if (type.equals("REMOVE_PURCHASE_CATEGORY")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
                String category= jo.getString("category");
                Result result = API.removePurchasePolicy(storeId, userId,-1,category);
                JSONObject out = new JSONObject();
                out.put("type", "REMOVE_PURCHASE_CATEGORY");
                out.put("result", result.isResult());
                out.put("message", result.getData());
                session.getRemote().sendString(out.toString());
            }
            else if (type.equals("REMOVE_PURCHASE_STORE")) {
                int userId = jo.getInt("userId");
                int storeId = jo.getInt("storeId");
//                String category= jo.getString("category");
                Result result = API.removePurchasePolicy(storeId, userId,-1,"");
                JSONObject out = new JSONObject();
                out.put("type", "REMOVE_PURCHASE_STORE");
                out.put("result", result.isResult());
                out.put("message", result.getData());
                session.getRemote().sendString(out.toString());
            }
        } catch (Exception e) {

        }


    }

}