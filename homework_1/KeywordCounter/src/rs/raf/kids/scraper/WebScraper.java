package rs.raf.kids.scraper;

import rs.raf.kids.core.Res;
import rs.raf.kids.log.Log;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebScraper implements Scraper {

    @Override
    public String getContent(String path) {
        String page = null;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(path))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            page = response.body();
        }catch(Exception e) {
            Log.e(Res.ERROR_SCAN_WEB);
        }

        return page != null ? page : "";
    }
}
