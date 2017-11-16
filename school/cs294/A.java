package cs294pkg.fuzzyfunctions;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	A
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.3
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Thu 15 May 1997 14:51:59 
*/

public class A extends genericFuzzyFunction {
	public double minlimit,maxlimit,middle;

	public A (double a,double b,double c) {
		super(a,b);
		minlimit = super.minlimit;
		maxlimit = super.maxlimit;
		middle=c;
	}

	public A (double d,double e) {
		this(d,e,(d+e)/2);
	}

	public double getGOM(double u) {
		double grade_of_membership=0;

		if (u < super.minlimit) {
			grade_of_membership = 0;
		} else if (super.minlimit <= u && u <= middle) {
			grade_of_membership = (u - super.minlimit)/(middle - super.minlimit);
		} else if (middle <= u && u <= super.maxlimit) {
			grade_of_membership = (super.maxlimit - u)/(super.maxlimit - middle);
		} else if ( u > super.maxlimit) {
			grade_of_membership = 0;
		}

		return grade_of_membership;
	}
}
