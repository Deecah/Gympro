package Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRateUtil {

    private static final BigDecimal DEFAULT_EXCHANGE_RATE = new BigDecimal("26000.00");

    public static BigDecimal fetchVNDToUSD() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.exchangerate.host/latest?base=VND&symbols=USD")
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonObject rates = jsonObject.getAsJsonObject("rates");
            
            if (rates == null) {
                System.err.println("Rates object is null in API response");
                return DEFAULT_EXCHANGE_RATE;
            }

            double usdRate = rates.get("USD").getAsDouble();

            BigDecimal rate = BigDecimal.valueOf(usdRate).setScale(8, RoundingMode.HALF_UP);

            System.out.println("Live exchange rate VND → USD: " + rate);
            return rate;

        } catch (Exception e) {
            System.err.println("Failed to fetch live exchange rate. Using fallback: " + e.getMessage());
            return DEFAULT_EXCHANGE_RATE;
        }
    }
}
