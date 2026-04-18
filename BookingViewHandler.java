/*
Module/Class Name: BookingViewHandler.java
Date of Code: 02/29/26
Programmer: Esmeralda Munoz
Brief Description:The BookingViewHandler.java class 
retrieves the bookings from the bookings file.

Important Functions:

handle(HttpExchange exchange)
Input: HTTP request
Output: Response text
Purpose: Retrieves all booking objects from the bookings file.

send(...)
Input: Response message
Output: Sends response from server to the frontend

Important Data Structures: List of bookings from the bookings.txt file

Algorithm/Design Used: All bookings are read from the bookings file to
display on the booking view page.
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BookingViewHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        File file = new File("bookings.txt");
        if (!file.exists()) file.createNewFile();

        List<String> lines = Files.readAllLines(Paths.get("bookings.txt"));
        send(exchange, String.join(",", lines));
    }

    private void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
