
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BookingCreateHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        File cartFile = new File("cart.txt");
        File bookingsFile = new File("bookings.txt");
        if (!cartFile.exists()) {
            cartFile.createNewFile();
        }
        if (!bookingsFile.exists()) {
            bookingsFile.createNewFile();
        }

        List<String> cartLines = Files.readAllLines(Paths.get("cart.txt"));
        if (cartLines.isEmpty()) {
            send(exchange, "Cart is empty");
            return;
        }

        List<String> bookingLines = Files.readAllLines(Paths.get("bookings.txt"));
        int nextId = bookingLines.size() + 1;

        FileWriter fw = new FileWriter("bookings.txt", true);
        for (String line : cartLines) {
            String[] parts = line.split("\\|");
            if (parts.length == 5) {
                Booking booking = new Booking(
                    nextId,
                    Integer.parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    Double.parseDouble(parts[3]),
                    Integer.parseInt(parts[4]),
                    "BOOKED"
                );
                fw.write(booking.toLine() + "\n");
                nextId++;
            }
        }
        fw.close();

        Files.write(Paths.get("cart.txt"), new ArrayList<String>());
        send(exchange, "Booking created");
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