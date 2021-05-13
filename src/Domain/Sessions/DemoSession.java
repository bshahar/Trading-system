package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

import java.util.LinkedList;
import java.util.List;

public class DemoSession implements  SessionInterface{

    private List<String > msgs = new LinkedList<>();

    public void send(String msg) {
        this.msgs.add(msg);
    }

    @Override
    public void set(Session s) {

    }

    public List<String> getMsgs() {
        return msgs;
    }
}
