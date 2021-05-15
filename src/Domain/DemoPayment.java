package Domain;

import java.util.Map;

public class DemoPayment implements PaymentInterface{

    @Override
    public Result handshake() {
        return new Result(true, "OK");
    }

    @Override
    public Result pay(Map<String, String> content) {
        return new Result(true, 10001);
    }

    @Override
    public Result cancelPayment(int transactionId) {
        return new Result(true, 1);
    }
}
