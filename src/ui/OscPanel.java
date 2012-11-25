package ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.Main;

public class OscPanel extends JPanel{

	private static final long serialVersionUID = 1L;
    private final String[]notes = new String[121];
    
	public OscPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Oscillators"));
		
		setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1,1,1,1);
		c.weightx = 1.0;
		c.weighty = 1.0;

		fillNotes();

		// oscillator 1
		c.gridx = 0; c.gridy = 0; add(new JLabel("A1"),c);
		c.gridx = 1; Main.getInstance().getParamHash().put(0,createFrequencyKnob(c, "Frequency", 0, 120));
		c.gridx = 2; Main.getInstance().getParamHash().put(1,createFineTuneKnob(c, "Fine Tune", 1, 100));
		c.gridx = 3; Main.getInstance().getParamHash().put(2,createShapeAKnob(c, "Shape/PW", 2, 102));
		c.gridx = 4; Main.getInstance().getParamHash().put(3,createLevelKnob(c, "Level", 3, 100));
		c.gridx = 5; Main.getInstance().getParamHash().put(64,createGlideKnob(c, "Glide", 64, 200));
		
		// oscillator 2
		c.gridx = 0; c.gridy = 1; add(new JLabel("A2"),c);
		c.gridx = 1; Main.getInstance().getParamHash().put(4,createFrequencyKnob(c, "Frequency", 4, 120));
		c.gridx = 2; Main.getInstance().getParamHash().put(5,createFineTuneKnob(c, "Fine Tune", 5, 100));
		c.gridx = 3; Main.getInstance().getParamHash().put(6,createShapeAKnob(c, "Shape/PW", 6, 102));
		c.gridx = 4; Main.getInstance().getParamHash().put(7,createLevelKnob(c, "Level", 7, 100));
		c.gridx = 5; Main.getInstance().getParamHash().put(68,createGlideKnob(c, "Glide", 68, 200));

		// oscillator 3
		c.gridx = 0; c.gridy = 2; add(new JLabel("D1"),c);
		c.gridx = 1; Main.getInstance().getParamHash().put(8,createFrequencyKnob(c, "Frequency", 8, 120));
		c.gridx = 2; Main.getInstance().getParamHash().put(9,createFineTuneKnob(c, "Fine Tune", 9, 100));
		c.gridx = 3; Main.getInstance().getParamHash().put(10,createShapeDKnob(c, "Shape", 10, 127));
		c.gridx = 4; Main.getInstance().getParamHash().put(11,createLevelKnob(c, "Level", 11, 100));
		c.gridx = 5; Main.getInstance().getParamHash().put(72,createGlideKnob(c, "Glide", 72, 200));

		// oscillator 4
		c.gridx = 0; c.gridy = 3; add(new JLabel("D2"),c);
		c.gridx = 1; Main.getInstance().getParamHash().put(12,createFrequencyKnob(c, "Frequency", 12, 120));
		c.gridx = 2; Main.getInstance().getParamHash().put(13,createFineTuneKnob(c, "Fine Tune", 13, 100));
		c.gridx = 3; Main.getInstance().getParamHash().put(14,createShapeDKnob(c, "Shape", 14, 127));
		c.gridx = 4; Main.getInstance().getParamHash().put(15,createLevelKnob(c, "Level", 15, 100));
		c.gridx = 5; Main.getInstance().getParamHash().put(76,createGlideKnob(c, "Glide", 76, 200));
	}
	
	private void fillNotes() {
		int octave = -2;
        int steps = 0;
        String[]arr = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < arr.length; ++j)
                notes[steps] = arr[j] + octave + " " + steps++;
            ++octave;
        }
        notes[steps] = arr[0] + octave + " " + steps;		
	}
	
    public JPanel createFrequencyKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("C-2 0");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();
				jla.setText(notes[(int)(range * t.getValue())]);
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
    }
    public JPanel createFineTuneKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("-50");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();
                
		    	jla.setText(""+(Math.round(range * t.getValue())-50));
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
	}
    public JPanel createShapeAKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("Saw");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();
                if ((int)(range * t.getValue()) == 0)
                	jla.setText("Saw");
                if ((int)(range * t.getValue()) == 1)
                	jla.setText("Tri");
                if ((int)(range * t.getValue()) == 2)
                	jla.setText("Saw+Tri");
                if ((int)(range * t.getValue()) > 2)
                	jla.setText("P"+((int)(range * t.getValue())-3));
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
	}
    public JPanel createShapeDKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("1");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();
                jla.setText(""+(1+(int)(range * t.getValue())));
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
	}
    public JPanel createLevelKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("0");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();

				double res = Math.round(range * t.getValue() * range);
				res /= range;

				jla.setText(""+(int)res);
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
	}
    public JPanel createGlideKnob(GridBagConstraints c, String name, final int parameterNumberInt, final int range) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints localConst = new GridBagConstraints();
        localConst.gridx = 0;
        localConst.gridy = 0;

        DKnob ts = new DKnob(range);
	    JLabel jl = new JLabel("0");
        jl.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        
        localConst.insets = new Insets(0,0,-7,0);
        container.add(ts,localConst);

        localConst.gridx = 0;
        localConst.gridy = 1;
        localConst.insets = new Insets(-7,0,0,0);
        container.add(jl,localConst);

        localConst.gridx = 0;
        localConst.gridy = 2;
        localConst.insets = new Insets(0,0,0,0);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Inconsolata", Font.PLAIN, 11));
        container.add(nameLabel,localConst);

	    ts.setValue((float)0.0);
	    final JLabel jla = jl;
        add(container,c);
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	DKnob t = (DKnob) e.getSource();
                if ((int)(range * t.getValue()) < 101)
    		    	jla.setText(""+((int)(range * t.getValue())));
                if ((int)(range * t.getValue()) > 100)
                	jla.setText("F"+((int)(range * t.getValue())-99));
                if ((int)(range * t.getValue()) == 199)
                	jla.setText("F00");
                if ((int)(range * t.getValue()) == 200)
                	jla.setText("OFF");
                try{
    				String evo = "F0012001";
    				String programParameter = "01";
    				
    				// parameter to be adjusted
    				String parameterNumberString = Integer.toHexString(parameterNumberInt);
    				String parameterNumber = parameterNumberString.length() == 1 ? "0" + parameterNumberString : parameterNumberString;
    				
    				// this is the actual value that changes when the user rotate the sw knob
    				int valueInt = (int)(range * t.getValue()); 
    				String valueString = Integer.toHexString(valueInt);
    				valueString = valueString.length() == 1 ? "0" + valueString : valueString;  
    				String MS = "0" + valueString.charAt(0);
    				String LS = "0" + valueString.charAt(1);

    				String endSysex = "F7";

    				//System.out.println(evo + programParameter + parameterNumber + LS + MS + endSysex);
    				Main.getInstance().sendSysex(evo + programParameter + parameterNumber + LS + MS + endSysex);

    				// this was to send the cc
    				// Main.getInstance().getMidiOutput().sendMidi(new byte[]{(byte)(status), (byte)cc, (byte)((int)(range * t.getValue()))}, de.humatic.mmj.MidiSystem.getHostTime());
                } catch (Exception ex){
                	ex.printStackTrace();
                }
		    }
		});
	    return container;
	}
}