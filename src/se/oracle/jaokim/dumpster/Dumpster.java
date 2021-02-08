/*
 * 
 */
package se.oracle.jaokim.dumpster;

/**
 *
 * @author JSNORDST
 */
public class Dumpster {
    
    static {
        System.loadLibrary("dump");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		if(args.length > 0 ) {
			switch(args[0]) {
				case "c":
					new Dumpster().div_operator(9);
					break;
				case "clib":
					new Dumpster().div_call(9);
					break;
				case "java":
					new Dumpster().div_java(9);
					break;
			}
		}
        
    }
    
    private native void div_operator(int d);
	private native void div_call(int d);
	private void div_java(int d) {
		
	}
}
