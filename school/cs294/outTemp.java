package cs294pkg.fuzzyvars;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	outTemp
	Package:	cs294pkg.fuzzyvars
	Version: 	0.12
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Mon 19 May 1997 09:53:48 
*/

public class outTemp {
	public Z Cold;
	public Pi Warm;
	public S Hot;
	public double curTemp;

	public outTemp(double temp_min,double temp_max) {
		double midpoint,delta;
		
		midpoint = (temp_min + temp_max)/2;
		delta = (temp_max - temp_min)/6;

		Cold = new Z(temp_min,midpoint);
		Warm = new Pi(midpoint-delta,midpoint+delta);
		Hot = new S(midpoint,temp_max);

		curTemp = temp_min;
	}

	public outTemp(double cold_min,double cold_max,
		double warm_min,double warm_max,
		double hot_min,double hot_max) {

		Cold = new Z(cold_min,cold_max);
		Warm = new Pi(warm_min,warm_max);
		Hot = new S(hot_min,hot_max);

		curTemp = cold_min;
	}
}
