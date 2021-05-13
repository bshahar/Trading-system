package Domain.Sessions;

import org.eclipse.jetty.websocket.api.Session;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DemoSession implements  SessionInterface{

    private Queue<String > msgs = new LinkedList<>();

    public void send(String msg) {
        this.msgs.add(msg);
    }

    @Override
    public void set(Session s) {

    }

    @Override
    public void set() {

    }


    public Queue<String> getMsgs() {
        return msgs;
    }
}
