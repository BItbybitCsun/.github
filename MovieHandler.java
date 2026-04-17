
import com.sun.net.httpserver.*;
import java.io.*;
import java.util.*;

public class MovieHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        List<Movie> movies = Arrays.asList(
            new Movie(1, "Minecraft Movie (3D)", "6:00 PM", 12.99),
            new Movie(2, "Sonic 3", "7:30 PM", 11.99),
            new Movie(3, "Spider-Man", "9:00 PM", 13.99)
        );

        String result = "";
        for (int i = 0; i < movies.size(); i++) {
            result += movies.get(i).toLine();
            if (i < movies.size() - 1) {
                result += ",";
            }
        }

        send(exchange, result);
    }

    private void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
