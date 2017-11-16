package cs294pkg;

import java.awt.*;

/*
	Class:	 	rectPanel
	Package:	cs294pkg;
	Version: 	0.7
	By:	 	Sriranga Veeraraghavan
	Last Mod:	16 Mar 97 8:51 PM
*/

public class rectPanel extends Panel {
	
	public Insets insets() {
		return new Insets(5,5,5,5);
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(5,5,this.size().width-5,this.size().height-5);
	}
}
