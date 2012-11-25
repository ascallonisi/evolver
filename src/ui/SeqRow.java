package ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class SeqRow extends JPanel {
	private static final long serialVersionUID = 1L;
	public Step currentStep;
	private int row;

	public SeqRow(int row) {
		this.row = row;
		setLayout(new GridLayout(1, 16, 10, 10));
		
		for (int i = 0; i < 16; i++) {
			Step step = new Step(i+(row*16));
			step.parent = this;
			add(step);
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
