package cs294pkg.fuzzyfunctions;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	Z
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.2
	By:	 	Sriranga Veeraraghavan
	Last Mod:	11 May 1997 6:29 PM
*/

public class Z extends S {
	
	public Z (double a,double b) {
		super(a,b);
	}

	public double getGOM(double u) {
		return (1 - super.getGOM(u));
	}
}
