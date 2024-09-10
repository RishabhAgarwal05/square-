package com.example.squareapi.controller;

import com.example.squareapi.service.SquareApiService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private SquareApiService squareApiService;

    @PostMapping("/create")
    public String createDeviceCode(@RequestParam String name, @RequestParam String location) {
    //public String initiateCheckout() {  
    try {
            // Call the Square API to create a device code
    	Map<String, String> response = squareApiService.createDeviceCode(name, location);
    	System.out.println("Response from Device Code API = " + response);

            // Fetch data from webhook.site
            String deviceId = squareApiService.fetchDeviceIdFromWebhook();

         // Initiate a checkout request
            String checkoutResponse = squareApiService.initiateCheckoutWithDeviceId(deviceId);

            return checkoutResponse;
        } catch (Exception e) {
        	return "Error: " + e.getMessage();
        }
    }
}
