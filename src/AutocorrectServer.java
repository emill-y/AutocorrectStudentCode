import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AutocorrectServer {

    public static void main(String[] args) throws IOException {
        // Initializes existing Autocorrect.java logic
        String[] dict = loadDictionary("large");
        Autocorrect corrector = new Autocorrect(dict, 2);

        // Create a server on localhost:8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/suggest", (exchange) -> {
            // Handle CORS (so the browser doesn't block the request)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            
            // Get the "word" from the URL: /suggest?word=mispelledword
            String query = exchange.getRequestURI().getQuery();
            String typed = query != null ? query.split("=")[1] : "";

            // Run logic
            String[] results = corrector.runTest(typed);

            // Convert array to a simple JSON string: ["word1", "word2"]
            String response = "[" + Arrays.stream(results)
                    .map(s -> "\"" + s + "\"")
                    .collect(Collectors.joining(",")) + "]";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        System.out.println("Server started at http://localhost:8000/suggest");
        server.start();
    }

     private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
}
