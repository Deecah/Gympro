/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payment.paypal;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.util.HashMap;
import java.util.Map;

public class PayPalConfig {
    private static final String CLIENT_ID = "AZLgshcLtPaVGeXQ3m90UDWLI4wMTABX0tan5OjT0LAU5NGtbwyUBBm2fKOdz-JC0so8XnTeFEkHTBrm";
    private static final String CLIENT_SECRET = "EB6EBNPXPO-mUwmvL2skpGW6L1-r63BO7qNL7YVuTd3Pb-X1nBzSJmEWeYClnsIm-3sAFrliw4aj9IwO";
    private static final String MODE = "sandbox"; 

    public static APIContext getAPIContext() throws PayPalRESTException {
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", MODE);
        OAuthTokenCredential authTokenCredential = new OAuthTokenCredential(CLIENT_ID, CLIENT_SECRET, sdkConfig);
        APIContext apiContext = new APIContext(authTokenCredential.getAccessToken());
        apiContext.setConfigurationMap(sdkConfig);
        return apiContext;
    }
}
