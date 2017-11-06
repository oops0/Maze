package falstad;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import falstad.Constants.StateGUI;
import generation.Order.Builder;
import java.awt.BorderLayout; 


/**
 * Implements the screens that are displayed whenever the game is not in 
 * the playing state. The screens shown are the title screen, 
 * the generating screen with the progress bar during maze generation,
 * and the final screen when the game finishes.
 * @author pk
 *
 */
public class MazeView extends DefaultViewer implements ActionListener {

	// need to know the maze model to check the state 
	// and to provide progress information in the generating state
	private MazeController controller ;
	
	String[] drivers = {"Wizard", "WallFwr", "Pledge", "Manual"};
	String[] generators = {"DFS", "Prim", "Eller"};
	String[] skill = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13", "14", "15"};
	

	JComboBox<String> driverList = new JComboBox<String>(drivers);
	JComboBox<String> genList = new JComboBox<String>(generators);
	JComboBox<String> skillList = new JComboBox<String>(skill);
	
	public MazeView(MazeController c) {
		super() ;
		controller = c ;
	}

	@Override
	public void redraw(MazePanel gc, StateGUI state, int px, int py, int view_dx,
			int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) {
		//dbg("redraw") ;
		switch (state) {
		case STATE_TITLE:
			redrawTitle(gc);
			break;
		case STATE_GENERATING:
			redrawGenerating(gc);
			break;
		case STATE_PLAY:
			// skip this one
			break;
		case STATE_FINISH:
			redrawFinish(gc);
			break;
		}
	}
	
	private void dbg(String str) {
		System.out.println("MazeView:" + str);
	}
	
	// 
	
	/**
	 * Helper method for redraw to draw the title screen, screen is hard coded
	 * @param  gc graphics is the off screen image
	 */
	void redrawTitle(MazePanel gc) {
		// produce white background
		gc.setColor(MazePanel.White);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(MazePanel.Blue);
		centerString(gc, fm, "MAZE", 70);
		// write the reference to falstad
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		centerString(gc, fm, "by Paul Falstad", 130);
		centerString(gc, fm, "www.falstad.com", 150);
		centerString(gc, fm, "Modifications by Scott Stewart", 170);
		// write the instructions
		gc.setColor(MazePanel.Black);
		centerString(gc, fm, "Choose options, then press any #", 220);
		centerString(gc, fm, "(0 - 9) to start the game.", 240);
		gc.setColor(MazePanel.Red);
		centerString(gc, fm, "Select difficulty", 330);
		
		//ComboBox
		gc.setLayout(new BorderLayout());
		
		gc.add(driverList, BorderLayout.LINE_START);
		gc.add(genList, BorderLayout.LINE_END);
		gc.add(skillList, BorderLayout.PAGE_END);
				
		genList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				switch(genList.getSelectedIndex()) {
					case 0:
						MazeController.userBuilder = Builder.DFS;
						break;
					case 1:
						MazeController.userBuilder = Builder.Prim;
						break;
					case 2:
						MazeController.userBuilder = Builder.Eller;
						break;
					default:
						break;
				}
				genList.setFocusable(false);
				driverList.setFocusable(false);
				skillList.setFocusable(false);
			}
		});
		
		driverList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent f) {
				switch(driverList.getSelectedIndex()) {
					case 0:
						MazeController.userDriver = "Wizard";
						break;
					case 1:
						MazeController.userDriver = "WallFollower";
						break;
					case 2:
						MazeController.userDriver = "Pledge";
						break;
					case 3:
						MazeController.userDriver = "Manual";
						break;
					default:
						break;
				}
				genList.setFocusable(false);
				driverList.setFocusable(false);
				skillList.setFocusable(false);			
			}
		});
		
		skillList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent g) {
				switch(skillList.getSelectedIndex()) {
					case 0:
						MazeController.userDifficulty = 0;
						break;
					case 1:
						MazeController.userDifficulty = 1;
						break;
					case 2:
						MazeController.userDifficulty = 2;
						break;
					case 3:
						MazeController.userDifficulty = 3;
						break;
					case 4:
						MazeController.userDifficulty = 4;
						break;
					case 5:
						MazeController.userDifficulty = 5;
						break;
					case 6:
						MazeController.userDifficulty = 6;
						break;
					case 7:
						MazeController.userDifficulty = 7;
						break;
					case 8:
						MazeController.userDifficulty = 8;
						break;
					case 9:
						MazeController.userDifficulty = 9;
						break;
					case 10:
						MazeController.userDifficulty = 10;
						break;
					case 11:
						MazeController.userDifficulty = 11;
						break;
					case 12:
						MazeController.userDifficulty = 12;
						break;
					case 13:
						MazeController.userDifficulty = 13;
						break;
					case 14:
						MazeController.userDifficulty = 14;
						break;
					case 15:
						MazeController.userDifficulty = 15;
						break;
					default: 
						break;
				}
				genList.setFocusable(false);
				driverList.setFocusable(false);
				skillList.setFocusable(false);
			}
		});
		
		gc.paintAll(gc.getGraphics());
		
	}
	/**
	 * Helper method for redraw to draw final screen, screen is hard coded
	 * @param gc graphics is the off screen image
	 */
	void redrawFinish(MazePanel gc) {
		// produce blue background
		gc.setColor(MazePanel.Blue);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(MazePanel.Yellow);
		centerString(gc, fm, "You won!", 100);
		// write some extra blufb
		gc.setColor(MazePanel.Orange);
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		centerString(gc, fm, "Congratulations!", 160);
		// write battery/odometer
		centerString(gc, fm, "Steps taken: " + Integer.toString(controller.robot.getOdometerReading()), 200);
		centerString(gc, fm, "Battery remaining: " + Float.toString(controller.robot.getBatteryLevel()), 240);
		// write the instructions
		gc.setColor(MazePanel.White);
		centerString(gc, fm, "Hit any key to restart", 300);
	}

	/**
	 * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
	 * only attribute percentdone is dynamic
	 * @param gc graphics is the off screen image
	 */
	void redrawGenerating(MazePanel gc) {
		// produce yellow background
		gc.setColor(MazePanel.Yellow);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(MazePanel.Red);
		centerString(gc, fm, "Building maze", 150);
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		// show progress
		gc.setColor(MazePanel.Black);
		if (null != controller)
			centerString(gc, fm, controller.getPercentDone()+"% completed", 200);
		else
			centerString(gc, fm, "Error: no controller, no progress", 200);
		// write the instructions
		centerString(gc, fm, "Hit escape to stop", 300);
	}
	
	private void centerString(MazePanel gc, FontMetrics fm, String str, int ypos) {
		gc.drawString(str, (Constants.VIEW_WIDTH-fm.stringWidth(str))/2, ypos);
	}

	final Font largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
	final Font smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

