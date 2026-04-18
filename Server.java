/*
    Module/Class Name: Server.java
    Date of Code: 03/15/26
    Programmer: Tony Ashvanian

    Brief Description: The Server.java class is the main entry point for the
    ByteBook backend. It starts an HTTP server on port 8000 and registers
    all URL routes to their corresponding handler classes.

    Important Functions:

    main(String[] args)
    Input: Command-line arguments (unused)
    Output: None
    Purpose: Creates and starts the HTTP server, binds each endpoint URL
    to its handler, and prints a confirmation message to the console.

    Important Data Structures: None - routes are registered directly on the
    HttpServer instance using context mappings.

    Algorithm/Design Used: Facade pattern - Server acts as a single entry
    point that hides the complexity of all individual handlers behind simple
    URL-to-handler mappings.
*/

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
