package Server;

import Domain.Receipt;
import Domain.ReceiptLine;
import Domain.Result;
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
public class AdminWebSocketDay {
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
        if (type.equals("GET_SYS_STAT_DAY")) {
            try {
                String date=jo.getString("date");
                JSONObject jsonObject= (JSONObject) API.getSystemManagerStatsPerDay(date).getData();
                jsonObject.put("type","GET_SYS_STAT_DAY");
                session.getRemote().sendString(jsonObject.toString());
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

        }

    }
}
