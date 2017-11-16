package cs294pkg.rules;

import java.lang.Math;
import java.util.Vector;
import cs294pkg.*;
import cs294pkg.fuzzyfunctions.*;
import cs294pkg.rules.*;
import cs294pkg.fuzzyvars.*;

/*
	Classname:	ruleBase
	Package:	cs294pkg.rules
	Version: 	0.45
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Sat 17 May 1997 13:50:43 
*/

public class ruleBase {
	public A FuzzyTemp,FuzzyFlow;
	public outTemp ot;
	public outFlow of;
	public valveControl hot_valve_control,cold_valve_control;
	public double hot_action,cold_action;
	public Vector rule_vect;
	public double granularity;
	public int action_selection_method;

	public ruleBase(outTemp a,outFlow b,valveControl hvc, valveControl cvc,int asm) {
		ot = a;
		of = b;
		hot_valve_control=hvc;
		cold_valve_control=cvc;
		action_selection_method=asm;
		rule_vect = new Vector(8);
		granularity=0.01;
		hot_action = (double)-6;
		cold_action = (double)-6;
	}
	
	public void setActions() {
		double f_delta_temp=0.1;
		double f_delta_flow=0.5;

		FuzzyTemp = new A(ot.curTemp - f_delta_temp, ot.curTemp + f_delta_temp);
		FuzzyFlow = new A(of.curFlow - f_delta_flow, of.curFlow + f_delta_flow);

		System.out.println("Begin Rule Evaluation");
		rules();

		if (!rule_vect.isEmpty()) {
			System.out.println("Begin Action Selection");	
			pickAction();
			System.out.println("Begin MOM Defuzzification");
			MOM();
			rule_vect.removeAllElements();
		} else {
			System.out.println("No Rules Fired, No Action Taken");
		}
	}
	
	public double maxmin(genericFuzzyFunction gff1,genericFuzzyFunction gff2,
		boolean verbose) {

		double gff1_gom,gff2_gom;
		double max_gom=(double)0;
	
		for (double i = (double)gff1.minlimit; i <= (double)gff1.maxlimit; 
			i+=granularity/10) {

			gff1_gom=gff1.getGOM(i);
			gff2_gom=gff2.getGOM(i);
			double delta_gom = Math.abs(gff1_gom - gff2_gom);

			if (verbose) {
				System.out.print(i + "\tgff1_gom: " + gff1_gom 
					+ "\tgff2_gom: " + gff2_gom);
			}

			if (delta_gom <= granularity && max_gom < gff1_gom 
				&& gff2_gom > (double)0) { 

				max_gom = (gff1_gom + gff2_gom)/2;
				if (verbose) System.out.print(" *");
			}

			if (verbose) System.out.print("\n");
		}
		
		if (verbose) System.out.println("max_gom: " + max_gom);
		return max_gom;
	}

