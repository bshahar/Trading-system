package Server;

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
public class StoreWebSocket {

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
        if(type.equals("GET_PRODUCTS")){
            int storeId = Integer.valueOf(jo.get("storeId").toString());
            List<Product> products =(List<Product>) API.getAllStoreProducts(storeId).getData();
            JSONObject json= new JSONObject();
            json.put("type", "PRODUCTS");
            JSONObject[] jsonProducts=new JSONObject[products.size()];
            int i=0;
            for(Product product: products){
                JSONObject jsonProduct= new JSONObject();
                jsonProduct.put("productName",product.getName());
                jsonProduct.put("productId",product.getId());
                jsonProduct.put("price",product.getPrice());
                jsonProduct.put("amount",product.getAmount());
                jsonProducts[i]=jsonProduct;
                i++;
            }
            json.put("items",jsonProducts);
            System.out.println(json);
            session.getRemote().sendString(json.toString());
        }
        else if(type.equals("LOGOUT"))
        {
            int id = Integer.valueOf(jo.get("id").toString());
            boolean result = API.registeredLogout(id).isResult();
            JSONObject json= new JSONObject();
            json.put("type", "LOGOUT");
            json.put("result",result);
            session.getRemote().sendString(json.toString());
        }else if(type.equals("ADD_PRODUCT")){
            int userId=Integer.valueOf(jo.get("userId").toString());
            int storeId= Integer.valueOf(jo.get("storeId").toString());
            int productId= Integer.valueOf(jo.get("productId").toString());
            int amount= Integer.valueOf(jo.get("amount").toString());
            boolean result=API.addProductToCart(userId,storeId,productId,amount).isResult();
            JSONObject json= new JSONObject();
            json.put("type", "ADD_PRODUCT");
            json.put("result",result);
            session.getRemote().sendString(json.toString());
        }else if(type.equals("SET_OFFER")){
            int userId=jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int amount= jo.getInt("amount");
            int price= jo.getInt("price");
            Result result=API.addPurchaseOffer(storeId,userId,productId,price,amount);
            JSONObject json= new JSONObject();
            json.put("type", "SET_OFFER");
            json.put("result",result.isResult());
            if(result.isResult()){
                json.put("message","Bid has been offered, please wait for the store's manager");
            }else{
                json.put("message",result.getData());
            }
            session.getRemote().sendString(json.toString());
        }

    }

}