package antonha.dateparse;

import java.time.temporal.TemporalAccessor;

public class Parser {
    public static void main(String[] args) {
        TemporalAccessor date = RFC3339Parser.parseDateTime("2023-01-01T23:38:34.000Z");
        System.out.println(date.toString());
    }
}
