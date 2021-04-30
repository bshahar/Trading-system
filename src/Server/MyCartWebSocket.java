package Server;

import Domain.Bag;
import Domain.Product;
import Domain.Result;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class MyCartWebSocket {

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
        if(type.equals("GET_CART")){
            int userId=Integer.parseInt(jo.get("id").toString());
            Result result=API.getCart(userId);
            if(result.isResult()){
                List<Bag> bags=(List<Bag>)result.getData();
                JSONObject jsonOut=new JSONObject();
                jsonOut.put("type","GET_CART");
                JSONObject[] jsonBags=new JSONObject[bags.size()];
                int j=0;
                for(Bag bag:bags){
                    int storeId=bag.getStoreId();
                    String storeName=API.getStoreName(storeId);
                    JSONObject[] jsonProducts=new JSONObject[bag.getProducts().size()];
                    int i=0;
                    for(Product product: bag.getProducts()){
                        JSONObject jsonProduct=new JSONObject();
                        jsonProduct.put("productName",product.getName());
                        jsonProduct.put("productId",product.getId());
                        jsonProduct.put("productAmount",bag.getProductsAmounts().get(product));
                        jsonProducts[i]=jsonProduct;
                        i++;
                    }
                    JSONObject jsonBag=new JSONObject();
                    jsonBag.put("storeId",storeId);
                    jsonBag.put("storeName",storeName);
                    jsonBag.put("products",jsonProducts);
                    jsonBags[j]=jsonBag;
                    j++;
                }
                jsonOut.put("bags",jsonBags);
                session.getRemote().sendString(jsonOut.toString());
            }else{
                JSONObject json= new JSONObject();
                json.put("type", "GET_CART");
                json.put("result",result.isResult());
                json.put("data",result.getData());
                session.getRemote().sendString(json.toString());
            }

        }
        else if(type.equals("LOGOUT"))
        {
            int id = Integer.valueOf(jo.get("id").toString());
            boolean result = API.registeredLogout(id).isResult();
            JSONObject json= new JSONObject();
            json.put("type", "LOGOUT");
            json.put("result",result);
            session.getRemote().sendString(json.toString());
        }

    }

}