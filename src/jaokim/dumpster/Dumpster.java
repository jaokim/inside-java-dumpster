/*
 * 
 */
package jaokim.dumpster;

import jaokim.dumpster.Divider;

/**
 *
 * @author Joakim Nordstrom
 */
public class Dumpster {
     /**
     * Main parses arguments and starts the test.
     */
    public static void main(String[] args) {
		String arg_testinput = "9";
		if(args.length > 0 ) {
			arg_testinput = args[0];
		}
		if(!arg_testinput.matches("[0-9]+")) {
			throw new IllegalArgumentException(String.format("Testinput must be a valid integer, not \"%s\"", arg_testinput));
		}
		Integer testinput = Integer.parseInt(arg_testinput);
				
		new Dumpster().doTestcase(testinput);
    }


	/**
	 * Performs the testcase
	 * @param testinput data for the test
	 */
	public void doTestcase(Integer testinput) {
		do_loops(testinput);
	}
	
	
	/**
	 * Count down from given number, calling do_div_call 
	 * for each iteration.
	 */
	private void do_loops(int loops) {
		for(int i = loops; i >= 0; i--) {
			new Divider().do_div(i);
		}
	}
}
