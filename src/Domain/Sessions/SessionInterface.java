package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Queue;

public interface SessionInterface {
    public void send(String msg);
    public void send(int userId, int messageId, String msg);
    public void set(Session s);
    public void set();
    public Queue<String> getMsgs();
}
