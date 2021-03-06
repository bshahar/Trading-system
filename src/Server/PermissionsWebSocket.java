package Server;

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
public class PermissionsWebSocket {

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
        if (type.equals("GET_PERMISSIONS")) {
            int ownerId=Integer.parseInt(jo.get("userId").toString());
            String managerName=jo.get("managerName").toString();
            int storeId=Integer.parseInt(jo.get("storeId").toString());
            Result result= API.getUserPermissionsMap(ownerId,managerName,storeId);
            if(result.isResult()){
                Map<String,Boolean> permissionsBool= (Map<String,Boolean>)result.getData();
                JSONObject[] permissions= new JSONObject[permissionsBool.size()];
                int i=0;
                for(String per: permissionsBool.keySet()){
                    JSONObject perJson= new JSONObject();
                    perJson.put("permission",per);
                    perJson.put("allowed",permissionsBool.get(per));
                    permissions[i]=perJson;
                    i++;
                }
                JSONObject out= new JSONObject();
                out.put("type","GET_PERMISSIONS");
                out.put("result",result.isResult());
                out.put("data",permissions);
                session.getRemote().sendString(out.toString());


            }else{
                JSONObject jsonObject= new JSONObject();
                jsonObject.put("type","GET_PERMISSIONS");
                jsonObject.put("result",result.isResult());
                jsonObject.put("message",result.getData());
                session.getRemote().sendString(jsonObject.toString());

            }
        }else if(type.equals("UPDATE_PERMISSIONS")) {
            String userName= jo.get("userName").toString();
            int ownerId= Integer.parseInt(jo.get("ownerId").toString());
            int storeId =Integer.parseInt(jo.get("storeId").toString());
            JSONArray permissions= jo.getJSONArray("permissions");
            List<String> toAdd= new LinkedList<>();
            List<String> toRemove= new LinkedList<>();
            for(int i=0; i<permissions.length(); i++){
                JSONObject json= permissions.getJSONObject(i);
                if(json.getBoolean("isAllowed")){
                    toAdd.add(json.getString("per"));
                }else{
                    toRemove.add(json.getString("per"));
                }
            }
            API.addPermissions(ownerId,(int)API.getUserIdByName(userName).getData(),storeId,API.getpermissionsIndex(toAdd));
            API.RemovePermissions(ownerId,(int)API.getUserIdByName(userName).getData(),storeId,API.getpermissionsIndex(toRemove));
            JSONObject out= new JSONObject();
            out.put("type", "UPDATE_PERMISSIONS");
            out.put("result",true);
            session.getRemote().sendString(out.toString());

        }


    }

}