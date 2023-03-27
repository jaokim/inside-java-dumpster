/*
 *
 */
package inside.dumpster.client;

import inside.dumpster.client.arguments.CliArguments;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@State(Scope.Benchmark)
public class BenchmarkRunner {
//    @Param({ "10", "60" })
//    public String duration;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
//    @Fork(value = 1, warmups = 2)
@Timeout(time = 60, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
    public void init() throws Exception {
      Cli cli = new Cli();
      CliArguments args = new CliArguments(new String[]
        {CliArguments.Instance.Limit.param, "100"}
              );
      cli.start(args);
    }
}
