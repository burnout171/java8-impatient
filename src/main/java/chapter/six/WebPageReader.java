package chapter.six;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import javax.annotation.Nullable;

public final class WebPageReader {

    public static String readPage(final String url, @Nullable final String authentication) throws IOException {
        URL page = new URL(url);
        URLConnection connection = page.openConnection();

        if (authentication != null) {
            String base64 = Base64.getEncoder().encodeToString(authentication.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + base64);
        }

        connection.connect();

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        }

        return builder.toString();
    }
}
