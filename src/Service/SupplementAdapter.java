package Service;

import Domain.Result;
import Domain.SupplementInterface;

import java.util.HashMap;
import java.util.Map;

public class SupplementAdapter implements SupplementInterface {

    HttpClient httpClient;

    public SupplementAdapter(String externalSystemsUrl) {
        this.httpClient = new HttpClient(externalSystemsUrl);
    }

    @Override
    public Result handshake() {
        Map<String, String> content = new HashMap<>();
        content.put("action_type", "handshake");
        return httpClient.sendRequest(content);
    }

    @Override
    public Result supply(Map<String, String> content) {
        if(handshake().getData().equals("OK")) {
            content.put("action_type", "supply");
            return httpClient.sendRequest(content);
        }
        return new Result(false, -1);
    }

    @Override
    public Result cancelSupplement(int transactionId) {
        Result handshakeResult = handshake();
        if(handshakeResult.isResult()) {
            Map<String, String> content = new HashMap<>();
            content.put("action_type", "cancel_supply");
            content.put( "transaction_id", String.valueOf(transactionId));
            return httpClient.sendRequest(content);
        }
        return handshakeResult;
    }

}
