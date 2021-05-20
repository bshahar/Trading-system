package Domain;

import java.util.Map;

public class DemoSupplement implements SupplementInterface{

    @Override
    public Result handshake() {
        return new Result(true, "OK");
    }

    @Override
    public Result supply(Map<String, String> content) {
        return new Result(true, 10001);
    }

    @Override
    public Result cancelSupplement(int transactionId) {
        return new Result(true, 1);
    }
}
