package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

public class realSession implements SessionInterface{
    Session session;

    public void set(Session s)
    {
        this.session = s;
    }

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
}
