package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Queue;

public class realSession implements SessionInterface{
    Session session;

    public void send(String msg) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(msg);
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void send(int userId, int messageId, String msg) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(msg);
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void set(Session s) {
        this.session = s;
    }

    @Override
    public void set() {

    }

    @Override
    public Queue<String> getMsgs() {
        return null;
    }
}
