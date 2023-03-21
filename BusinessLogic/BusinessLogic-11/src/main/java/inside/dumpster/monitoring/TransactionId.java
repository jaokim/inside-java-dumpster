/*
 * 
 */
package inside.dumpster.monitoring;

import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Relational;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@MetadataDefinition
@Label("Transaction Id")
@Relational()
public @interface TransactionId {
}
