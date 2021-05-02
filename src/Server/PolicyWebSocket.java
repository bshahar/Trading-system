package Server;

import Domain.Result;
import Service.API;
import javafx.util.Pair;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import org.json.JSONArray;
import org.json.JSONObject;

@WebSocket
public class PolicyWebSocket {

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
        try{
            JSONObject jo = new JSONObject(message);
            String type = jo.get("type").toString();
            if (type.equals("ADD_DISCOUNT_POLICY_PRODUCT")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                int prodId= jo.getInt("productId");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("operation").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    for(int j=0; j<jsonParams.length();j++){
                        params.add(jsonParams.getString(j));
                    }
                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountOnProduct(storeId,userId,prodId,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_DISCOUNT_POLICY_PRODUCT");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            }else if (type.equals("ADD_DISCOUNT_POLICY_CATEGORY")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                String category= jo.getString("category");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("mathOp").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    for(int j=0; j<jsonParams.length();j++){
                        params.add(jsonParams.getString(j));
                    }
                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountPolicyOnCategory(storeId,userId,category,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_DISCOUNT_POLICY_CATEGORY");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            } else if (type.equals("ADD_DISCOUNT_POLICY_STORE")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("operation").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    params.add(jsonParams.getString(0));
                    params.add(jsonParams.getString(1));
                    params.add(jsonParams.getString(2));
                    params.add(jsonParams.getString(3));
                    params.add(jsonParams.getString(4));
                    params.add(jsonParams.getString(5));
                    params.add(jsonParams.getString(6));

                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountPolicyOnStore(storeId,userId,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_DISCOUNT_POLICY_STORE");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            }else if (type.equals("ADD_PURCHASE_POLICY_PRODUCT")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                int prodId= jo.getInt("productId");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("operation").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    for(int j=0; j<jsonParams.length();j++){
                        params.add(jsonParams.getString(j));
                    }
                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountOnProduct(storeId,userId,prodId,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_PURCHASE_POLICY_PRODUCT");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            }else if (type.equals("ADD_PURCHASE_POLICY_CATEGORY")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                String category= jo.getString("category");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("mathOp").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    for(int j=0; j<jsonParams.length();j++){
                        params.add(jsonParams.getString(j));
                    }
                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountPolicyOnCategory(storeId,userId,category,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_PURCHASE_POLICY_CATEGORY");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            } else if (type.equals("ADD_PURCHASE_POLICY_STORE")) {
                int userId= jo.getInt("userId");
                int storeId= jo.getInt("storeId");
                String operator= jo.get("operation").toString();
                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                Date begin= format.parse(jo.get("begin").toString());
                Date end= format.parse(jo.get("end").toString());
                int precentage= jo.getInt("presentage");
                String mathOp= jo.get("operation").toString();
                JSONArray arr=jo.getJSONArray("list");
                List<Pair<String,List<String>>> policies=new LinkedList<>();
                for(int i=0; i<arr.length();i++){
                    JSONObject policy=arr.getJSONObject(i);
                    String name=policy.getString("policyName");
                    JSONArray jsonParams= policy.getJSONArray("params");
                    List<String> params =new LinkedList<>();
                    params.add(jsonParams.getString(0));
                    params.add(jsonParams.getString(1));
                    params.add(jsonParams.getString(2));
                    params.add(jsonParams.getString(3));
                    params.add(jsonParams.getString(4));
                    params.add(jsonParams.getString(5));
                    params.add(jsonParams.getString(6));

                    Pair<String ,List<String>> pair= new Pair<>(name,params);
                    policies.add(pair);
                }
                Result result=API.addDiscountPolicyOnStore(storeId,userId,operator,policies,begin,end,precentage,mathOp);
                JSONObject out=new JSONObject();
                out.put("type","ADD_PURCHASE_POLICY_STORE");
                out.put("result",result.isResult());
                out.put("message",result.getData());
                session.getRemote().sendString(out.toString());
            }

        }catch (Exception e){

        }


    }

}