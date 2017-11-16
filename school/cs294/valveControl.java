package cs294pkg.fuzzyvars;

import cs294pkg.fuzzyfunctions.*;

/*
	Classname:	valveControl
	Package:	cs294pkg.fuzzyvars
	Version: 	0.12
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Mon 19 May 1997 09:54:48 
*/

public class valveControl {
	public L NB;
	public A NM,NS,ZR,PS,PM;
	public Gamma PB; 

	public valveControl(double min_position) {
		double max_position,delta;

		max_position = -1*min_position;		
		delta = max_position/3;

		NB = new L(min_position,min_position+delta);
		NM = new A(min_position,min_position+(2*delta));
		NS = new A(min_position+delta,0);
		ZR = new A(min_position+(2*delta),delta);
		PS = new A(0,2*delta);
		PM = new A(delta,max_position);
		PB = new Gamma(2*delta,max_position);
	}

}
