package falstad;

import java.util.Arrays;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.Distance;


/**
 * Wizard driver checks neighboring squares for the shortest distance to exit
 * Moves to the appropriate square and repeats until the game is won
 * 
 * Collaborators: MazeController, BasicRobot
 * @author scstew
 */

public class Wizard implements RobotDriver {

	BasicRobot robot;
	Distance distances;
	int width, height;
	
	public Wizard() { }
	
	@Override
	public boolean drive2Exit() throws Exception {
		//Turn on all map features for viewing
		robot.getController().showMaze = true;
		robot.getController().showSolution = true;
		robot.getController().mapMode = true;
		
		
		//Main movement
		while(!robot.isAtExit()) {		
			if ( (robot.getBatteryLevel() > 0) && !robot.isAtExit() ) {
					int curX = robot.getCurrentPosition()[0];
					int curY = robot.getCurrentPosition()[1];
					int[] curDirection = robot.getCurrentDirection().getDirection();
					
					//Call accessory function to fill an array with distances to exit
					int distanceArray[] = getDistanceArray(curDirection, curX, curY);
										
					//Call accessory function to determine best possible move using distanceArray
					int move = getMove(distanceArray);
										
					//Move to the best square
					switch(move){
						case 0:
							//Forward best move
							robot.move(1, false);
							Thread.sleep(50);
							break;
						case 1:
							//Right best move
							robot.rotate(Turn.RIGHT);
							robot.move(1, false);
							Thread.sleep(50);
							break;
						case 2:
							//Left best move
							robot.rotate(Turn.LEFT);
							robot.move(1, false);
							Thread.sleep(50);
							break;
						case 3:				
							//Back best move
							robot.rotate(Turn.AROUND);
							robot.move(1, false);
							Thread.sleep(50);
							break;
					}
				}
		}
		
		//At exit, turn appropriately and drive into it
		if (robot.canSeeExit(Direction.FORWARD)){
			robot.move(1, false);
			Thread.sleep(50);
		}
		else if (robot.canSeeExit(Direction.LEFT)){
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
			Thread.sleep(50);
		}
		else if (robot.canSeeExit(Direction.RIGHT)) {
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
			Thread.sleep(50);
		}
		return true;
	}
	
	/**
	 * Accessory function used to store distance values of neighbors
	 * Will return an array that contains the distanceToExit values of all neighbors, used to determine the next move
	 * @param curDirection
	 * @param curX
	 * @param curY
	 * @return array
	 */
	protected int[] getDistanceArray(int[] curDirection, int curX, int curY) {
		int[] East = {1, 0};
		int[] West = {-1, 0};
		int[] North = {0 , -1};
		int[] South = {0, 1};
		
		//Distances initialized to (max-1) to make it arbitrarily large, while avoiding confusion with functions that look for the true max_value
		int backDist = Integer.MAX_VALUE-1;
		int leftDist = Integer.MAX_VALUE-1;
		int rightDist = Integer.MAX_VALUE-1;
		int forwardDist = Integer.MAX_VALUE-1;
		Distance distance = robot.getController().getMazeConfiguration().getMazedists();
		
		//Best move needs to be selected relative to the driver's current direction, so we change x/y values appropriately 
		if (Arrays.equals(curDirection, East)) {
			//Forward
			if (curX + East[0] < width){
				forwardDist = distance.getDistance(curX + East[0], curY + East[1]);
			}
			//Right
			if (curY + North[1] >= 0){
				rightDist = distance.getDistance(curX + North[0], curY + North[1]);
			}
			//Left
			if (curY + South[1] < height){
				leftDist = distance.getDistance(curX + South[0], curY + South[1]);
			}
			//Back
			if (curX + West[0] >= 0){
				backDist = distance.getDistance(curX + West[0], curY + West[1]);
			}
		}
		else if (Arrays.equals(curDirection, West)) {
			//Forward
			if (curX + West[0] >= 0){
				forwardDist = distance.getDistance(curX + West[0], curY + West[1]);
			}
			//Right
			if (curY + South[1] < height){
				rightDist = distance.getDistance(curX + South[0], curY + South[1]);
			}
			//Left
			if (curY + North[1] >= 0){
				leftDist = distance.getDistance(curX + North[0], curY + North[1]);
			}
			//Back
			if (curX + East[0] < width){
				backDist = distance.getDistance(curX + East[0], curY + East[1]);
			}
		}
		else if (Arrays.equals(curDirection, North)) {
			//Forward
			if (curY + North[1] >= 0){
				forwardDist = distance.getDistance(curX + North[0], curY + North[1]);
			}
			//Right
			if (curX + West[0] >= 0){
				rightDist = distance.getDistance(curX + West[0], curY + West[1]);
			}
			//Left
			if (curX + East[0] < width){
				leftDist = distance.getDistance(curX + East[0], curY + East[1]);
			}
			//Back
			if (curY + South[1] < height){
				backDist = distance.getDistance(curX + South[0], curY + South[1]);
			}
		}
		else if (Arrays.equals(curDirection, South)) {
			//Forward
			if (curY + South[1] < height){
				forwardDist = distance.getDistance(curX + South[0], curY + South[1]);
			}
			//Right
			if (curX + East[0] < width){
				rightDist = distance.getDistance(curX + East[0], curY + East[1]);
			}
			//Left
			if (curX + West[0] >= 0){
				leftDist = distance.getDistance(curX + West[0], curY + West[1]);
			}
			//Back
			if (curY + North[1] >= 0){
				backDist = distance.getDistance(curX + North[0], curY + North[1]);
			}
		}
		int[] array = {forwardDist, rightDist, leftDist, backDist};
		return array;
	}
	
	/**
	 * Accessory function used to compute next move
	 * minDist to exit set to infinity, loop through all neighboring cells to find true minimum
	 * minIndex used to report which cell should be moved to
	 * @param distanceArray
	 * @return minIndex
	 */
	protected int getMove(int[] distanceArray){
		int minDist = Integer.MAX_VALUE-1;
		int minIndex = 0;
		
		//Iterate through the distance array and set minIndex to the neighboring REACHABLE cell with the minimum distance to exit
		for (int i = 0; i < 4; i++){
			if (distanceArray[i] < minDist){
				switch(i){
					case 0:
						//Can't reach cells with walls up, so don't change the index in this case
						if ((robot.distanceToObstacle(Direction.FORWARD) != 0)){
							//New metric of minimum distance is set to FORWARD cell's distance
							minDist = distanceArray[i];
							//New minimum index is set to FORWARD cell's index
							minIndex = i;
						}
						break;
					case 1:
						if (robot.distanceToObstacle(Direction.RIGHT) != 0){
							minDist = distanceArray[i];
							minIndex = i;
						}
						break;
					case 2:
						if ((robot.distanceToObstacle(Direction.LEFT) != 0)){
							minDist = distanceArray[i];
							minIndex = i;
						}
						break;
					case 3:
						if (robot.distanceToObstacle(Direction.BACKWARD) != 0){
							minDist = distanceArray[i];
							minIndex = i;
						}
						break;
				}
			}			
		}
		return minIndex;
	}
	
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setDistance(Distance distance) {
		distances = distance;
	}

	
	//Make compiler happy
	@Override
	public float getEnergyConsumption() { return 0; }

	@Override
	public int getPathLength() { return 0; }
}			