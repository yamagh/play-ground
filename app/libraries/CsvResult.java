package libraries;

import play.mvc.Result;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static play.mvc.Results.ok;

public class CsvResult {
    public static Result ok(String csvContent, String filename) {
        try {
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String contentDisposition = String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encodedFilename, encodedFilename);
            return play.mvc.Results.ok(csvContent)
                .as("text/csv")
                .withHeader("Content-Disposition", contentDisposition);
        } catch (java.io.UnsupportedEncodingException e) {
            // This should not happen with UTF-8, but handle it just in case.
            throw new RuntimeException(e);
        }
    }
}
