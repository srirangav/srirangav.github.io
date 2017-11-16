package cs294pkg.fuzzyfunctions;

import java.lang.Math;
import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	S
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.3
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Thu 15 May 1997 14:52:51 
*/

public class S extends genericFuzzyFunction {
	public double minlimit,maxlimit,midpoint;
	
	public S (double a,double b) {
		super(a,b);
		minlimit=super.minlimit;
		maxlimit=super.maxlimit;
		midpoint=(super.minlimit + super.maxlimit)/2;
	}

	public double getGOM(double u) {
		double grade_of_membership = 0;

		if (u <= super.minlimit) {
			grade_of_membership = 0;						
		} else if (super.minlimit < u  && u <= midpoint) {	
			grade_of_membership = 
				2*Math.pow(((u - super.minlimit)/
					(super.maxlimit - super.minlimit)),(double)2);
		} else if (midpoint < u && u <= super.maxlimit) {
			grade_of_membership = 
				1 - 2*Math.pow(((u - super.maxlimit)/
					(super.maxlimit - super.minlimit)),(double)2);
		} else if (u > super.maxlimit) {
			grade_of_membership = 1;
		}

		return grade_of_membership;
	}
}