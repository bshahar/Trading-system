package Server.StorePermissions;

import Domain.Store;
import Service.API;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;



@WebSocket
public class storePermissionsWebSocket {

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
        int id = Integer.parseInt(jo.get("id").toString());
        if(type.equals("GET_STORES")){
            List<Store> stores = API.getMyStores(id);
            JSONObject json= new JSONObject();
            json.put("type", "GET_STORES");
            JSONObject[] jsonStores=new JSONObject[stores.size()];
            int i=0;
            for(Store store: stores){
                System.out.println("GOT stores "+store.getName());
                JSONObject jsonProduct= new JSONObject();
                jsonProduct.put("storeName",store.getName());
                jsonProduct.put("storeId",store.getStoreId());
                jsonStores[i]=jsonProduct;
                i++;
            }
            json.put("stores",jsonStores);
            System.out.println(json);
            session.getRemote().sendString(json.toString());
        }
        else if(type.equals("LOGOUT"))
        {
            boolean result = API.registeredLogout(id).isResult();
            JSONObject json= new JSONObject();
            json.put("type", "LOGOUT");
            json.put("result",result);
            session.getRemote().sendString(json.toString());
        }
    }

}

