/*Module/Class Name: ReportHandler.java
Date of Code: March 10
Programmer: Gor
Brief Description:
The ReportHandler.java class handles HTTP requests and generates a report from the bookings.txt file. 
It calculates total bookings, booked and canceled counts, total tickets.
Important Functions:
handle(HttpExchange exchange)
Input: HTTP request (exchange)
Output: Sends report as text 
Reads the file, processes booking data, and calculates totals.
send(HttpExchange exchange, String response)
Input: HTTP exchange and response string
Output: Sends response to client
Sets headers and writes the response.
Important Data Structures:
Uses a List<String> to store file lines and String[] to split each line into parts (like price, quantity, status).
Algorithm/Design Used:
Uses a simple loop to go through each line and calculate totals. This is used because it is easy and efficient for processing file data line by line. */

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReportHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        File file = new File("bookings.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        List<String> lines = Files.readAllLines(Paths.get("bookings.txt"));
        int total = lines.size();
        int booked = 0;
        int canceled = 0;
        double revenue = 0.0;
        int totalTickets = 0;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 7) {
                String status = parts[6];
                double price = Double.parseDouble(parts[4]);
                int quantity = Integer.parseInt(parts[5]);

                totalTickets += quantity;

                if (status.equals("BOOKED")) {
                    booked++;
                    revenue += price * quantity;
                }
                if (status.equals("CANCELED")) {
                    canceled++;
                }
            }
        }

        String response = "Total bookings: " + total +
                "\nBooked: " + booked +
                "\nCanceled: " + canceled +
                "\nTotal tickets: " + totalTickets +
                "\nRevenue: $" + String.format("%.2f", revenue);

        send(exchange, response);
    }

    private void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
