package falstad;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.Distance;

/**
 * WallFollower driver clings to the wall on the left hand side until it reaches the exit
 * Can only sense front and left
 * 
 * Collaborators: MazeController, BasicRobot
 * @author scstew
 */

public class WallFollower implements RobotDriver {

	public BasicRobot robot;
	
	public WallFollower() {	}
	
	
	@Override
	public boolean drive2Exit() throws Exception {
		/* 
		* while not at exit:
		* 	if in a room:
		* 		move towards a position where wall is on left and can move forward
		* 	else if there is a wall on the left and you can move forward:
		* 		move forward
		* 	else if wall on left and can't move forward:
		* 		turn right
		* 	else:
		* 		turn left
		*/
		
		robot.getController().showMaze = true;
		robot.getController().showSolution = true;
		robot.getController().mapMode = true;
		
		//Main movement
		while (!robot.isAtExit()) {
			if (robot.getBatteryLevel() > 0) { 
				
				//If no left wall, turn left and move
				if (robot.distanceToObstacle(Direction.LEFT) > 0) {
					robot.rotate(Turn.LEFT);
					robot.move(1, false);
					Thread.sleep(50);
				}
				
				//If only left wall, move 
				else if(robot.distanceToObstacle(Direction.LEFT) == 0 && robot.distanceToObstacle(Direction.FORWARD) > 0) {
					robot.move(1, false);
					Thread.sleep(50);
				}
				
				//Else both walls, so turn right
				else {
					robot.rotate(Turn.RIGHT);
					Thread.sleep(50);
				}
			}
			else {
				return false;
			}
		}
		
		//Get to win screen
		if(robot.isAtExit()) {
			//Exit is ahead so move into it
			if (robot.canSeeExit(Direction.FORWARD)){
				robot.move(1, false);
				Thread.sleep(50);
			}
			
			//At exit but not in front, so keep turning left until it is in front, then move into it
			else {
				robot.rotate(Turn.LEFT);
				Thread.sleep(50);
				
				if (robot.canSeeExit(Direction.FORWARD)){ 
					robot.move(1, false); 
				}
				
				//Missed it, turn again
				else { 
					robot.rotate(Turn.LEFT); 
					Thread.sleep(50); 
					
					if (robot.canSeeExit(Direction.FORWARD)){ 
						robot.move(1, false); 
					}
					
					//Missed it, turn again
					else {
						robot.rotate(Turn.LEFT); robot.move(1, false); 
						Thread.sleep(50); 
					}
				}
			}
		}

		return true;
	}
	
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
	}

	
	//Make the compiler happy
	@Override
	public void setDimensions(int width, int height) { }

	@Override
	public void setDistance(Distance distance) { }
	
	@Override
	public float getEnergyConsumption() { return 0; }

	@Override
	public int getPathLength() { return 0; }
}
