/*
 *
 */
package inside.dumpster.client.arguments;

import java.net.URL;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class CliArguments extends Arguments<CliArguments> {
    public final Arg Address = new Arguments.Arg("-a", "address", String.class, Arguments.Arg.Is.Optional, "Port to web server.", "http://localhost:8081/", Arg.Askable.Yes);
    public final Arg Duration = new Arguments.Arg("-d", "duration", Integer.class, Arguments.Arg.Is.Optional, "Set how long the client will run.", null, Arg.Askable.Yes);
    public final Arg Limit = new Arguments.Arg("-l", "limit", Integer.class, Arguments.Arg.Is.Optional, "Sets a limit to number of requests made.", null, Arg.Askable.Yes);
    public final Arg Filter = new Arguments.Arg("-f", "filter", String.class, Arguments.Arg.Is.Optional, "Filter destinations.", null, Arg.Askable.Yes);
    public final Arg Interactive = new Arguments.Arg("-i", "interactive", Boolean.class, Arguments.Arg.Is.Optional, "Decide interactively when each reqauest will be sent.", "false");
    public final Arg DelayThreshold = new Arguments.Arg("-delay", "delay", Integer.class, Arguments.Arg.Is.Optional, "Delay threshold for how often requests are sent", "100");

    public CliArguments (String [] args) throws Exception {
        super.parseArgs(args);

    }

}
