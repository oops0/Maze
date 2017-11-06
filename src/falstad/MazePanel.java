package falstad;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.ColorConvertOp;
import java.util.Random;

/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 * 
 * @author pk
 *
 */
public class MazePanel extends Panel  {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	// bufferImage can only be initialized if the container is displayable,
	// uses a delayed initialization and relies on client class to call initBufferImage()
	// before first use
	private Image bufferImage;  
	private Graphics2D graphics; // obtained from bufferImage, 
	// graphics is stored to allow clients to draw on same graphics object repeatedly
	// has benefits if color settings should be remembered for subsequent drawing operations
	
	static int Black = 0;
	static int White = 1;
	static int Yellow = 2;
	static int Green = 3;
	static int Red = 4;
	static int Orange = 5;
	static int darkGray = 6;
	static int Gray = 7;
	static int Blue = 8;
	
	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel() {
		setFocusable(false);
		bufferImage = null; // bufferImage initialized separately and later
		graphics = null;
	}
	
	//Pseudo graphics functions
	public void setColor(int color) {
		switch(color) {
		case 0:
			graphics.setColor(Color.black);
			break;
		case 1:
			graphics.setColor(Color.white);
			break;
		case 2:
			graphics.setColor(Color.yellow);
			break;
		case 3:
			graphics.setColor(Color.green);
			break;
		case 4:
			graphics.setColor(Color.red);
			break;
		case 5:
			graphics.setColor(Color.orange);
			break;
		case 6:
			graphics.setColor(Color.darkGray);
			break;
		case 7:
			graphics.setColor(Color.gray);
			break;
		case 8:
			graphics.setColor(Color.blue);
			break;
		default:
			graphics.setColor(Color.white);
			break;
		}
	}
	
	public void setColor(int color, int caseNum) {
		switch(caseNum) {
		case 0:
            graphics.setColor(new Color(color, 20, 20));
            break;
        case 1:
        	graphics.setColor(new Color(20, color, 20));
            break;
        case 2:
        	graphics.setColor(new Color(20, 20, color));
            break;
        case 3:
        	graphics.setColor(new Color(color, color, 20));
            break;
        case 4:
        	graphics.setColor(new Color(20, color, color));
            break;
        case 5:
        	graphics.setColor(new Color(color, 20, color));
            break;
        default:
        	graphics.setColor(new Color(20, 20, 20));
            break;
        }
	}
	
	public void setFont(Font font) {
		graphics.setFont(font);
	}

	public void drawLine(int nx1, int ny1, int nx12, int ny2) {
		graphics.drawLine(nx1, ny1, nx12, ny2);
	}
	
	public void fillOval(int i, int j, int cirsiz, int cirsiz2) {
		graphics.fillOval(i,  j,  cirsiz, cirsiz2);
	}
	
	public void fillRect(int i, int j, int view_width, int k) {
		graphics.fillRect(i, j, view_width, k);
	}
	
	public void fillPolygon(int[] xps, int[] yps, int i) {
		graphics.fillPolygon(xps, yps, i);;
	}
	
	public FontMetrics getFontMetrics() {
		FontMetrics fm = graphics.getFontMetrics();
		return fm;
	}
	
	public void drawString(String str, int i, int ypos) {
		graphics.drawString(str, i, ypos);
	}
	

	
	/**
	 * Method to draw the buffer image on a graphics object that is
	 * obtained from the superclass. The method is used in the MazeController.
	 * Warning: do not override getGraphics() or drawing might fail. 
	 */
	public void update() {
		paint(getGraphics());
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 */
	@Override
	public void paint(Graphics g) {
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
		}
		else {
			g.drawImage(bufferImage,0,0,null);	
		}
	}

	public void initBufferImage() {
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
	}
	/**
	 * Obtains a graphics object that can be used for drawing.
	 * The object internally stores the graphics object and will return the
	 * same graphics object over multiple method calls. 
	 * To make the drawing visible on screen, one needs to trigger 
	 * a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on, null if impossible to obtain image
	 */
	public Graphics getBufferGraphics() {
		if (null == graphics) {
			// instantiate and store a graphics object for later use
			if (null == bufferImage)
				initBufferImage();
			if (null == bufferImage)
				return null;
			graphics = (Graphics2D) bufferImage.getGraphics();
			if (null == graphics) {
				System.out.println("Error: creation of graphics for buffered image failed, presumedly container not displayable");
			}
			// success case
			
			//System.out.println("MazePanel: Using Rendering Hint");
			// For drawing in FirstPersonDrawer, setting rendering hint
			// became necessary when lines of polygons 
			// that were not horizontal or vertical looked ragged
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		
		return graphics;
	}
}
