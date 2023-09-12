/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.client.Result;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyResult extends Result {
  private final int requestedWattage;
  private int receivedWattage;
  private BigDecimal result = new BigDecimal(BigInteger.ZERO);


  public EnergyResult(int requestedWattage) {
    this.requestedWattage = requestedWattage;
  }
  public void addWattage(int receivedWattage) {
    this.receivedWattage += receivedWattage;
  }

  public void setReceivedWattage(int receivedWattage) {
    this.receivedWattage = receivedWattage;
  }

  public int getReceivedWattage() {
    return receivedWattage;
  }

  public void addResult(BigDecimal result) {
    this.result = this.result.add(result);
    setResult(this.result.toPlainString());
  }

  @Override
  public String toString() {
    return String.format("Requested: %d, recv: %d, res: %s", this.requestedWattage, this.receivedWattage, this.result.toPlainString());
  }

  boolean isReached() {
    return receivedWattage >= requestedWattage ;
  }

}
