package cs294pkg.fuzzyvars;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	outFlow
	Package:	cs294pkg.fuzzyvars
	Version: 	0.12
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Mon 19 May 1997 09:53:14 
*/

public class outFlow {
	public Z Low;
	public Pi Normal;
	public S Strong;
	public double curFlow;

	public outFlow(double flow_min,double flow_max) {
		double midpoint,delta;
		
		midpoint = (flow_min + flow_max)/2;
		delta = (flow_max - flow_min)/6;

		Low = new Z(flow_min,midpoint);
		Normal = new Pi(midpoint-delta,midpoint+delta);
		Strong = new S(midpoint,flow_max);

		curFlow = flow_min;
	}

	public outFlow(double low_min,double low_max,
		double normal_min,double normal_max,
		double strong_min,double strong_max) {

		Low = new Z(low_min,low_max);
		Normal = new Pi(normal_min,normal_max);
		Strong = new S(strong_min,strong_max);

		curFlow = low_min;
	}
}
