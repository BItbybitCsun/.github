/*
    Module/Class Name: LoginHandler.java
    Date of Code: 03/15/26
    Programmer: Tony Ashvanian

    Brief Description: The LoginHandler.java class handles user authentication
    for the /login endpoint. It reads credentials from the request body and
    validates them against the users.txt file. If the file does not exist,
    it is created with default accounts.

    Important Functions:

    handle(HttpExchange exchange)
    Input: HTTP POST request with body formatted as "username|password"
    Output: Plain-text response ("Login success" or "Login failed")
    Purpose: Parses the request body, reads users.txt, and checks whether
    the submitted credentials match any stored user.

    send(HttpExchange exchange, String response)
    Input: HTTP exchange object, response string
    Output: Sends HTTP response to the frontend
    Purpose: Writes the response with CORS headers back to the client.

    Important Data Structures: List<String> of lines read from users.txt,
    where each line is stored as "username|password".

    Algorithm/Design Used: Linear search through users.txt to find a matching
    credential pair. Selected for simplicity given the small user base; a
    hashed lookup would be used for a production system.
*/

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