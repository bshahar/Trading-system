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
        if (type.equals("GET_BIDS")) {
            int storeId = jo.getInt("storeId");
            int userId = jo.getInt("userId");
            Result result = API.getOffersForStore(storeId, userId);
            JSONObject json = new JSONObject();
            json.put("type", "GET_BIDS");
            json.put("result", result.isResult());
            if (result.isResult()) {
                Map<PurchaseOffer, Product> offers = (Map<PurchaseOffer, Product>) result.getData();
                JSONObject[] jsonoffers = new JSONObject[offers.size()];
                int counter = 0;
                for (PurchaseOffer purchaseOffer : offers.keySet()) {
                    JSONObject offer = new JSONObject();
                    offer.put("offerId", purchaseOffer.getId());
                    offer.put("amount", purchaseOffer.getNumOfProd());
                    offer.put("price", purchaseOffer.getPriceOfOffer());
                    Product product = offers.get(purchaseOffer);
                    offer.put("productId", product.getId());
                    offer.put("productName", product.getName());
                    offer.put("userName", purchaseOffer.getUser().getUserName());
                    jsonoffers[counter] = offer;
                    counter++;
                }
                json.put("offers", jsonoffers);
            } else {
                json.put("message", result.getData());
            }
            session.getRemote().sendString(json.toString());
        } else if (type.equals("APPROVE_OFFER")) {
            int userId= jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int offerId= jo.getInt("offerId");
            Result result = API.approvePurchaseOffer(storeId,userId,productId,offerId);
            JSONObject out= new JSONObject();
            out.put("type", "APPROVE_OFFER");
            out.put("result",result.isResult());
            out.put("message",result.getData());
            session.getRemote().sendString(out.toString());
        }else if (type.equals("DECLINE_OFFER")) {
            int userId= jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int offerId= jo.getInt("offerId");
            Result result = API.disapprovePurchaseOffer(storeId,userId,productId,offerId);
            JSONObject out= new JSONObject();
            out.put("type", "DECLINE_OFFER");
            out.put("result",result.isResult());
            out.put("message",result.getData());
            session.getRemote().sendString(out.toString());
        }else if (type.equals("COUNTER_OFFER")) {
            int userId= jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int offerId= jo.getInt("offerId");
            int price = jo.getInt("price");
            Result result = API.counterPurchaseOffer(storeId,userId,productId,offerId,price);
            JSONObject out= new JSONObject();
            out.put("type", "COUNTER_OFFER");
            out.put("result",result.isResult());
            out.put("message",result.getData());
            session.getRemote().sendString(out.toString());
        }else if (type.equals("GET_COUNTER_BIDS")) {
            int userId = jo.getInt("userId");
            Result result = API.getOffersForCostumer( userId);
            JSONObject json = new JSONObject();
            json.put("type", "GET_COUNTER_BIDS");
            json.put("result", result.isResult());
            if (result.isResult()) {
                Map<PurchaseOffer, Product> offers = (Map<PurchaseOffer, Product>) result.getData();
                JSONObject[] jsonoffers = new JSONObject[offers.size()];
                int counter = 0;
                for (PurchaseOffer purchaseOffer : offers.keySet()) {
                    JSONObject offer = new JSONObject();
                    offer.put("offerId", purchaseOffer.getId());
                    offer.put("amount", purchaseOffer.getNumOfProd());
                    offer.put("price", purchaseOffer.getPriceOfOffer());
                    Product product = offers.get(purchaseOffer);
                    offer.put("productId", product.getId());
                    offer.put("storeId",product.getStoreId());
                    offer.put("productName", product.getName());
                    offer.put("storeName", API.getStoreName(product.getStoreId()));
                    jsonoffers[counter] = offer;
                    counter++;
                }
                json.put("offers", jsonoffers);
            } else {
                json.put("message", result.getData());
            }
            session.getRemote().sendString(json.toString());
        } else if (type.equals("APPROVE_COUNTER_OFFER")) {
            int userId= jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int offerId= jo.getInt("offerId");
            Result result = API.approveCounterOffer(storeId,userId,productId,true);
            JSONObject out= new JSONObject();
            out.put("type", "APPROVE_COUNTER_OFFER");
            out.put("result",result.isResult());
            out.put("message",result.getData());
            session.getRemote().sendString(out.toString());
        }else if (type.equals("DECLINE_COUNTER_OFFER")) {
            int userId= jo.getInt("userId");
            int storeId= jo.getInt("storeId");
            int productId= jo.getInt("productId");
            int offerId= jo.getInt("offerId");
            Result result = API.rejectCounterOffer(storeId,userId,productId,false);
            JSONObject out= new JSONObject();
            out.put("type", "DECLINE_COUNTER_OFFER");
            out.put("result",result.isResult());
            out.put("message",result.getData());
            session.getRemote().sendString(out.toString());
        }

    }

}