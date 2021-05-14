package Domain;

import java.util.Map;

public interface SupplementInterface {

    public Result handshake();

    public Result supply(Map<String, String> content);

    public Result cancelSupplement(int transactionId);
}
