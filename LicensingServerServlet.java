package com.bitmovin.bitdash.licensing.servlets;

import com.bitmovin.bitdash.licensing.requests.LicenseRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkroepfl on 11.02.16.
 */
public class LicensingServerServlet extends HttpServlet
{
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LicenseRequest licenseRequest;
        int httpCode;
        boolean allowLicense;
        Map<Object, Object> customData = new HashMap<>();

        try
        {
            licenseRequest = gson.fromJson(request.getReader(),LicenseRequest.class);

            String thirdPartyKey = (String)licenseRequest.getCustomData().get("thirdPartyKey");

            if(getLicenses().containsKey(thirdPartyKey))
            {
                allowLicense = getLicenses().get(thirdPartyKey);

                if (allowLicense)
                {
                    httpCode = 200;
                    customData.put("message", "Licensekey was approved.");
                    customData.put("param1", "data1");
                }
                else
                {
                    httpCode = 400;
                    customData.put("message", "Licensekey was denied.");
                    customData.put("param1", "data1");
                }
            }
            else
            {
                httpCode = 400;
                customData.put("message", "Licensekey was not found.");
                customData.put("param1", "data1");
            }

            sendResponse(response,httpCode,customData);
        }
        catch (Exception e)
        {
            customData = new HashMap<>();
            customData.put("message",e.getMessage());
            customData.put("param1", "data1");

            sendResponse(response, 500, customData);
        }
    }

    private void sendResponse(HttpServletResponse response, int httpCode, Map<Object, Object> customData) throws IOException
    {
        response.setStatus(httpCode);
        response.setContentType("application/json");

        response.getWriter().write(gson.toJson(customData));
    }

    private Map<String, Boolean> getLicenses()
    {
        Map<String, Boolean> licenses = new HashMap<>();

        licenses.put("key1",true);
        licenses.put("key2",false);

        return licenses;
    }
}
