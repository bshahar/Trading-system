package Server;

import Domain.Filter;
import Domain.Product;
import Domain.Result;
import Service.API;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;

@WebSocket
public class SearchProductsWebSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        JSONObject jo = new JSONObject(message);
        String type = jo.get("type").toString();
        if (type.equals("SEARCH_PRODUCTS")) {
            int userId = Integer.parseInt(jo.get("userId").toString());
            String searchType = jo.get("searchType").toString();
            String param = jo.get("param").toString();
            String minPrice = jo.get("minPrice").toString();
            String maxPrice = jo.get("maxPrice").toString();
            String prodRank = jo.get("prodRank").toString();
            String storeRank = jo.get("storeRank").toString();
            String category = jo.get("category").toString();

            int minPriceInt;
            if (minPrice.equals("")) {
                minPriceInt = Integer.MIN_VALUE;
            } else {
                minPriceInt = Integer.parseInt(minPrice);
            }
            int maxPriceInt;
            if (maxPrice.equals("")) {
                maxPriceInt = Integer.MAX_VALUE;
            } else {
                maxPriceInt = Integer.parseInt(maxPrice);
            }
            int prodRankInt;
            if (prodRank.equals("")) {
                prodRankInt = -1;
            } else {
                prodRankInt = Integer.parseInt(prodRank);
            }
            int storeRankInt;
            if (storeRank.equals("")) {
                storeRankInt = -1;
            } else {
                storeRankInt = Integer.parseInt(storeRank);
            }
            if (param.equals("")) {
                JSONObject out = new JSONObject();
                out.put("type", "SEARCH_PRODUCTS");
                out.put("result", false);
                out.put("message", "Parameter can't be empty");
                session.getRemote().sendString(out.toString());
            } else {
                Filter filter = new Filter(searchType, param, minPriceInt, maxPriceInt, prodRankInt, category, storeRankInt);
                Result result = API.searchProduct(filter, userId);
                if (!result.isResult()) {
                    JSONObject out = new JSONObject();
                    out.put("type", "SEARCH_PRODUCTS");
                    out.put("result", result.isResult());
                    out.put("message", result.getData());
                    session.getRemote().sendString(out.toString());
                } else {
                    Map<Integer, Integer> storeProdId = ( Map<Integer, Integer>)result.getData();
                    JSONObject[] prodsJson = new JSONObject[storeProdId.size()];
                    int i = 0;
                    for (Integer prodId : storeProdId.keySet()) {
                        Product product = API.getProductById(prodId);
                        JSONObject prod = new JSONObject();
                        prod.put("productName", product.getName());
                        prod.put("price", product.getPrice());
                        prod.put("amount", API.getProductAmount(prodId));
                        prod.put("productId", product.getId());
                        prod.put("storeId", storeProdId.get(prodId));
                        prodsJson[i] = prod;
                        i++;
                    }
                    JSONObject out = new JSONObject();
                    out.put("type", "SEARCH_PRODUCTS");
                    out.put("products", prodsJson);
                    out.put("result", result.isResult());
                    session.getRemote().sendString(out.toString());

                }

            }

        }


    }

}