
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
