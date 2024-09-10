package com.example.squareapi.service;

import com.example.squareapi.model.DeviceCodeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;

@Service
public class SquareApiService {

    private static final String SQUARE_API_BASE = "https://connect.squareupsandbox.com/v2";
    private static final String WEBHOOK_URL = "https://webhook.site/token/9415a1f5-c4c9-4605-adfc-60b88888a703/requests";
    private static final String SQUARE_API_KEY = "Bearer EAAAl8jJdbTZDPtAyt121MViNzlCB5ANoTkPhQJpk8uY4XGNxBCPGR9eVLBMNRmL";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> createDeviceCode(String name, String location) {
        String url = SQUARE_API_BASE + "/devices/codes";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Square-Version", "2024-08-21");
        headers.set("Authorization", SQUARE_API_KEY);
        headers.set("Content-Type", "application/json");

        String requestBody = "{ \"device_code\": { \"product_type\": \"TERMINAL_API\", \"location_id\": \"" + location + "\", \"name\": \"" + name + "\" }, \"idempotency_key\": \"ff4bc375-a00a-46b6-8a1f-c337e3700646\" }";
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<DeviceCodeResponse> response = restTemplate.postForEntity(url, entity, DeviceCodeResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            // Extract the 'code' from response
            String extractedCode = response.getBody().getDeviceCode().getCode();

            // Prepare the response with key 'extractedCode'
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("extractedCode", extractedCode);

            // Return the response in the required format
            return responseBody;
        } else {
            throw new RuntimeException("Failed to create device code: " + response.getStatusCode());
        }
    }

    public String fetchDeviceIdFromWebhook() throws Exception {
    	System.out.println("Entered in the webhook flow");
        ResponseEntity<String> response = restTemplate.getForEntity(WEBHOOK_URL, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Failed to fetch data from Webhook. Status: " + response.getStatusCode());
        }

        // Parse the response JSON
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode dataArray = rootNode.path("data");
        System.out.println(dataArray);

        if (!dataArray.isArray()) {
            throw new Exception("Invalid Webhook data format");
        }

        // Loop through the array to find the matching entry
        for (JsonNode dataEntry : dataArray) {
            // Extract content field and parse it as JSON
            String content = dataEntry.path("content").asText();
            JsonNode contentNode = objectMapper.readTree(content);

            // Check if the type is "device.code.paired"
            String eventType = contentNode.path("type").asText();
            if ("device.code.paired".equals(eventType)) {
                // Extract the inner device code object and check if status is "PAIRED"
                JsonNode deviceCodeNode = contentNode.path("data").path("object").path("device_code");
                String status = deviceCodeNode.path("status").asText();

                if ("PAIRED".equals(status)) {
                    // Extract the device_id and return it
                    String deviceId = deviceCodeNode.path("device_id").asText();
                    System.out.println("Device Id from webhook" + deviceId);
                    return deviceId;
                }
            }
        }

        throw new Exception("No device found with type 'device.code.paired' and status 'PAIRED'.");
    }

 // Modified method to return a response String
    public String initiateCheckoutWithDeviceId(String deviceId) {
    	System.out.println("Entered in the Checkout flow");
        String checkoutUrl = "https://connect.squareupsandbox.com/v2/terminals/checkouts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Square-Version", "2024-08-21");
        headers.set("Authorization", "Bearer EAAAl8jJdbTZDPtAyt121MViNzlCB5ANoTkPhQJpk8uY4XGNxBCPGR9eVLBMNRmL");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create checkout request body
        String checkoutRequestBody = String.format(
                "{ \"checkout\": { \"amount_money\": { \"amount\": 100, \"currency\": \"USD\" }, " +
                        "\"device_options\": { \"device_id\": \"%s\", \"skip_receipt_screen\": false, " +
                        "\"tip_settings\": { \"allow_tipping\": true, \"custom_tip_field\": false, \"separate_tip_screen\": true }, " +
                        "\"collect_signature\": false }, \"note\": \"ice cream\" }, \"idempotency_key\": \"2104d6c0-1dc8-413d-be18-ce0642c01234\" }",
                deviceId
        );
        System.out.println("Checking value of Device Id" + deviceId);
        System.out.println("Checking the request body for Checkout API = " + checkoutRequestBody);

        // Create the request
        HttpEntity<String> entity = new HttpEntity<>(checkoutRequestBody, headers);

        // Send POST request to Square API for checkout
        ResponseEntity<String> checkoutResponse = restTemplate.exchange(checkoutUrl, HttpMethod.POST, entity, String.class);

        if (checkoutResponse.getStatusCode() == HttpStatus.OK) {
        	System.out.println("Response from Checkout API = " + checkoutResponse.getBody());
            return checkoutResponse.getBody();
        } else {
            return "Failed to initiate checkout. Status: " + checkoutResponse.getStatusCode();
        }
    }
}