	public void rules() {
		double curTemp_gom_cold,curTemp_gom_warm,curTemp_gom_hot;
		double curFlow_gom_low,curFlow_gom_normal,curFlow_gom_strong;
		
		curTemp_gom_cold = maxmin(FuzzyTemp,ot.Cold,(boolean)false);
		curTemp_gom_warm = maxmin(FuzzyTemp,ot.Warm,(boolean)false);
		curTemp_gom_hot  = maxmin(FuzzyTemp,ot.Hot,(boolean)false);

		System.out.println("gom_cold: " + curTemp_gom_cold + "\tgom_warm: " 
			+ curTemp_gom_warm + "\tgom_hot: " + curTemp_gom_hot);

		curFlow_gom_low = maxmin(FuzzyFlow,of.Low,(boolean)false);
		curFlow_gom_normal = maxmin(FuzzyFlow,of.Normal,(boolean)false);
		curFlow_gom_strong = maxmin(FuzzyFlow,of.Strong,(boolean)false);

		System.out.println("gom_low: " + curFlow_gom_low + "\tgom_normal: " 
			+ curFlow_gom_normal + "\tgom_strong: " + curFlow_gom_strong);
 
		/* [1] rule cold,low --> hotvalve PB, coldvalve PS (was PB,ZR) */
		if (curTemp_gom_cold > 0.0 && curFlow_gom_low > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_cold,curFlow_gom_low),
					new String("PB"), new String("PS"),1));
			System.out.println("Fired: [1] rule cold,low (PB,PS)");
		}

		/* [2] rule cold,normal --> hotvalve PM, coldvalve ZR */
		if (curTemp_gom_cold > 0.0 && curFlow_gom_normal > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_cold,curFlow_gom_normal),
					new String("PM"), new String("ZR"),2));
			System.out.println("Fired: [2] rule cold,normal (PM,ZR)");
		}

		/* [3] rule cold,strong --> hotvalve PM, coldvalve NB (was ZR,NB) */
		if (curTemp_gom_cold > 0.0 && curFlow_gom_strong > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_cold,curFlow_gom_strong),
					new String("PM"), new String("NB"),3));
			System.out.println("Fired: [3] rule cold,strong (PM,NB)");
		}

		/* [4] rule warm,low --> hotvalve PS, coldvalve ZR (was PS,PS) */
		if (curTemp_gom_warm > 0.0 && curFlow_gom_low > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_warm,curFlow_gom_low),
					new String("PS"), new String("ZR"),4));
			System.out.println("Fired: [4] rule warm,low (PS,ZR)");
		}

		/* [5] rule warm,strong --> hotvalve NM, coldvalve NS (was NS,NS) */
		if (curTemp_gom_warm > 0.0 && curFlow_gom_strong > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_warm,curFlow_gom_strong),
					new String("NM"), new String("NS"),5));
			System.out.println("Fired: [5] rule warm,strong (NM,NS)");
		}

		/* [6] rule hot,low --> hotvalve NM, coldvalve PS (was ZR,PB) */
		if (curTemp_gom_hot > 0.0 && curFlow_gom_low > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_hot,curFlow_gom_low),
					new String("NM"), new String("PS"),6));
			System.out.println("Fired: [6] rule hot,low (NM,PS)");
		}

		/* [7] rule hot,normal --> hotvalve NB, coldvalve PS (was NM,ZR) */
		if (curTemp_gom_hot > 0.0 && curFlow_gom_normal > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_hot,curFlow_gom_normal),
					new String("NB"), new String("PS"),7));
			System.out.println("Fired: [7] rule hot,normal (NB,PS)");
		}

		/* [8] rule hot,strong --> hotvalve NB, coldvalve NM (was NB,ZR) */
		if (curTemp_gom_hot > 0.0 && curFlow_gom_strong > 0.0) {
			rule_vect.addElement(
				new ruleConsequent(Math.min(curTemp_gom_hot,curFlow_gom_strong),
					new String("NB"), new String("NM"),8));
			System.out.println("Fired: [8] rule hot,strong (NB,NM)");
		}

	}
	
	public void pickAction() {
		
		if (rule_vect.size()-1 >0) {
			switch (action_selection_method) {
				case 1:
					pickByGOM(); break;
				case 2:
					pickByRandom(); break;
				case 3:
					pickFirst(); break;
				case 4:
					pickLast(); break;
				default:
					pickByGOM();
			}
		}
	}
	
	public void pickByRandom() {
		double random_rule = Math.random();
		int which_rule = (int)(2*(rule_vect.size()-1)*random_rule);
		adjust_rule_vect((ruleConsequent)rule_vect.elementAt(which_rule));
	}

	public void pickFirst() {
		adjust_rule_vect((ruleConsequent)rule_vect.firstElement());
	}
	
	public void pickLast() {
		adjust_rule_vect((ruleConsequent)rule_vect.lastElement());
	}

	public void pickByGOM() {
		Vector temp_vect = new Vector(8);
		ruleConsequent rc_rv,rc_tv;
		int last = 0;

		temp_vect.addElement((ruleConsequent)rule_vect.firstElement());
		
		for (int i = 0; i <= rule_vect.size()-1; i++) {
			rc_rv = (ruleConsequent)rule_vect.elementAt(i);
			rc_tv = (ruleConsequent)temp_vect.elementAt(last);
			System.out.println("Evaluating Rule " + rc_rv.rulenum + " vs Rule "
				+ rc_tv.rulenum);
			if (rc_rv.rule_gom - rc_tv.rule_gom > granularity) {
				temp_vect.setElementAt(rc_rv,last);
				System.out.println("Picked Rule: " + rc_rv.rulenum);
			} else if (Math.abs(rc_rv.rule_gom - rc_tv.rule_gom) <= granularity 
				&& rc_rv.rulenum != rc_tv.rulenum) {

				last++;
				temp_vect.setSize(last+1);
				temp_vect.setElementAt(rc_rv,last);
				System.out.println("Added Rule: " + rc_rv.rulenum);
			}
		}

		replace(rule_vect,temp_vect);

		System.out.print("Remaining Rule(s): ");
		for (int i =0; i <=rule_vect.size()-1; i++) {
			rc_rv=(ruleConsequent)rule_vect.elementAt(i);
			System.out.print(rc_rv.rulenum + " ");
		}
		System.out.print("\n");

		temp_vect.removeAllElements();
	}

	public void replace(Vector v1,Vector v2) {
		v1.removeAllElements();
		for (int i = 0;i <= v2.size()-1;i++) {
			v1.addElement(v2.elementAt(i));
		}
	}

	public void adjust_rule_vect(ruleConsequent rca) {
		rule_vect.removeAllElements();
		rule_vect.addElement(rca);
	}

	public void MOM() {
		double J;
		double xtotal_hot=0;
		double xtotal_cold=0;

		J = rule_vect.size();

		for (int i = 0; i <= J-1; i++) {
			xtotal_hot += getXVal
				((ruleConsequent)rule_vect.elementAt(i),new String("hot"));
			xtotal_cold += getXVal
				((ruleConsequent)rule_vect.elementAt(i),new String("cold"));
		}

		hot_action=xtotal_hot/J;
		cold_action=xtotal_cold/J;

		System.out.println("Recommended Actions: " + hot_action + " (hot) " + 
			cold_action + " (cold)");
	}

	public double getXVal(ruleConsequent rc,String which_valve) {
		double xval=0;

		if (which_valve.equals("hot")) {
			/* Defuzzify Hot Actions */
			if (rc.hotvalve.equals("NB")) {
				/* NB Action */
				xval=bigXVal(hot_valve_control.NB,rc.rule_gom);
				System.out.println("Defuzzified NB (hot)  -->\t" + xval);
			} else if (rc.hotvalve.equals("PB")) {
				/* PB Action */
				xval=bigXVal(hot_valve_control.PB,rc.rule_gom);
				System.out.println("Defuzzified PB (hot)  -->\t" + xval);
			} else if (rc.hotvalve.equals("PM")) {
				/* PM Action */
				xval=rc.rule_gom*(hot_valve_control.PM.maxlimit 
					- hot_valve_control.PM.minlimit);
				xval+=hot_valve_control.PM.minlimit;
				System.out.println("Defuzzified PM (hot)  -->\t" + xval);
			} else if (rc.hotvalve.equals("NM")) {
				/* NM Action */
				xval=rc.rule_gom*(hot_valve_control.NM.maxlimit 
					- hot_valve_control.NM.minlimit);
				xval+=hot_valve_control.NM.minlimit; 
				System.out.println("Defuzzified NM (hot)  -->\t" + xval);
			} else if (rc.hotvalve.equals("PS")) {
				/* PS Action */
				xval=rc.rule_gom*(hot_valve_control.PS.maxlimit 
					- hot_valve_control.PS.minlimit);
				xval+=hot_valve_control.PS.minlimit;
				System.out.println("Defuzzified PS (hot)  -->\t" + xval);
			} else if (rc.hotvalve.equals("NS")) {
				/* NS Action */
				xval=rc.rule_gom*(hot_valve_control.NS.maxlimit
					- hot_valve_control.NS.minlimit);
				xval+=hot_valve_control.NS.minlimit;
				System.out.println("Defuzzified NS (hot)  -->\t" + xval);
			} else {
				/* ZR Action */
				xval=rc.rule_gom*(hot_valve_control.ZR.maxlimit 
					- hot_valve_control.ZR.minlimit);
				xval+=hot_valve_control.ZR.minlimit;
				System.out.println("Defuzzified ZR (hot)  -->\t" + xval);
			}
		} else {
			/* Defuzzify Cold Actions */
			if (rc.coldvalve.equals("NB")) {
				/* NB Action */
				xval=bigXVal(cold_valve_control.NB,rc.rule_gom);
				System.out.println("Defuzzified NB (cold) -->\t" + xval);
			} else if (rc.coldvalve.equals("PB")) {
				/* PB Action */
				xval=bigXVal(cold_valve_control.PB,rc.rule_gom);
				System.out.println("Defuzzified PB (cold) -->\t" + xval);
			} else if (rc.coldvalve.equals("PM")) {
				/* PM Action */
				xval=rc.rule_gom*(cold_valve_control.PM.maxlimit 
					- cold_valve_control.PM.minlimit);
				xval+=cold_valve_control.PM.minlimit;
				System.out.println("Defuzzified PM (cold) -->\t" + xval);
			} else if (rc.coldvalve.equals("NM")) {
				/* NM Action */
				xval=rc.rule_gom*(cold_valve_control.NM.maxlimit 
					- cold_valve_control.NM.minlimit);
				xval+=cold_valve_control.NM.minlimit;
				System.out.println("Defuzzified NM (cold) -->\t" + xval);
			} else if (rc.coldvalve.equals("PS")) {
				/* PS Action */
				xval=rc.rule_gom*(cold_valve_control.PS.maxlimit 
					- cold_valve_control.PS.minlimit);
				xval+=cold_valve_control.PS.minlimit;
				System.out.println("Defuzzified PS (cold) -->\t" + xval);
			} else if (rc.coldvalve.equals("NS")) {
				/* NS Action */
				xval=rc.rule_gom*(cold_valve_control.NS.maxlimit 
					- cold_valve_control.NS.minlimit);
				xval+=cold_valve_control.NS.minlimit;
				System.out.println("Defuzzified NS (cold) -->\t" + xval);
			} else {
				/* ZR Action */
				xval=rc.rule_gom*(cold_valve_control.ZR.maxlimit 
					- cold_valve_control.ZR.minlimit);
				xval+=cold_valve_control.ZR.minlimit;
				System.out.println("Defuzzified ZR (cold) -->\t" + xval);
			}
		}
		
		return xval;	
	}

	public double bigXVal(genericFuzzyFunction gff,double target_gom) {
		double xvalof_gom=0;

		for (double i = gff.minlimit; i <= gff.maxlimit; i+=granularity/10) {
			if (Math.abs(gff.getGOM(i) - target_gom) <= granularity) {
				xvalof_gom=i;
				break;
			}
		}

		return xvalof_gom;	
	}
}




