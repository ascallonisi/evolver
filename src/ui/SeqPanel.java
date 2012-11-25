package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SeqPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public SeqPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new SeqRow(0));
		add(new SeqRow(1));
		add(new SeqRow(2));
		add(new SeqRow(3));
	}
}
