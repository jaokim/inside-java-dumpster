
package inside.dumpster.admission;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.util.ThreadHelper;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom <joakim.nordstrom@oracle.com>
 */
public class AdmissionService extends BusinessLogicService<AdmissionPayload, AdmissionResult> {

    public AdmissionService(Class<AdmissionPayload> payloadClass, Class<AdmissionResult> resultClass) {
        super(payloadClass, resultClass);
    }

    public AdmissionService() {
        super(AdmissionPayload.class, AdmissionResult.class);
    }

    @Override
    public AdmissionResult invoke(AdmissionPayload payload) throws BusinessLogicException {
        try {
            AdmissionResult result = standInQueue(payload).get();
            return result;
        } catch (ExecutionException | InterruptedException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    protected Future<AdmissionResult> standInQueue(AdmissionPayload payload) throws BusinessLogicException {
        final CompletableFuture<AdmissionResult> future = new CompletableFuture<>();
        final AdmissionResult result = new AdmissionResult();
        int i = 0;
        while(i++ < payload.getDstBytes()) {
            result.setResult(new Random().toString());
        }
        return future;
    }
}
