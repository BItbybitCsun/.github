
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CartRemoveHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, "Use POST");
            return;
        }

        File file = new File("cart.txt");
        if (!file.exists()) file.createNewFile();

        String body = new String(exchange.getRequestBody().readAllBytes()).trim();
        if (body.isEmpty()) {
            send(exchange, "No cart item selected");
            return;
        }

        int index = Integer.parseInt(body);
        List<String> lines = Files.readAllLines(Paths.get("cart.txt"));
        List<String> updated = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {
            if (i != index) {
                updated.add(lines.get(i));
            }
        }

        Files.write(Paths.get("cart.txt"), updated);
        send(exchange, "Removed from cart");
    }

    private void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
