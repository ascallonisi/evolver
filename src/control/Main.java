package control;
/*

Program Parameters
Sequencer Parameters
Main Parameters

Request Program Dump -> Program Data Dump
Request Edit Buffer Dump -> Edit Buffer Data Dump
Request Waveshape Dump -> Waveshape Data Dump
Request Program Name Dump -> Program Name Data Dump
Request Main Parameters Dump -> Main Parameters Data Dump

Start/Stop Button
Reset Button
Shift button ON
Shift button OFF

Actions:
  The user change a parameter on the hw -> synch with sw
  The user change a parameter on the sw -> synch with hw
  The user change the program on the hw -> synch with sw
  
Possible to save a program on the sw and open it.
Possible to save a program on the hw from the sw.

*/



import java.util.HashMap;

import javax.swing.JComponent;

import ui.MainWindow;
import de.humatic.mmj.MidiInput;
import de.humatic.mmj.MidiOutput;
import de.humatic.mmj.MidiSystem;

public class Main {

	private static Main instance = null;
	MidiInput midiInput;
	MidiOutput midiOutput;
	private HashMap<Integer,JComponent> paramList = new HashMap<Integer,JComponent>();

	public static Main getInstance() {
		if (instance == null)
			instance = new Main();
		return instance;
	}

	private Main() {}
	
	private void initMidi() {

		// I don't really know what this do, to check 
		boolean sendActiveSensing = true;
		try {
			MidiSystem.enableActiveSensing(sendActiveSensing);
		}catch (Exception e) {
			e.printStackTrace();
		}
		MidiSystem.initMidiSystem("mmj src", "mmj dest");
	}
		
	public MidiInput getMidiInput() {
		return midiInput;
	}

	public void setMidiInput(MidiInput midiInput) {
		this.midiInput = midiInput;
	}

	public MidiOutput getMidiOutput() {
		return midiOutput;
	}

	public void setMidiOutput(MidiOutput midiOutput) {
		this.midiOutput = midiOutput;
	}

	public void sendSysex(String s) {
        int n = s.length() / 2;
        byte[] msg = new byte[n];
        for (int i = 0; i < n; i++) {
            msg[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        Main.getInstance().getMidiOutput().sendMidi(msg);
    }
	
	public HashMap<Integer,JComponent> getParamHash() {
		return paramList;
	}

	public void setParamHash(HashMap<Integer,JComponent> paramList) {
		this.paramList = paramList;
	}

	public static void main(String[] args) {
		Main.getInstance().initMidi();
		MainWindow.getInstance();
	}
}