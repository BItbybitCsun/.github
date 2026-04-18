
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LoginHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, "Login failed");
            return;
        }

        File usersFile = new File("users.txt");
        if (!usersFile.exists()) {
            FileWriter fw = new FileWriter(usersFile);
            fw.write("john|1234\n");
            fw.write("admin|admin123\n");
            fw.close();
        }

        String body = new String(exchange.getRequestBody().readAllBytes()).trim();
        String[] loginParts = body.split("\\|");

        if (loginParts.length != 2) {
            send(exchange, "Login failed");
            return;
        }

        String username = loginParts[0];
        String password = loginParts[1];

        List<String> lines = Files.readAllLines(Paths.get("users.txt"));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                User user = new User(parts[0], parts[1]);
                if (user.checkLogin(username, password)) {
                    send(exchange, "Login success");
                    return;
                }
            }
        }

        send(exchange, "Login failed");
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