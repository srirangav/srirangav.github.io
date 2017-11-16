package cs294pkg.rules;

/*
	Classname:	ruleConsequent
	Package:	cs294pkg.rules
	Version: 	0.2
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Fri 16 May 1997 07:47:18 
*/

public class ruleConsequent {
	public String hotvalve,coldvalve;
	public double rule_gom;
	public int rulenum;

	public ruleConsequent(double rg,String hv,String cv,int rn) {
		rule_gom=rg;
		hotvalve=hv;
		coldvalve=cv;
		rulenum=rn;
	}
}