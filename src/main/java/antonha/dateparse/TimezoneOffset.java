package antonha.dateparse;

import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Container class for timezone offset, denoted by hours and minutes
 */
public class TimezoneOffset
{
    public static final TimezoneOffset UTC = new TimezoneOffset(0, 0);
    private final int hours;
    private final int minutes;

    private TimezoneOffset(final int hours, final int minutes)
    {
        if (hours > 0 && minutes < 0)
        {
            throw new DateTimeException("Zone offset minutes must be positive because hours is positive");
        }
        else if (hours < 0 && minutes > 0)
        {
            throw new DateTimeException("Zone offset minutes must be negative because hours is negative");
        }

        this.hours = hours;
        this.minutes = minutes;
    }

    public static TimezoneOffset ofHoursMinutes(int hours, int minutes)
    {
        return new TimezoneOffset(hours, minutes);
    }

    public static TimezoneOffset of(ZoneOffset offset)
    {
        final int seconds = offset.getTotalSeconds();
        final int hours = seconds / 3600;
        final int remainder = seconds % 3600;
        final int minutes = remainder / 60;
        return TimezoneOffset.ofHoursMinutes(hours, minutes);
    }

    public int getHours()
    {
        return hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getTotalSeconds()
    {
        return hours * 60 * 60 + minutes * 60;
    }

    public ZoneOffset toZoneOffset()
    {
        if (this.equals(UTC))
        {
            return ZoneOffset.UTC;
        }
        return ZoneOffset.ofHoursMinutes(hours, minutes);
    }

    /**
     * @hidden
     */
    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        TimezoneOffset that = (TimezoneOffset) o;
        return hours == that.hours && minutes == that.minutes;
    }

    /**
     * @hidden
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString()
    {
        return "TimezoneOffset{" + "hours=" + hours + ", minutes=" + minutes + '}';
    }
}
