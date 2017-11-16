package cs294pkg.fuzzyfunctions;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	Gamma
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.3
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Thu 15 May 1997 14:52:13 
*/

public class Gamma extends genericFuzzyFunction {
	public double maxlimit,minlimit;

	public Gamma (double a,double b) {
		super(a,b);
		maxlimit=super.maxlimit;
		minlimit=super.minlimit;
	}

	public double getGOM(double u) {
		double grade_of_membership=0;

		if (u < super.minlimit) {
			grade_of_membership=0;
		} else if (super.minlimit <= u && u <= super.maxlimit) {
			grade_of_membership=(u - super.minlimit)/(super.maxlimit - super.minlimit);
		} else if ( u > super.maxlimit) {
			grade_of_membership=1;
		}

		return grade_of_membership;
	}
}
