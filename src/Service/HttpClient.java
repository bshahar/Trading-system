package Service;

import Domain.Result;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class HttpClient {
    private URL url;

    public HttpClient(String externalSystemsUrl) {
        try {
            this.url = new URL(externalSystemsUrl);
        } catch (Exception e) {
        }
    }


    public Result sendRequest(Map<String, String> values) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            if(values.get("action_type") != null) {
                String str = "";
                switch (values.get("action_type")) {
                    case "handshake":
                        str = "action_type=handshake";
                        break;
                    case "pay":
                        str = "action_type=pay&" +
                                "card_number=" + values.get("card_number") + "&" +
                                "month=" + values.get("month") + "&" +
                                "year=" + values.get("year") + "&" +
                                "holder=" + values.get("holder") + "&" +
                                "ccv=" + values.get("ccv") + "&" +
                                "id=" + values.get("id");
                        break;
                    case "supply":
                        str = "action_type=supply&" +
                                "name=" + values.get("name") + "&" +
                                "address=" + values.get("address") + "&" +
                                "city=" + values.get("city") + "&" +
                                "country=" + values.get("country") + "&" +
                                "zip=" + values.get("zip");
                        break;
                    case "cancel_pay":
                        str = "action_type=cancel_pay&" +
                                "transaction_id=" + values.get("transaction_id");
                        break;
                    case "cancel_supply":
                        str = "action_type=cancel_supply&" +
                                "transaction_id=" + values.get("transaction_id");
                        break;
                }
                wr.writeBytes(str);
            }
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                if(line.equals("unexpected-output"))
                    response.append(String.valueOf(-1));
                else
                    response.append(line);
            }
            rd.close();
            connection.disconnect();
            return new Result(true, response.toString());
        } catch (Exception ex) {
            if (connection != null) connection.disconnect();
            return new Result(false, "Could not connect to external system.");
        }
    }

}
