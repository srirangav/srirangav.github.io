package cs294pkg.fuzzyfunctions;

/*
	Classname:	genericFuzzyFunction
	Package:	cs294pkg.fuzzyfunctions
	Version: 	0.1
	By:	 	Sriranga Veeraraghavan
	Last Mod:	13 May 1997 11:25 AM
*/

public abstract class genericFuzzyFunction {
	public double maxlimit,minlimit;
	
	public genericFuzzyFunction(double a,double b) {
		minlimit = a;
		maxlimit = b;
	}

	public abstract double getGOM(double u);

}