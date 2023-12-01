/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package antonha.dateparse;

import com.ethlo.time.ITU;
import org.openjdk.jmh.annotations.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@BenchmarkMode(Mode.AverageTime)

@OutputTimeUnit(TimeUnit.NANOSECONDS)

@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Benchmark)
public class Benchmark {


    @Param({
            "2023-01-01T23:38:34.000Z",
            "1970-01-01T00:16:12.675Z",
            "5050-01-01T12:02:01.123Z",
    })
    public String dateString;


    @org.openjdk.jmh.annotations.Benchmark()
    public TemporalAccessor benchESParse() {
        return ElasticsearchParsers.doParse(dateString);
    }

    @org.openjdk.jmh.annotations.Benchmark()
    public TemporalAccessor benchESStrictParse() {
        return ElasticsearchParsers.doParseStrict(dateString);
    }

//

    /*
    Not complete, here as a reference for roughly how fast Instant.parse() is.
     */
    @org.openjdk.jmh.annotations.Benchmark
    public TemporalAccessor benchInstantParse() {
        return Instant.parse(dateString);
    }
//
    @org.openjdk.jmh.annotations.Benchmark
    public TemporalAccessor benchCharParser() {
        return CharDateParser.parse(dateString);
    }

    //
    @org.openjdk.jmh.annotations.Benchmark
    public TemporalAccessor benchITUParser() {
        return ITU.parseDateTime(dateString);
    }

    //
    @org.openjdk.jmh.annotations.Benchmark
    public TemporalAccessor benchRFCParser() {
        return RFC3339Parser.parseDateTime(dateString);
    }
}
