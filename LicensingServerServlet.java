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
        boolean allowLicense;
        Map<Object, Object> customData = new HashMap<>();

        try
        {
            licenseRequest = gson.fromJson(request.getReader(),LicenseRequest.class);

            String thirdPartyKey = (String)licenseRequest.getCustomData().get("thirdPartyKey");

            if(getLicenses().containsKey(thirdPartyKey))
            {
                customData.put("param1", "data1");
                sendDenyResponse(response, customData);
                return;
            }

            allowLicense = getLicenses().get(thirdPartyKey);

            if (!allowLicense)
            {
                customData.put("param1", "data1");
                sendDenyResponse(response, customData);
                return;
            }

            customData.put("param1", "data1");
            sendAllowResponse(response, customData);
        }
        catch (Exception e)
        {
            customData = new HashMap<>();
            customData.put("param1", "data1");

            sendErrorResponse(response, customData, e.getMessage());
        }
    }

    private void sendAllowResponse(HttpServletResponse response, Map<Object, Object> customData) throws IOException
    {
        customData.put("message", "Licensekey was approved.");
        response.setStatus(200);
        response.setContentType("application/json");

        response.getWriter().write(gson.toJson(customData));
    }

    private void sendDenyResponse(HttpServletResponse response, Map<Object, Object> customData) throws IOException
    {
        customData.put("message", "Licensekey was denied.");
        response.setStatus(400);
        response.setContentType("application/json");

        response.getWriter().write(gson.toJson(customData));
    }

    private void sendErrorResponse(HttpServletResponse response, Map<Object, Object> customData, String error) throws IOException
    {
        customData.put("message", error);
        response.setStatus(500);
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

