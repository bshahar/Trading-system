package Server;

import Domain.Product;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.Result;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.json.JSONArray;
import org.json.JSONObject;

@WebSocket
public class BidsWebSocket {

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
        if(type.equals("GET_BIDS")){
            int storeId = jo.getInt("storeId");
            int userId = jo.getInt("userId");
            Result result= API.getOffersForStore(storeId,userId);
            JSONObject json= new JSONObject();
            json.put("type", "GET_BIDS");
            json.put("result", result.isResult());
            if(result.isResult()){
                Map<PurchaseOffer,Product> offers=(Map<PurchaseOffer,Product>) result.getData();
                JSONObject []jsonoffers=new JSONObject[offers.size()];
                int counter=0;
                for(PurchaseOffer purchaseOffer : offers.keySet()){
                    JSONObject offer=new JSONObject();
                    offer.put("offerId",purchaseOffer.getId());
                    offer.put("amount",purchaseOffer.getNumOfProd());
                    offer.put("price",purchaseOffer.getPriceOfOffer());
                    Product product= offers.get(purchaseOffer);
                    offer.put("productId",product.getId());
                    offer.put("productName",product.getName());
                    jsonoffers[counter]=offer;
                    counter++;
                }
                json.put("offers",jsonoffers);
            }else{
                json.put("message",result.getData());
            }
            session.getRemote().sendString(json.toString());
        }


    }

}