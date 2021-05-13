package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

public interface SessionInterface {
    public void send(String msg);
    public void set(Session s);
    public void set();
}
