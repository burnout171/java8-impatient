package chapter.six;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LinkExtracter {

    private final Pattern pattern;

    LinkExtracter() {
        this.pattern = Pattern.compile("(?i)href=\"http://(.*?)\"");
    }

    List<String> getLinksFromPage(final String url) {
        String page;
        try {
            page = readPage(url);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        Matcher matcher = pattern.matcher(page);
        List<String> links = new ArrayList<>();
        while (matcher.find()) {
            links.add(matcher.group(1));
        }

        return links;
    }

    private String readPage(final String url) throws IOException {
        URL page = new URL(url);
        URLConnection connection = page.openConnection();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            builder.append(inputLine);
        }
        reader.close();

        return builder.toString();
    }
}
