import cs294pkg.*;
import cs294pkg.fuzzyfunctions.*;
import cs294pkg.rules.*;
import cs294pkg.fuzzyvars.*;
import java.lang.Math;
import java.awt.*;

/*
	Classname:	controlEngine
	Package:	none
	Version: 	0.31
	By:	 	Sriranga Veeraraghavan
	Last Mod:	Sun 18 May 1997 10:17:55 
*/

public class cs294Engine extends Frame {
	public outTemp output_temp;
	public outFlow output_flow;
	public valveControl cold_valve,hot_valve;
	public ruleBase rb;
	public double min_temp,max_temp,goal_temp,goal_temp_delta;
	public double min_flow,max_flow,goal_flow,goal_flow_delta;
	public double min_domain;
	public double hot_valve_pos,cold_valve_pos;
	public double fh,fc,ph,pc,th,tc;
	public double iteration,max_iterations,iteration_factor;
	public int act_sel;
	public Choice ActionChoice,FunctionChoice,DomainChoice,FlowChoice;
	public int calc_flow_complex;
	public TextField goal_temp_tf,goal_flow_tf;
	public TextField goal_temp_delta_tf,goal_flow_delta_tf;
	public TextField hot_tf,cold_tf;
	public TextField it_factor_tf,it_tf;
	public Label ending_conditions;
	public Panel main_panel;

	public cs294Engine() {
		initGlobals();
		initGUI();
	}

	void initGlobals() {	
		/* The control range is from 5-55 C, 
			default goal is 36 C, default delta is 3 C */
		min_temp  = (double)5;
		max_temp  = (double)55;
		goal_temp = (double)36;
		goal_temp_delta = (double)3;

		/* flow range 0-30 l/min, 
			default goal is 12 l/min, default delta is 2 l/min  */
		min_flow = (double)0;
		max_flow = (double)30;
		goal_flow = (double)12;
		goal_flow_delta = (double)2;

		/* the constant hot and cold pressures, ph and pc */
		ph = max_flow/2;
		pc = max_flow/2;

		/* the constant inlet hot and cold temperatures */
		tc = min_temp;
		th = max_temp;

		/* the valve control is the "standard domain" (real interval [-6,6]) */
		min_domain=(double)-6;
		cold_valve = new valveControl(min_domain);
		hot_valve  = new valveControl(min_domain);

		/* inital valve positions (off) */
		hot_valve_pos=(double)0;
		cold_valve_pos=(double)0;

		/* the default number of iterations after control is stopped */
		max_iterations=(double)10;

		/* calculate flow using complex formula */
		calc_flow_complex=3;
	}

	void initGUI(){
		this.setTitle("CS294 Project");
		this.setResizable(true);
		this.setBackground(Color.white);

		main_panel = new rectPanel();
		initmain_panel();
		this.add("Center",main_panel);

//		this.pack();
		this.resize(500,400);
		this.show();
	}

	void initmain_panel() {
		main_panel.setLayout(new GridLayout(0,1,5,5));

		Panel HeadingPanel = new Panel();
		initHeadingPanel(HeadingPanel);
		main_panel.add(HeadingPanel);

		Panel FuzzyControlsPanel = new Panel();
		initFuzzyControlsPanel(FuzzyControlsPanel);
		main_panel.add(FuzzyControlsPanel);

		Panel RunControlPanel = new Panel();
		initRunControlPanel(RunControlPanel);
		main_panel.add(RunControlPanel);

		Panel GoalPanel = new Panel();
		initGoalPanel(GoalPanel);
		main_panel.add(GoalPanel);

		Panel ValvePanel = new Panel();
		initValvePanel(ValvePanel);
		main_panel.add(ValvePanel);

		Panel ButtonPanel = new Panel();
		initButtonPanel(ButtonPanel);
		main_panel.add(ButtonPanel);
	}

	void initButtonPanel(Panel p) {
		p.add(new Button("Run"));
		p.add(new Button("Clear"));
		p.add(new Button("Quit"));
	}

	void initValvePanel(Panel p) {
		p.setLayout(new GridLayout(1,2,5,5));

		p.add(new Label("Hot Valve Pos [0,1]",Label.CENTER));
		hot_tf = new TextField(5);
		p.add(hot_tf);

		p.add(new Label("Cold Valve Pos [0,1]",Label.CENTER));
		cold_tf = new TextField(5);
		p.add(cold_tf);		
	}

