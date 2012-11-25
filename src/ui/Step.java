package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import control.Main;

public class Step extends JComponent {

	private static final long serialVersionUID = 1L;
	private final static Dimension PREF_SIZE = new Dimension(50, 120);
	private int level;
	private int stepNumberInt;
	public SeqRow parent;
	
	public Step(int stepNumberInt) {
		this.stepNumberInt = stepNumberInt;
		setPreferredSize(PREF_SIZE);
		level = 100;
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				parent.currentStep = (Step)me.getSource();
				level = me.getY();
				repaint();
				parent.currentStep.changed();
			}
			public void mouseEntered(MouseEvent me) {
				parent.currentStep = (Step)me.getSource();
			}
		});

		// Let the user control the knob with the mouse
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				parent.currentStep.level = me.getY();
				if (parent.currentStep.level > 100)
					parent.currentStep.level = 100;
				if (parent.currentStep.level < 0)
					parent.currentStep.level = 0;
				parent.currentStep.repaint();					
				parent.currentStep.changed();
			}
		});
	}

	private void changed() {
		String evo = "F0012001";
		String sequancerParameter = "08";
		
		// parameter to be adjusted
		String stepNumberString = Integer.toHexString(stepNumberInt);
		String stepNumber = stepNumberString.length() == 1 ? "0" + stepNumberString : stepNumberString;
		
		// this is the actual value that changes when the user rotate the sw knob
		String valueString = Integer.toHexString(getHeight()-parent.currentStep.level-20);
		valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
		String MS = "0" + valueString.charAt(0);
		String LS = "0" + valueString.charAt(1);

		String endSysex = "F7";

		//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
		Main.getInstance().sendSysex(evo + sequancerParameter + stepNumber + LS + MS + endSysex);		
	}

	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setBackground(getParent().getBackground());
			g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON));
		}

		g.setColor(Color.BLUE);
		g.drawRect(0, 0, width-1, height-21);
		g.fillRect(0, level, width, height-20-level);
		g.drawString(height-20-level+"", 0, height-10+5);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = getHeight()-level-20;
		if (this.level > 100)
			this.level = 100;
		if (this.level < 0)
			this.level = 0;
		repaint();
	}

	public int getStepNumberInt() {
		return stepNumberInt;
	}

	public void setStepNumberInt(int stepNumberInt) {
		this.stepNumberInt = stepNumberInt;
	}
}