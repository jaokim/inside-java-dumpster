/*
 * 
 */
package jaokim.dumpster;

/**
 *
 * @author Joakim Nordstrom
 */
public class Divider {
	static {
        System.loadLibrary("dump");
    }
	
	
	/**
	 * Perform a div using native call to div().
	 * @param d will be subtractred with 9, then used as denominator.
	 */
	public native void native_div_call(int d);


	/**
	 * Make the native div call.
	 * @param j will be subtracted with 9, then used as denominator.
	 */
	public void do_div(int j) {
	    native_div_call(j);
	}
	
}
