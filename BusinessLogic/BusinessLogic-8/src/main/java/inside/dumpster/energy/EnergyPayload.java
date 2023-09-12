/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.client.Payload;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyPayload extends Payload {
  public Long getIterations() {
    return Long.parseLong(this.getDuration());
  }

  public int getIngoingWattage() {
    return this.getSrcBytes();
  }

  public void setIngoingWattage(int watts) {
    this.setSrcBytes(watts);
  }

  public int getRequestedWattage() {
    return this.getDstBytes();
  }

  public void setRequestedWattage(int watts) {
    this.setDstBytes(watts);
  }
}
