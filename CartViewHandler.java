/*
Module/Class Name: CartViewHandler.java
Date of Code: 03/11/26
Programmer: Esmeralda Munoz
Brief Description: The CartViewHandler.java class retrieves 
all cart items from the cart file.

Important Functions:

handle(HttpExchange exchange)
Input: HTTP request
Output: Response text
Purpose: To retrieve all cart items from the cart file.

send(...)
Input: Response message
Output: Sends the message from the server to the frontend

Important Data Structures: List containing cart data from cart.txt

Algorithm/Design Used: Cart items are read from the cart file to be 
displayed on the cart page.
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CartViewHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        File file = new File("cart.txt");
        if (!file.exists()) file.createNewFile();

        List<String> lines = Files.readAllLines(Paths.get("cart.txt"));
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
