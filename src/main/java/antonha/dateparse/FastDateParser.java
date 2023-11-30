package antonha.dateparse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;

public class FastDateParser {
    
    public static TemporalAccessor parse(final String inputTime) {
        int len = inputTime.length();
        char[] tmp = inputTime.toCharArray();
        if (len > 19
            && len < 31
            && tmp[len - 1] == 'Z'
            && tmp[4] == '-'
            && tmp[7] == '-'
            && (tmp[10] == 'T' || tmp[10] == 't' || tmp[10] == ' ')
            && tmp[13] == ':'
            && tmp[16] == ':'
            && allDigits(tmp, 20, len - 1)) {
            final int year = NumberConverter.read4(tmp, 0);
            final int month = NumberConverter.read2(tmp, 5);
            final int day = NumberConverter.read2(tmp, 8);
            final int hour = NumberConverter.read2(tmp, 11);
            final int min = NumberConverter.read2(tmp, 14);
            final int sec = NumberConverter.read2(tmp, 17);
            if (tmp[19] == '.') {
                final int nanos = readNanos(tmp, len - 1, 20);
                return OffsetDateTime.of(year, month, day, hour, min, sec, nanos, ZoneOffset.UTC);
            }
            return OffsetDateTime.of(year, month, day, hour, min, sec, 0, ZoneOffset.UTC);
        } else if (len > 22
            && len < 36
            && tmp[len - 3] == ':'
            && (tmp[len - 6] == '+' || tmp[len - 6] == '-')
            && tmp[4] == '-'
            && tmp[7] == '-'
            && (tmp[10] == 'T' || tmp[10] == 't' || tmp[10] == ' ')
            && tmp[13] == ':'
            && tmp[16] == ':'
            && allDigits(tmp, len - 2, len)
            && allDigits(tmp, len - 5, len - 3)) {
                final int year = NumberConverter.read4(tmp, 0);
                final int month = NumberConverter.read2(tmp, 5);
                final int day = NumberConverter.read2(tmp, 8);
                final int hour = NumberConverter.read2(tmp, 11);
                final int min = NumberConverter.read2(tmp, 14);
                final int sec = NumberConverter.read2(tmp, 17);
                final int offHour = NumberConverter.read2(tmp, len - 5);
                final int offMin = NumberConverter.read2(tmp, len - 2);
                final boolean isPositiveOffset = tmp[len - 6] == '+';
                final ZoneOffset offset = ZoneOffset.ofHoursMinutes(
                    isPositiveOffset ? offHour : -offHour,
                    isPositiveOffset ? offMin : -offMin
                );
                if (tmp[19] == '.') {
                    final int nanos = readNanos(tmp, len - 6, 20);
                    OffsetDateTime dt;
                    return OffsetDateTime.of(year, month, day, hour, min, sec, nanos, offset);
                }
                return OffsetDateTime.of(year, month, day, hour, min, sec, 0, offset);
            } else {
                return OffsetDateTime.parse(new String(tmp, 0, len));
            }
    }

    public static TemporalAccessor parseLocalDateTime(final String inputTime) {
        int len = inputTime.length();
        char[] tmp = inputTime.toCharArray();
        if (len > 18
            && len < 30
            && tmp[4] == '-'
            && tmp[7] == '-'
            && (tmp[10] == 'T' || tmp[10] == 't' || tmp[10] == ' ')
            && tmp[13] == ':'
            && tmp[16] == ':'
            && allDigits(tmp, 20, len - 1)) {
            final int year = NumberConverter.read4(tmp, 0);
            final int month = NumberConverter.read2(tmp, 5);
            final int day = NumberConverter.read2(tmp, 8);
            final int hour = NumberConverter.read2(tmp, 11);
            final int min = NumberConverter.read2(tmp, 14);
            final int sec = NumberConverter.read2(tmp, 17);
            if (len > 19) {
                if (tmp[19] != '.') {
                    throw new IllegalArgumentException("Invalid date time: " + inputTime);
                }
                final int nanos = readNanos(tmp, len - 1, 20);
                return OffsetDateTime.of(year, month, day, hour, min, sec, nanos, ZoneOffset.UTC);
            }
            return OffsetDateTime.of(year, month, day, hour, min, sec, 0, ZoneOffset.UTC);
        } else {
            return LocalDateTime.parse(inputTime);
        }
    }

    private static boolean allDigits(char[] buffer, int start, int end) {
        for (int i = start; i < end; i++) {
            if (buffer[i] < '0' || buffer[i] > '9') return false;
        }
        return true;
    }

    private static int readNanos(final char[] tmp, final int len, final int offset) {
        switch (len - offset) {
            case 1:
                return 100000000 * (tmp[offset] - 48);
            case 2:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48);
            case 3:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48);
            case 4:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48);
            case 5:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48) + 10000 * (tmp[offset + 4] - 48);
            case 6:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48) + 10000 * (tmp[offset + 4] - 48) + 1000 * (tmp[offset + 5] - 48);
            case 7:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48) + 10000 * (tmp[offset + 4] - 48) + 1000 * (tmp[offset + 5] - 48) + 100 * (tmp[offset + 6]
                        - 48);
            case 8:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48) + 10000 * (tmp[offset + 4] - 48) + 1000 * (tmp[offset + 5] - 48) + 100 * (tmp[offset + 6] - 48)
                    + 10 * (tmp[offset + 7] - 48);
            default:
                return 100000000 * (tmp[offset] - 48) + 10000000 * (tmp[offset + 1] - 48) + 1000000 * (tmp[offset + 2] - 48) + 100000
                    * (tmp[offset + 3] - 48) + 10000 * (tmp[offset + 4] - 48) + 1000 * (tmp[offset + 5] - 48) + 100 * (tmp[offset + 6] - 48)
                    + 10 * (tmp[offset + 7] - 48) + tmp[offset + 8] - 48;
        }
    }

    private static class NumberConverter {
        static int read2(final char[] buf, final int pos) {
            final int v1 = buf[pos] - 48;
            return (v1 << 3) + (v1 << 1) + buf[pos + 1] - 48;
        }

        static int read4(final char[] buf, final int pos) {
            final int v2 = buf[pos + 1] - 48;
            final int v3 = buf[pos + 2] - 48;
            return (buf[pos] - 48) * 1000 + (v2 << 6) + (v2 << 5) + (v2 << 2) + (v3 << 3) + (v3 << 1) + buf[pos + 3] - 48;
        }
    }

}
