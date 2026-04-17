
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