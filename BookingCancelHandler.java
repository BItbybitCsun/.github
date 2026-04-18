
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BookingCancelHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, "Use POST");
            return;
        }

        File file = new File("bookings.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        String body = new String(exchange.getRequestBody().readAllBytes()).trim();
        if (body.isEmpty()) {
            send(exchange, "No booking selected");
            return;
        }

        int bookingId = Integer.parseInt(body);
        List<String> lines = Files.readAllLines(Paths.get("bookings.txt"));
        List<String> updated = new ArrayList<String>();
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 7) {
                int currentId = Integer.parseInt(parts[0]);
                if (currentId == bookingId) {
                    updated.add(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|" + parts[4] + "|" + parts[5] + "|CANCELED");
                    found = true;
                } else {
                    updated.add(line);
                }
            }
        }

        Files.write(Paths.get("bookings.txt"), updated);
        send(exchange, found ? "Booking canceled" : "Booking not found");
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