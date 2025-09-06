package services;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvService {

    public <T> String generate(List<String> headers, List<T> data, Function<T, List<String>> rowMapper) {
        final var headerRow = String.join(",", headers.stream().map(this::escapeCsv).collect(Collectors.toList()));

        final var dataRows = data.stream()
                .map(rowMapper)
                .map(rowData -> rowData.stream().map(this::escapeCsv).collect(Collectors.joining(",")));

        return Stream.concat(Stream.of(headerRow), dataRows)
                .collect(Collectors.joining("\n"));
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // ダブルクォートが含まれている場合は、ダブルクォートでエスケープします。
        String escaped = value.replace("\"", "\"\"");
        // カンマ、ダブルクォート、または改行が含まれている場合は、フィールド全体をダブルクォートで囲みます。
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
