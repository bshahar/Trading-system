package Service;

import Domain.PaymentInterface;
import Domain.Result;

import java.util.HashMap;
import java.util.Map;

public class PaymentAdapter implements PaymentInterface {

    HttpClient httpClient;

    public PaymentAdapter(String externalSystemsUrl) {
        this.httpClient = new HttpClient(externalSystemsUrl);
    }

    @Override
    public Result handshake() {
        Map<String, String> content = new HashMap<>();
        content.put("action_type", "handshake");
        return httpClient.sendRequest(content);
    }

    @Override
    public Result pay(Map<String, String> content) {
        if(handshake().getData().equals("OK")) {
            content.put("action_type", "pay");
            return httpClient.sendRequest(content);
        }
        return new Result(false, -1);
    }

    @Override
    public Result cancelPayment(int transactionId) {
        if(handshake().getData().equals("OK")) {
            Map<String, String> content = new HashMap<>();
            content.put("action_type", "cancel_pay");
            content.put( "transaction_id", String.valueOf(transactionId));
            /*if(httpClient.sendRequest(content).isResult()
                    && (Integer)httpClient.sendRequest(content).getData() == 1) //TODO check what comes back from server
             */
            return httpClient.sendRequest(content);
        }
        return new Result(false, -1);
    }
}
