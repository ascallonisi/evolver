package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import control.Main;
import de.humatic.mmj.MidiInput;
import de.humatic.mmj.MidiListener;
import de.humatic.mmj.MidiOutput;
import de.humatic.mmj.MidiSystem;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static MainWindow instance = null;
	private JComboBox ib, ob;
	private String[] hexChars = new String[]{"0","1", "2", "3", "4", "5","6","7","8","9","A", "B","C","D","E","F"};
	private SeqPanel seqPanel;
	
	public static MainWindow getInstance() {
		if (instance == null)
			instance = new MainWindow();
		return instance;
	}
	
	private MainWindow() {
		super("here goes the current program name");

	    initInputComboBox();
	    initOutputComboBox();

	    JPanel north = new JPanel();
		north.add(ib);
		north.add(ob);
		north.add(new JButton(new AbstractAction("get edit buffer") {
			
			public void actionPerformed(ActionEvent e) {
                try { Main.getInstance().sendSysex("F001200106F7"); }
                catch (Exception ex){ ex.printStackTrace(); }
			}
		})); 

		getContentPane().add("North", north);

//		OscPanel oscPanel = new OscPanel();
//		JPanel _ = new JPanel();
//		_.add(oscPanel);
//		getContentPane().add("Center", _);

		seqPanel = new SeqPanel();
		getContentPane().add("Center", seqPanel);

		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		//setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	private void initInputComboBox() {
	    ib = new JComboBox(MidiSystem.getInputs());
		ib.addItem("input");
		//ib.setSelectedIndex(ib.getItemCount()-1);
		ib.setSelectedIndex(0);
		Main.getInstance().setMidiInput(openInput(0));
		ib.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ib.getSelectedIndex() >= ib.getItemCount()-1)
					return;
				Main.getInstance().setMidiInput(openInput(ib.getSelectedIndex()));
				System.out.println(Main.getInstance().getMidiInput()+"\n"+Main.getInstance().getMidiInput().getDeviceInfo());					
			}
		});
	}

	private void initOutputComboBox() {
        ob = new JComboBox(de.humatic.mmj.MidiSystem.getOutputs());
		ob.addItem("output");
		//ob.setSelectedIndex(ob.getItemCount()-1);
		ob.setSelectedIndex(0);
		Main.getInstance().setMidiOutput(openOutput(0));
		ob.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ob.getSelectedIndex() >= ob.getItemCount()-1)
					return;
				Main.getInstance().setMidiOutput(openOutput(ob.getSelectedIndex()));
				System.out.println(Main.getInstance().getMidiOutput()+"\n"+Main.getInstance().getMidiOutput().getDeviceInfo());	
			}
		});
	}
	
	private MidiInput openInput(int index) {
		MidiInput mi = de.humatic.mmj.MidiSystem.openMidiInput(index);
		new MidiInputListener(mi);
		return mi;
	}
		
	private MidiOutput openOutput(int index) {
		return de.humatic.mmj.MidiSystem.openMidiOutput(index);
	}	

	private class MidiInputListener implements MidiListener {
		private MidiInput myInput;

		private MidiInputListener(MidiInput in) {
			myInput = in;
			in.addMidiListener(this);
		}
		
		public void midiInput(byte[] data){
            StringBuilder sb = new StringBuilder();
            // System.out.println("Input from: "+myInput.getName());
			for (int i = 0; i < data.length; i++) {

                 System.out.print(hexChars[(data[i] & 0xFF) / 16] );
                 System.out.print(hexChars[(data[i] & 0xFF) % 16]+"  ");
                 sb.append(hexChars[(data[i] & 0xFF) / 16]+hexChars[(data[i] & 0xFF) % 16]);
//				if (i > 4 && i < data.length - 1) {
//    				sb.append((char)Integer.parseInt(hexChars[(data[i] & 0xFF) / 16] + hexChars[(data[i] & 0xFF) % 16], 16));
//				}
                 if (data.length > 5 && (i+1) % 16 == 0) System.out.println("");
			}
			System.out.println("");
			//System.out.println(sb.toString().trim());
			// System.out.println("-> "+sb);
			
			// program parameters
			if (sb.toString().startsWith("F001200101")) {				
				int paramNumber = Integer.parseInt(sb.substring(10,12),16);
				String LS = sb.substring(13,14);
				String MS = sb.substring(15,16);
				int value = Integer.parseInt(MS+LS, 16);
				System.out.println("paramNumber: "+paramNumber+", value: "+value);
				
				DKnob _ = (DKnob)Main.getInstance().getParamHash().get(paramNumber).getComponent(0); 
				_.setValue((float)value/_.getRange());
			}

			// sequencer parameters
			if (sb.toString().startsWith("F001200108")) {
				int stepNumber = Integer.parseInt(sb.substring(10,12),16);
				String LS = sb.substring(13,14);
				String MS = sb.substring(15,16);
				int value = Integer.parseInt(MS+LS, 16);
				
				SeqRow row = (SeqRow)seqPanel.getComponent((int)stepNumber/16);
				Step step = (Step)row.getComponent(stepNumber%16);
				step.setLevel(value);
				//System.out.println("row: "+(int)stepNumber/16+", step: "+stepNumber%16);
			}

			// main parameters
			if (sb.toString().startsWith("F001200109")) {				
				int mainParamNumber = Integer.parseInt(sb.substring(10,12),16);
				String LS = sb.substring(13,14);
				String MS = sb.substring(15,16);
				int value = Integer.parseInt(MS+LS, 16);
				System.out.println("mainParamNumber: "+mainParamNumber+", value: "+value);
			}

			// edit buffer data
			if (sb.toString().startsWith("F001200103")) {				
				int j = 0; int k = 0;
				int vals[] = new int[64];
				for (int i = 0; i < 220; i++) {
					if (i%8 != 0) {
						int value = Integer.parseInt(sb.substring(i*2+10,i*2+12), 16);
						//System.out.println("value "+j+" : "+sb.substring(i*2+10,i*2+12) + ","+value);
						if (j > 127)
							vals[k++] = value; 
						++j;
					}
				}
				for (int i = 0; i < vals.length; i++) {
					SeqRow row = (SeqRow)seqPanel.getComponent((int)i/16);
					Step step = (Step)row.getComponent(i%16);
					step.setLevel(vals[i]);
					if (i % 8 == 0) {
						System.out.println();
					}
					System.out.print(vals[i]+"\t");					
				}
			}
		}
	}		
}