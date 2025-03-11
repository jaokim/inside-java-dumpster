/*
 *
 */
package inside.dumpster.client.arguments;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class CliArguments extends Arguments<CliArguments> {
    public final Arg Address = new Arguments.Arg("-url", "url", String.class, Arguments.Arg.Is.Optional, "Make requests to this URL.", "http://localhost:8081/", Arg.Askable.Yes);
    public final Arg Duration = new Arguments.Arg("-duration", "duration", Integer.class, Arguments.Arg.Is.Optional, "Set how long the client will run. In seconds.", null, Arg.Askable.Yes);
    public final Arg Limit = new Arguments.Arg("-limit", "limit", Integer.class, Arguments.Arg.Is.Optional, "Sets a limit to number of requests made.", null, Arg.Askable.Yes);
    public final Arg Filter = new Arguments.Arg("-filter", "filter", String.class, Arguments.Arg.Is.Optional, "Filter destinations.", null, Arg.Askable.Yes);
    public final Arg Interactive = new Arguments.Arg("-i", "interactive", Boolean.class, Arguments.Arg.Is.Optional, "Decide interactively when each reqauest will be sent.", "false");
    public final Arg DelayThreshold = new Arguments.Arg("-delay", "delay", Integer.class, Arguments.Arg.Is.Optional, "Delay threshold for how often requests are sent. The original delay is divided by this value.", "100");
    public final static CliArguments Instance;
    static {
      CliArguments inst = null;
      try {
        inst = new CliArguments(new String[0]);
      } catch (Exception ex) {
      } finally {
        Instance = inst;
      }
    }
    public CliArguments (String [] args) throws Exception {
        super.parseArgs(args);
    }

}
