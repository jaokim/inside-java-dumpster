/*
 *
 */
package inside.dumpster.backend.energy;

import java.math.BigDecimal;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class WindPowerCalculator {

    public double Borwein(int noOfIter)
    {
        // Calculating initial value of 1/pi
        double oneByPi = 6.0 - 4 * Math.sqrt(2);

        // Calculating the constant value y
        // used in Borwein Algorithm
        double y = Math.sqrt(2) - 1.0;

        double oneByPiAfterIter;
        double yAfterIter;

        // It calculates an estimation
        // of 1/pi that increases in accurary
        // the more iterations you use
        for (int i = 0; i < noOfIter; i++) {

            // Based on Algorithm formulas are used
            yAfterIter= (1 - Math.pow((1 - y * y * y * y), (0.25))) /
                          (1+ Math.pow((1 - y * y * y * y), (0.25)));

            oneByPiAfterIter = oneByPi * Math.pow((1 + yAfterIter), 4) -
            Math.pow(2, 2 * i + 3) * yAfterIter * (1 + yAfterIter +
                                           yAfterIter * yAfterIter);

            y = yAfterIter;

            oneByPi = oneByPiAfterIter;
        }
        return oneByPi;
    }

  /*
   * https://www.geeksforgeeks.org/implementing-borwein-algorithm-in-java/?ref=rp
   *
   * @param noOfIter
   * @return
   */
      // Main driver method
    public BigDecimal Borwein_l(long noOfIter) {
        // Calculating initial value of 1/pi
        //double oneByPi = 6.0 - 4 * Math.sqrt(2);
        BigDecimal oneByPi = new BigDecimal(6.0).subtract(new BigDecimal(4) .multiply(new BigDecimal(Math.sqrt(2))));

        // Calculating the constant value y
        // used in Borwein Algorithm
        BigDecimal y = new BigDecimal(Math.sqrt(2)).subtract(new BigDecimal(1.0));

        BigDecimal oneByPiAfterIter;
        BigDecimal yAfterIter;

        // It calculates an estimation
        // of 1/pi that increases in accurary
        // the more iterations you use
        for (long i = 0; i < noOfIter; i++) {

            // Based on Algorithm formulas are used
            yAfterIter= (BigDecimal.ONE.subtract(((BigDecimal.ONE.subtract(y.multiply(y).multiply(y).multiply(y))).pow(2).
                    divideToIntegralValue(BigDecimal.ONE.add(BigDecimal.ONE.subtract(y.multiply(y).multiply(y).multiply(y)))))));

            oneByPiAfterIter = oneByPi.multiply( (BigDecimal.ONE.add(yAfterIter).pow(4).subtract(BigDecimal.ONE.add(BigDecimal.ONE).pow(
                    (BigDecimal.valueOf(i)
                            .add(BigDecimal.valueOf(3))
                            .multiply(new BigDecimal(i)
                            .add(BigDecimal.valueOf(3)))
                            .add(BigDecimal.valueOf(3))).intValue())))
            );
//            Math.pow(2, 2 * i + 3) * yAfterIter * (1 + yAfterIter +
//                                           yAfterIter * yAfterIter);

            y = yAfterIter;

            oneByPi = oneByPiAfterIter;
        }
        return oneByPi;
    }
    public static void main(String[] args) {
      long start = System.currentTimeMillis();

     double res = new WindPowerCalculator().Borwein(119502 * 100);

      System.out.println("Tome:"+ res+ ", " + (System.currentTimeMillis() - start) + " ms");
  }

}
