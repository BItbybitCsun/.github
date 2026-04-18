/*
Module/Class Name: CartAddHandler.java
Date of Code: 03/11/26
Programmer: Esmeralda Munoz
Brief Description: The CartAddHandler.java class appends 
the selected movie to the cart file.

Important Functions:

handle(HttpExchange exchange)
Input: HTTP request
Output: Response text
Purpose: Appends the cart data to the cart file.

Important Data Structures: Cart data stored in cart.txt

Algorithm/Design Used: The cart data is added to the cart file after 
the user adds items to their cart.

*/
import com.sun.net.httpserver.*;
import java.io.*;

public class CartAddHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, "Use POST");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes()).trim();
        if (body.isEmpty()) {
            send(exchange, "No movie selected");
            return;
        }

        File file = new File("cart.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter("cart.txt", true);
        fw.write(body + "\n");
        fw.close();

        send(exchange, "Added to cart");
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