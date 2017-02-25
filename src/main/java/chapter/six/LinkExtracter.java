package chapter.six;

import static chapter.six.WebPageReader.readPage;

import java.io.IOException;
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
            page = readPage(url, null);
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
}
