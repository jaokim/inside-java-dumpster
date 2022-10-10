package inside.dumpster.micronaut;

import inside.dumpster.outside.Bug;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.runtime.Micronaut;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
      Bug.registerMXBean();
       Map<String, Object> defaults = CollectionUtils.mapOf(
            "micronaut.server.multipart.max-file-size", "100MB",
            "micronaut.server.max-request-size", "100MB"
        );
      Micronaut.build(args).properties(defaults).run(Application.class, args);
    }
}
