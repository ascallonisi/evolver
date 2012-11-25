package ui;

/*  
 * DKnob.java
 * (c) 2000 by Joakim Eriksson
 *  
 * DKnob is a component similar to JSlider but with 
 * round "user interface", a knob. 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class DKnob extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static float START = 225;
	private final static float LENGTH = 270;
	private final static float PI = (float) 3.1415;
	private final static float START_ANG = (START/360)*PI*2;
	private final static float LENGTH_ANG = (LENGTH/360)*PI*2;
	private final static float MULTIP = 180 / PI; 

	private float DRAG_SPEED;
	private float CLICK_SPEED;
	private int size;
	private int middle;

	private final static Dimension MIN_SIZE = new Dimension(80, 80);
	private final static Dimension PREF_SIZE = new Dimension(80, 80);

	private ChangeEvent changeEvent = null;
	private EventListenerList listenerList = new EventListenerList();

	private Arc2D hitArc = new Arc2D.Float(Arc2D.PIE);

	private float ang = (float) START_ANG;
	private float val = 0.0F;
	private int dragpos = -1;
	private float startVal;
	private int range;
	
	public DKnob(int range) {
		this.range = range;
		DRAG_SPEED = 0.005F;
		//CLICK_SPEED = 0.00833333333F;
		CLICK_SPEED = (float)(1.0 / range);

		setPreferredSize(PREF_SIZE);
		setBorder(BorderFactory.createLineBorder(Color.black));

		hitArc.setAngleStart(235); // Degrees ??? Radiant???
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				dragpos = me.getX() + me.getY();
				startVal = val;

				// Fix last angle
				int xpos = middle - me.getX();
				int ypos = middle - me.getY();
				Math.atan2(xpos, ypos);

				requestFocus();
			}

			public void mouseClicked(MouseEvent me) {
				hitArc.setAngleExtent(-(LENGTH + 20));
				if  (hitArc.contains(me.getX(), me.getY())) {	   
					hitArc.setAngleExtent(MULTIP * (ang-START_ANG)-10);
					if  (hitArc.contains(me.getX(), me.getY())) {
						decValue();
					} else incValue();	
				}
			}
		});

		// Let the user control the knob with the mouse
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				float f = DRAG_SPEED * (float)(dragpos - (me.getX() + me.getY()));
				setValue(startVal + f);
			}

			public void mouseMoved(MouseEvent me) {
			}
		});

		// Let the user control the knob with the keyboard
		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {} 
			public void keyPressed(KeyEvent e) { 
				int k = e.getKeyCode();
				if (k == KeyEvent.VK_RIGHT)
					incValue();
				else if (k == KeyEvent.VK_LEFT)
					decValue();
			}		
		});
	}

	private void incValue() {
		setValue(val + CLICK_SPEED);
	}

	private void decValue() {
		setValue(val - CLICK_SPEED);
	}

	public float getValue() {
		return val;
	}

	public void setValue(float val) {
		if (val < 0) val = 0;
		if (val > 1) val = 1;
		this.val = val;
		ang = START_ANG - (float) LENGTH_ANG * val;
		repaint();
		fireChangeEvent();
	}

	public void addChangeListener(ChangeListener cl) {
		listenerList.add(ChangeListener.class, cl);
	}

	public void removeChangeListener(ChangeListener cl) {
		listenerList.remove(ChangeListener.class, cl);		
	}

	public Dimension getMinimumSize() {
		return MIN_SIZE;
	}

	protected void fireChangeEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
			}
		}
	}


	// Paint the DKnob
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		size = Math.min(width, height) - 22;
		middle = 10 + size/2;

		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setBackground(getParent().getBackground());
			g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON));

			// For the size of the "mouse click" area
			hitArc.setFrame(4, 4, size+12, size+12);
		}

		g.setColor(Color.BLUE);
		g.drawArc(10, 10, size, size, 315, 270);
		g.fillOval(14, 14, size-8, size-8);

		g.setColor(Color.RED);
		int x = 10 + size/2 + (int)(size/2 * Math.cos(ang));
		int y = 10 + size/2 - (int)(size/2 * Math.sin(ang));
		// g.drawLine(10 + size/2, 10 + size/2, x, y);
		// g.setColor(Color.RED);
		g.fillOval(x-4, y-4, 10, 10);
	}

	public int getRange() {
		return range;
	}
}
