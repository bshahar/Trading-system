package Domain;

import java.io.IOException;
import java.util.Map;

public interface PaymentInterface {

    public Result handshake() throws IOException;

    public Result pay(Map<String, String> content);

    public Result cancelPayment(int transactionId);
}
