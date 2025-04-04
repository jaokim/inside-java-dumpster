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
    return this.getDuration() != null ? Long.parseLong(this.getDuration()) : 0;
  }

  public int getIngoingWattage() {
    return this.getSrcBytes();
  }

  public void setIngoingWattage(int watts) {
    this.setSrcBytes(watts);
  }

  public int getRequestedWattage() {
    return Integer.parseInt(this.getSrcPackets());
  }

  public void setRequestedWattage(int watts) {
    this.setSrcPackets(String.valueOf(watts));
  }
}