	void initGoalPanel(Panel p) {
		p.setLayout(new GridLayout(0,1,5,5));
		
		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(1,2,5,5));
		Panel p2 = new Panel();
		p2.setLayout(new GridLayout(1,2,5,5));

		String str = "Goal Temp (" + Double.toString(min_temp) + "-" + 
			Double.toString(max_temp) + " C):";
		p1.add(new Label(str,Label.CENTER));
		goal_temp_tf = new TextField(3);
		p1.add(goal_temp_tf);

		str = "Goal Flow (" + Double.toString(min_flow) + "-" 
			+ Double.toString(max_flow) + " l/min):";
		p1.add(new Label(str,Label.CENTER));
		goal_flow_tf = new TextField(3);
		p1.add(goal_flow_tf);

		p2.add(new Label("Temp Delta (C):",Label.CENTER));
		goal_temp_delta_tf = new TextField(3);
		p2.add(goal_temp_delta_tf);

		p2.add(new Label("Flow Delta (l/min):",Label.CENTER));
		goal_flow_delta_tf = new TextField(3);
		p2.add(goal_flow_delta_tf);

		p.add(p1);
		p.add(p2);
	}
	
	void initRunControlPanel(Panel p) {
		p.setLayout(new GridLayout(1,2,5,5));

		p.add(new Label("End at Iteration",Label.CENTER));
		it_tf = new TextField(5);
		p.add(it_tf);

		p.add(new Label("Scale By",Label.CENTER));
		it_factor_tf = new TextField(5);
		p.add(it_factor_tf);
	}

	void initFuzzyControlsPanel(Panel p) {
		p.setLayout(new GridLayout(0,4,5,5));

		p.add(new Label("Conflict Resolution:",Label.CENTER));
		initActionChoice();
		p.add(ActionChoice);

		p.add(new Label("Fuzzy Functions:",Label.CENTER));
		initFunctionChoice();
		p.add(FunctionChoice);

		p.add(new Label("Control Domain:",Label.CENTER));
		initDomainChoice();
		p.add(DomainChoice);

		p.add(new Label("Flow Calc:",Label.CENTER));
		initFlowChoice();
		p.add(FlowChoice);
	}
	
	void initFlowChoice() {
		FlowChoice = new Choice();
		FlowChoice.addItem("Simple Delta");
		FlowChoice.addItem("Simple Position");
		FlowChoice.addItem("Complex Delta");
		FlowChoice.select("Complex Delta");
	}

	void initFunctionChoice() {
		FunctionChoice = new Choice();
		FunctionChoice.addItem("Symetric");
		FunctionChoice.addItem("Asymetric");
		FunctionChoice.select("Symetric");
	}
		
	void initDomainChoice() {
		DomainChoice = new Choice();
		DomainChoice.addItem("Standard");
		DomainChoice.addItem("Normal");
		DomainChoice.select("Standard");
	}

	void initActionChoice() {
		ActionChoice = new Choice();
		ActionChoice.addItem("gom");
		ActionChoice.addItem("random");
		ActionChoice.addItem("first");
		ActionChoice.addItem("last");
		ActionChoice.select("gom");
	}
	
	void initHeadingPanel(Panel p) {
		p.setLayout(new GridLayout(0,1,5,5));
		p.add(new Label("A FuzzyLogic Water Temperature and Flow Controller v0.3"
			,Label.CENTER));
		p.add(new Label("By Sriranga Veeraraghavan",Label.CENTER));
		p.add(new Label("Written on a Macintosh Centris 650 using JDK 1.0.2",Label.CENTER));
		ending_conditions = new Label("System Idle",Label.CENTER);
		p.add(ending_conditions);
	}
	
	public boolean handleEvent (Event event) {
		if (event.id == Event.WINDOW_DESTROY) {
			dispose();
			System.exit(0);
			return true;
		}
		return super.handleEvent(event);
	}	

	public boolean action(Event event, Object arg) {
		String term_string;

		if (event.target instanceof Button) {
			if (arg.equals("Run")) {
				ending_conditions.setText("Initalizing System...");
				getVals();
				ending_conditions.setText("Controling System...");
				runFuzzyControl();

				/* adjust number iteration for final output */
				if (iteration < max_iterations) {
					iteration--;
					term_string = new String(" (Done)");
				} else {
					term_string = new String (" (Max Iterations)");
				}
				ending_conditions.setText("Iteration: " + iteration
					+ "\tCurrent Temp (C): " + rb.ot.curTemp + 
					"\tCurrent Flow (l/min): " + rb.of.curFlow +
					term_string);
			}
			if (arg.equals("Quit")) {
				dispose();
				System.exit(0);
			}
			if (arg.equals("Clear")) {
				String tmp = new String("");
				goal_temp_tf.setText(tmp);
				goal_flow_tf.setText(tmp);
				goal_temp_delta_tf.setText(tmp);
				goal_flow_delta_tf.setText(tmp);
				hot_tf.setText(tmp);
				cold_tf.setText(tmp);
				it_factor_tf.setText(tmp);
				it_tf.setText(tmp);
				FlowChoice.select("Complex Delta");
				FunctionChoice.select("Symetric");
				DomainChoice.select("Standard");
				ActionChoice.select("gom");
				ending_conditions.setText("Settings Cleared");
			}
		}
		return true;
	}

	void getVals() {
		Double gtv = new Double(0);
		Double gfv = new Double(0);
		Double dtv = new Double(0);
		Double dfv = new Double(0);
		Double hpv = new Double(0);
		Double cpv = new Double(0);
		Double itv = new Double(0);
		Double ifv = new Double(0);

		String ac = ActionChoice.getSelectedItem();
		String fc = FunctionChoice.getSelectedItem();
		String dc = DomainChoice.getSelectedItem();
		String cc = FlowChoice.getSelectedItem();
		String gt = goal_temp_tf.getText();
		String gf = goal_flow_tf.getText();
		String dt = goal_temp_delta_tf.getText();
		String df = goal_flow_delta_tf.getText();
		String hp = hot_tf.getText();
		String cp = cold_tf.getText();
		String it = it_tf.getText();
		String ft = it_factor_tf.getText();

		if (fc.equals("Symetric")) {
			output_temp = new outTemp(min_temp,max_temp);
			output_flow = new outFlow(min_flow,max_flow);
		} else {
			output_temp = new outTemp(22.0,37.0,2.0,36.0,27.0,50.0);
			output_flow = new outFlow(6.0,15.0,1.0,12.0,16.0,40.0);
		}

		ending_conditions.setText("Initalized Fuzzy Sets for Temp and Flow");

		if (dc.equals("Standard")) {
			min_domain=(double)-6;
		} else {
			min_domain=(double)-1;
		}
		
		cold_valve = new valveControl(min_domain);
		hot_valve  = new valveControl(min_domain);

		ending_conditions.setText("Initalized Fuzzy Sets for Control");

		if (cc.equals("Simple Delta")) {
			calc_flow_complex = 1;
		} else if (cc.equals("Simple Position")) {
			calc_flow_complex = 2;
		} else {
			calc_flow_complex = 3;
		}

		if (ac.equals("gom")) {
			act_sel = 1;
		} else if (ac.equals("random")) {
			act_sel = 2;
		} else if (ac.equals("first")) {
			act_sel = 3;
		} else {
			act_sel = 4;
		}

		rb = new ruleBase(output_temp,output_flow,cold_valve,hot_valve,act_sel);
		System.out.println("Default Temp: " + rb.ot.curTemp + "\tDefault Flow: " 
			+ rb.of.curFlow);
		ending_conditions.setText("Initalized Rule Base");

		try {
			if (gt.equals("")) gt = "36";
			gtv = Double.valueOf(gt);
			goal_temp = gtv.doubleValue();
			if (goal_temp < min_temp) goal_temp = min_temp;
			if (goal_temp > max_temp) goal_temp = max_temp;
		} catch (NumberFormatException e) {
			goal_temp = 36.0;
		}

		try {
			if (gf.equals("")) gf = "12";
			gfv = Double.valueOf(gf);
			goal_flow = gfv.doubleValue();
			if (goal_flow < min_flow) goal_flow = min_flow;
			if (goal_flow > max_flow) goal_flow = max_flow;
		} catch (NumberFormatException e) {
			goal_flow = 12.0;
		}

		try {
			if (dt.equals("")) dt = "3";
			dtv = Double.valueOf(dt);
			goal_temp_delta = dtv.doubleValue();
			if (goal_temp_delta > max_temp - min_temp ||
				goal_temp_delta < (double)0) goal_temp_delta=(double)3;
		} catch (NumberFormatException e) {
			goal_temp_delta=3.0;
		}

		try {
			if (df.equals("")) df = "2";
			dfv = Double.valueOf(df);
			goal_flow_delta = dfv.doubleValue();
			if (goal_flow_delta > max_flow - min_flow ||
				goal_flow_delta < (double)0) goal_flow_delta=(double)2;
		} catch (NumberFormatException e) {
			goal_flow_delta=2.0;
		}

		try {
			if (hp.equals("")) hp = "0";
			hpv = Double.valueOf(hp);
			hot_valve_pos = hpv.doubleValue();
			if (hot_valve_pos < 0) hot_valve_pos = 0;
			if (hot_valve_pos > 1) hot_valve_pos = 1;
			rb.hot_action = unfixAction(hot_valve_pos);
		} catch (NumberFormatException e) {
			hot_valve_pos = 0;
			rb.hot_action = min_domain;
		}

		try {
			if (cp.equals("")) cp = "0";
			cpv = Double.valueOf(cp);
			cold_valve_pos = cpv.doubleValue();
			if (cold_valve_pos < 0) cold_valve_pos = 0;
			if (cold_valve_pos > 1) cold_valve_pos = 1;
			rb.cold_action = unfixAction(cold_valve_pos);
		} catch (NumberFormatException e) {
			hot_valve_pos = 0;
			rb.cold_action = min_domain;
		}

		System.out.println("Inital Hot Pos: " + fixAction(rb.hot_action) 
			+ "\tInital Cold Pos: " + fixAction(rb.cold_action));

		doAction();
	
		System.out.println("Inital Temp: " + rb.ot.curTemp + "\tIntial Flow: " 
			+ rb.of.curFlow + "\n");

		try {
			if (it.equals("")) it = "10";
			itv = Double.valueOf(it);
			max_iterations = itv.doubleValue();
			if (max_iterations <= (double)0) max_iterations = (double)1;
		} catch (NumberFormatException e) {
			max_iterations = (double)10;
		}

		try {
			if (ft.equals("")) ft = "1";
			ifv = Double.valueOf(ft);
			iteration_factor = ifv.doubleValue();
			if (iteration_factor >= (double)1 ||
				iteration_factor <= (double)0) iteration_factor = (double)1;
		} catch (NumberFormatException e) {
			iteration_factor = (double)1;
		}

		iteration=(double)1;
	}
	
	void calcTemp() {
		if (rb.of.curFlow == 0) {
			rb.ot.curTemp = 0;
		} else {
			rb.ot.curTemp=((tc*fc) + (th*fh))/rb.of.curFlow;
		}
	}

	void setValvePos() {
		double rec_action;

		rec_action = fixAction(rb.hot_action);
		System.out.print("Recommended Pos: " + rec_action + " (hot)\t");
		if (hot_valve_pos == 1 && rb.hot_action > 0) {
			System.out.println("ERROR: hot valve at max position, cannot open " +
				"further. Closing hot valve.");
			hot_valve_pos = 0.75;
			iteration_factor = 1;
		} else if (hot_valve_pos == 0 && rb.hot_action < 0) {
			System.out.println("ERROR: hot valve at min position, cannot close " +
				"further. Opening hot valve.");
			hot_valve_pos = 0.25;
			iteration_factor = 1;
		} else {
			if (rb.hot_action < 0) {
				hot_valve_pos -= rec_action;
			} else {
				hot_valve_pos += rec_action;
			}
			if (hot_valve_pos > 1) hot_valve_pos = 1;
			if (hot_valve_pos < 0) hot_valve_pos = 0;
		} 

		rec_action = fixAction(rb.cold_action);
		System.out.println(rec_action + " (cold)");
		if (cold_valve_pos == 1 && rb.cold_action > 0) {
			System.out.println("ERROR: cold valve at max position, cannot open " +
				"further. Closing cold valve.");
			cold_valve_pos = 0.75;
			iteration_factor = 1;
		} else if (cold_valve_pos == 0 && rb.cold_action < 0) {
			System.out.println("ERROR: cold valve at min position, cannot close " +
				"further. Opening cold valve.");
			cold_valve_pos = 0.25;
			iteration_factor = 1;
		} else {
			if (rb.cold_action < 0) {
				cold_valve_pos -= rec_action;
			} else {
				cold_valve_pos += rec_action;
			}
			if (cold_valve_pos > 1) cold_valve_pos = 1;
			if (cold_valve_pos < 0) cold_valve_pos = 0;
		}
	}

	void calcFlow() {
	
		/* 
		   The flow was originally calculated (old way) using absolute
		   position, instead of relative position.  This would lead to
		   problems, where adjustments were to corse and the temp and
		   flow would alternate between too high and too low settings
		   forever.
		   The flow is now calculated (new way) using relative position,
		   and scaling factors, which reduces the effect of susequent
		   changes
		*/

		if (calc_flow_complex != 2) {
			System.out.println("Current Valve Pos: " + hot_valve_pos + " (hot)\t" +
				cold_valve_pos + " (cold)");
			setValvePos();
			System.out.println("Actual Actions: " + unfixAction(hot_valve_pos) +
				" (hot)\t" + unfixAction(cold_valve_pos) + " (cold)");
			System.out.println("New Valve Pos: " + hot_valve_pos + " (hot)\t" +
				cold_valve_pos + " (cold)");
		}

		/* calculate the hot and cold flows */
		switch (calc_flow_complex) {
			case 1:
				System.out.println("Calculating Flow (Simple Delta)");
				fc=iteration_factor*pc*cold_valve_pos;
				fh=iteration_factor*ph*hot_valve_pos;
				break;
			case 2: 
				System.out.println("Calculating Flow (Simple Pos)");
				fc=iteration_factor*pc*fixAction(rb.cold_action);
				fh=iteration_factor*ph*fixAction(rb.hot_action);
				break;
			case 3: 
				System.out.println("Calculating Flow (Complex Delta)");
				fc=complexFlowCalc(cold_valve_pos,rb.cold_action,pc);
				fh=complexFlowCalc(hot_valve_pos,rb.hot_action,ph);
				break;
		}

		rb.of.curFlow=fc+fh;
	}

	double complexFlowCalc(double vp,double a,double p) {
			double tmp = Math.max(0.1,vp);
			tmp *=iteration_factor;
			tmp *=fixAction(a);
			tmp +=vp;
			double tmp2 = Math.min(1.0,tmp);
			tmp = Math.max(0.0,tmp2);
			return p*tmp;

	}

	double fixAction(double action) {
		/* actions occur on the interval [-,+], but flow rate 
		   calculations are easier on the interval [0,1] */

		return (double)(action + Math.abs(min_domain))/(2*Math.abs(min_domain));
	}
	
	double unfixAction(double pos) {
		/* position is on the interval [0,1] but the actions
		   are given on the interval [-,+] */
		
		return ((2*pos) - 1)*Math.abs(min_domain);
	}	

	void doAction() {
		calcFlow();
		calcTemp();
	}
	
	void runFuzzyControl() {
		double given_if = iteration_factor;

		System.out.println("Goal Temp (C): " + goal_temp + 
			"\tGoal Flow (l/min): " + goal_flow);
		
		System.out.println("Goal Temp Delta (C): " + goal_temp_delta + 
			"\tGoal Flow Delta (l/min): " + goal_flow_delta);
		
		while (Math.abs(goal_temp - rb.ot.curTemp) > goal_temp_delta ||
			Math.abs(goal_flow - rb.of.curFlow) > goal_flow_delta) {
			
			System.out.println("Iteration: " + iteration);
			rb.setActions();
			doAction();
			System.out.println("Current Temp (C): " + rb.ot.curTemp 
				+ "\tCurrent Flow (l/min): " + rb.of.curFlow + "\n");
			ending_conditions.setText("Iteration: " + iteration 
				+ "\tCurrent Temp (C): " + rb.ot.curTemp + 
				"\tCurrent Flow (l/min): " + rb.of.curFlow);
			if (iteration >= max_iterations) break;
			iteration++;
			iteration_factor*=given_if;
		}
	}

	public static void main() {
		cs294Engine c294e = new cs294Engine();
	}
}




