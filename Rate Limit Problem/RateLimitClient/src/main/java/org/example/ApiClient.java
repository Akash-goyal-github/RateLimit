package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ApiClient {
    private static final String API_URL = "http://localhost:8080/api/call?input=";
    private static final int MAX_CALLS_PER_MINUTE = 15; // Max allowed calls
    private static final long TIME_FRAME_MS = 60000; // 1 minute in milliseconds
    private static final BlockingQueue<String> requestQueue = new ArrayBlockingQueue<>(100); // Queue to hold requests

    public static void main(String[] args) {
        ApiClient client = new ApiClient();

        // Start a thread to process the queue
        new Thread(client::processRequests).start();

        // Simulate making 20 API calls
        for (int i = 1; i <= 20; i++) {
            String input = "Request " + i;
            try {
                requestQueue.put(input);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Failed to add request to queue: " + e.getMessage());
            }
        }
    }

    private void processRequests() {
        int callCount = 0; // Counter for calls made in the current minute
        long startTime = System.currentTimeMillis(); // Record start time of the current period

        while (true) {
            try {
                // Take an item from the queue, blocking if necessary
                String input = requestQueue.take();
                String response = callApi(input);
                System.out.println("Response for " + input + ": " + response);
                callCount++;

                // Check if we have reached the limit of calls for the current minute
                if (callCount >= MAX_CALLS_PER_MINUTE) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime < TIME_FRAME_MS) {
                        // Wait for the remainder of the minute if the limit has been reached

                        System.out.println("Waiting for 1 min to avoid rate limit");
                        Thread.sleep(TIME_FRAME_MS);
                    }
                    // Reset the counter and start time for the next period
                    callCount = 0;
                    startTime = System.currentTimeMillis();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Request processing interrupted: " + e.getMessage());
            }
        }
    }

    private String callApi(String input) {
        StringBuilder response = new StringBuilder();
        try {
            String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            URL url = new URL(API_URL + encodedInput);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("client", "akash1");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                response.append("Error: ").append(responseCode);
            }
        } catch (Exception e) {
            response.append("Exception: ").append(e.getMessage());
        }
        return response.toString();
    }
}

