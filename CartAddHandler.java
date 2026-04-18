
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