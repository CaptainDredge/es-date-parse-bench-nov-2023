package antonha.dateparse;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;

/**
 * Enumeration of the fields that makes up the date/date-time
 */
public enum Field
{
    YEAR(4),
    MONTH(7),
    DAY(10),
    HOUR(13),
    MINUTE(16),
    SECOND(19),
    NANO(20);

    private final int requiredLength;

    Field(int requiredLength)
    {
        this.requiredLength = requiredLength;
    }

    public static Field valueOf(Class<? extends Temporal> type)
    {
        if (Year.class.equals(type))
        {
            return YEAR;
        }
        else if (YearMonth.class.equals(type))
        {
            return MONTH;
        }
        else if (LocalDate.class.equals(type))
        {
            return DAY;
        }
        else if (OffsetDateTime.class.equals(type))
        {
            return SECOND;
        }

        throw new IllegalArgumentException("Type " + type.getSimpleName() + " is not supported");
    }

    public int getRequiredLength()
    {
        return requiredLength;
    }
}