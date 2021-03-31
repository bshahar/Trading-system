import java.util.logging.Level;

public class Bag {
    private Store store;
    private User user;
    private State state;

    public Bag(Store store, User user, State state)
    {
        this.state = state;
        this.user = user;
        this.store = store;
    }
}
