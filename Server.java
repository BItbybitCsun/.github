
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/login", new LoginHandler());
        server.createContext("/movies", new MovieHandler());
        server.createContext("/cart/add", new CartAddHandler());
        server.createContext("/cart/view", new CartViewHandler());
        server.createContext("/cart/remove", new CartRemoveHandler());
        server.createContext("/bookings/create", new BookingCreateHandler());
        server.createContext("/bookings/view", new BookingViewHandler());
        server.createContext("/bookings/cancel", new BookingCancelHandler());
        server.createContext("/report", new ReportHandler());

        server.start();
        System.out.println("Server running at http://localhost:8000");
    }
}
