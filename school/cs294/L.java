package cs294pkg.fuzzyfunctions;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	L
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.1
	By:	 	Sriranga Veeraraghavan
	Last Mod:	11 May 1997 10:12 PM
*/

public class L extends Gamma {

	public L (double a,double b) {
		super(a,b);
	}

	public double getGOM(double u) {
		return (1 - super.getGOM(u));
	}
}
