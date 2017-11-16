package cs294pkg.fuzzyfunctions;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	Pi
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.4
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Thu 15 May 1997 14:51:26 
*/

public class Pi extends genericFuzzyFunction {
	public double maxlimit,minlimit,midpoint;
	public S PiS;
	public Z PiZ;

	public Pi (double a,double b) {
		super(a,b);
		minlimit=super.minlimit;
		midpoint = (super.maxlimit + super.minlimit)/2;
		maxlimit=super.maxlimit;
		PiS = new S(super.minlimit,midpoint);
		PiZ = new Z(midpoint,super.maxlimit);
	}

	public double getGOM(double u) {
		double grade_of_membership=0;

		if (u <= midpoint) {
			grade_of_membership = PiS.getGOM(u);
		} else if (midpoint < u) {
			grade_of_membership = PiZ.getGOM(u);
		}

		return grade_of_membership;
	}
}
