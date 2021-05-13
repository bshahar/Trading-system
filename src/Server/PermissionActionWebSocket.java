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
public class PermissionActionWebSocket {

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
        if (type.equals("ADD_PRODUCT")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            String prodName = jo.get("prodName").toString();
            String categories = jo.get("categories").toString();
            int price = Integer.parseInt(jo.get("price").toString());
            String description = jo.get("description").toString();
            int amount = Integer.parseInt(jo.get("amount").toString());
            List<String> arr = Arrays.asList(categories.split(","));
            Result result = API.addProduct(userId, storeId, prodName, arr, price, description, amount);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "ADD_PRODUCT");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getData());
            session.getRemote().sendString(jsonOut.toString());

        }else if (type.equals("EDIT_PRODUCT")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            int productId = Integer.parseInt(jo.get("productId").toString());
            int price = Integer.parseInt(jo.get("price").toString());
            int amount = Integer.parseInt(jo.get("amount").toString());
            Result result = API.editProduct(userId, storeId,productId,price, amount);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "EDIT_PRODUCT");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getData());
            session.getRemote().sendString(jsonOut.toString());

        }else if (type.equals("REMOVE_PRODUCT")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            int productId = Integer.parseInt(jo.get("productId").toString());
            Result result = API.removeProductFromStore(userId, storeId,productId);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "REMOVE_PRODUCT");
            jsonOut.put("result", result.isResult());
            jsonOut.put("message", result.getData());
            session.getRemote().sendString(jsonOut.toString());

        } else if (type.equals("ADD_MANAGER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getData();
                Result result2 = API.addStoreManager(ownerId, userId, storeId);
                JSONObject jsonOut = new JSONObject();
                jsonOut.put("type", "ADD_MANAGER");
                jsonOut.put("result", result2.isResult());
                jsonOut.put("message", result2.getData());
                session.getRemote().sendString(jsonOut.toString());
                String storeName = API.getStoreName(storeId);
            }

        } else if (type.equals("REMOVE_MANAGER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getData();
                Result result2 = API.removeManager(ownerId, userId, storeId);
                JSONObject jsonOut = new JSONObject();
                jsonOut.put("type", "REMOVE_MANAGER");
                jsonOut.put("result", result2.isResult());
                jsonOut.put("message", result2.getData());
                session.getRemote().sendString(jsonOut.toString());
                String storeName = API.getStoreName(storeId);
            }

        }else if (type.equals("ADD_OWNER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getData();
                Result result2 = API.addStoreOwner(ownerId, userId, storeId);
                JSONObject jsonOut = new JSONObject();
                jsonOut.put("type", "ADD_OWNER");
                jsonOut.put("result", result2.isResult());
                jsonOut.put("message", result2.getData());
                session.getRemote().sendString(jsonOut.toString());
                String storeName = API.getStoreName(storeId);
            }
        }else if (type.equals("REMOVE_OWNER")) {
            int ownerId = Integer.parseInt(jo.get("ownerId").toString());
            String userName = jo.get("userName").toString();
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getUserIdByName(userName);
            if (result.isResult()) {
                int userId = (int) result.getData();
                Result result2 = API.removeOwner(ownerId, userId, storeId);
                JSONObject jsonOut = new JSONObject();
                jsonOut.put("type", "REMOVE_OWNER");
                jsonOut.put("result", result2.isResult());
                jsonOut.put("message", result2.getData());
                session.getRemote().sendString(jsonOut.toString());
                String storeName = API.getStoreName(storeId);
            }
        }
        else if (type.equals("GET_WORKERS")) {
            int ownerId = Integer.parseInt(jo.get("userId").toString());
            int storeId = Integer.parseInt(jo.get("storeId").toString());
            Result result = API.getStoreWorkers(ownerId,storeId);
            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "GET_WORKERS");
            jsonOut.put("result", result.isResult());
            if(result.isResult()){
                List<User> users=(List<User> ) result.getData();
                JSONObject[] usersjson=new JSONObject[users.size()];
                int i=0;
                for(User user: users){
                    JSONObject userJson=new JSONObject();
                    userJson.put("userId",user.getId());
                    userJson.put("userName",user.getUserName());
                    usersjson[i]=userJson;
                    i++;
                }
                jsonOut.put("data",usersjson);
            }else{

                jsonOut.put("message", result.getData());
            }
            session.getRemote().sendString(jsonOut.toString());

        }else if(type.equals("GET_PURCHASES")){
            int userId=Integer.parseInt(jo.get("userId").toString());
            int storeId=Integer.parseInt(jo.get("storeId").toString());
            Result result= API.getStorePurchaseHistory(userId,storeId);
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
            }
        }
    }

}

