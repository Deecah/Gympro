package payment.payos;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class PayOSUtil {
    public static class PayOSResponse {
        public String checkoutUrl;
        public String error;
        public String rawResponse;
    }

    public static PayOSResponse createPaymentRequest(int orderCode, int amount, String description) {
        PayOSResponse result = new PayOSResponse();
        try {
            URL url = new URL(PayOSConfig.ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("x-client-id", PayOSConfig.CLIENT_ID);
            conn.setRequestProperty("x-api-key", PayOSConfig.API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format(
                "{\n" +
                "  \"orderCode\": %d,\n" +
                "  \"amount\": %d,\n" +
                "  \"description\": \"%s\",\n" +
                "  \"cancelUrl\": \"%s\",\n" +
                "  \"returnUrl\": \"%s\"\n" +
                "}", orderCode, amount, description,
                PayOSConfig.CANCEL_URL, PayOSConfig.RETURN_URL
            );

            System.out.println("[PayOS DEBUG] 🚀 Sending JSON to PayOS: \n" + json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int responseCode = conn.getResponseCode();
            System.out.println("[PayOS DEBUG] 🔁 HTTP Response: " + responseCode);

            Scanner scanner;
            if (responseCode >= 200 && responseCode < 300) {
                scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
            } else {
                scanner = new Scanner(conn.getErrorStream()).useDelimiter("\\A");
            }

            String response = scanner.hasNext() ? scanner.next() : "";
            result.rawResponse = response;
            System.out.println("[PayOS DEBUG] 📥 PayOS Response: \n" + response);

            try {
                JSONObject obj = new JSONObject(response);
                if (obj.has("data")) {
                    JSONObject data = obj.getJSONObject("data");
                    if (data.has("checkoutUrl")) {
                        result.checkoutUrl = data.getString("checkoutUrl");
                    }
                }
                if (obj.has("error")) {
                    result.error = obj.getString("error");
                }
            } catch (Exception parseEx) {
                result.error = "Lỗi parse JSON: " + parseEx.getMessage();
            }
            return result;
        } catch (Exception e) {
            System.out.println("[PayOS DEBUG] ❌ Exception khi gọi PayOS API:");
            e.printStackTrace();
            result.error = e.getMessage();
        }
        return result;
    }
}