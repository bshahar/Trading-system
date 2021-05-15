package Server;

import Domain.*;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class myPurchases {

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
        if(type.equals("GET_PURCHASES")){
            int userId=Integer.parseInt(jo.get("userId").toString());
            Result result= API.getUserPurchaseHistory(userId);
            JSONObject jsonOut=new JSONObject();
            jsonOut.put("type","GET_PURCHASES");
            if(result.isResult()){
                List<Receipt> receipts=(List<Receipt>)result.getData();
                JSONObject[] receiptsJson=new JSONObject[receipts.size()];
                int j=0;
                for(Receipt receipt : receipts){
                    JSONObject[] linesJson= new JSONObject[receipt.getLines().size()];
                    int i=0;
                    for(Receipt.ReceiptLine receiptLine : receipt.getLines()){
                        JSONObject receiptLineJson= new JSONObject();
                        receiptLineJson.put("prodName",receiptLine.getProdName());
                        receiptLineJson.put("price",receiptLine.getPrice());
                        receiptLineJson.put("amount",receiptLine.getAmount());
                        linesJson[i]=receiptLineJson;
                        i++;
                    }
                    JSONObject receiptJson= new JSONObject();
                    receiptJson.put("storeName", API.getStoreName(receipt.getStoreId()));
                    receiptJson.put("userName", receipt.getUserName());

                    receiptJson.put("totalCost",receipt.getTotalCost());
                    receiptJson.put("lines",linesJson);
                    receiptsJson[j]=receiptJson;
                    j++;
                }
                jsonOut.put("result",true);
                jsonOut.put("data",receiptsJson);
                session.getRemote().sendString(jsonOut.toString());

            }else{
                jsonOut.put("result",false);
                jsonOut.put("message",result.getData());
                session.getRemote().sendString(jsonOut.toString());

            }
        }
    }

}

