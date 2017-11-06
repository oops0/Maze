package falstad;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.Distance;

/**
 * Pledge driver moves in a direction until it hits an obstacle
 * Uses Pledge's algorithm to traverse the obstacle and continue in main direction
 * 
 * Collaborators: MazeController, BasicRobot
 * @author scstew
 */

public class Pledge implements RobotDriver {

	BasicRobot robot;
	
	public Pledge() { }
	
	@Override
	public boolean drive2Exit() throws Exception {
		robot.getController().showMaze = true;
		robot.getController().mapMode = true;
		
		int counter = 0;
		
		//Main movement
		//Begin if sufficient battery level
		while (!robot.isAtExit() && robot.getBatteryLevel() > 0) {
			//Default case: obstacle counter at zero and able to move forward
			if (counter == 0 && robot.distanceToObstacle(Direction.FORWARD) != 0) {
				robot.move(1, false);
				Thread.sleep(50);
			}
			
			//Obstacle counter at 0 but unable to move forward; turn left
			else if (counter == 0 && robot.distanceToObstacle(Direction.FORWARD) == 0) {
				robot.rotate(Turn.LEFT);
				counter--;
				Thread.sleep(50);
			}
			
			//Obstacle counter is not 0 so rotate, update counter, and move appropriately
			else {
				//Move forward if we can, because we must have just turned if the counter is not zero
				if (robot.distanceToObstacle(Direction.FORWARD) != 0) {
					robot.move(1, false);
					Thread.sleep(50);
					
					//If we move forward and a right path opens up, turn right
					if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
						robot.rotate(Turn.RIGHT);
						counter++;
						Thread.sleep(50);
					}
				}
				
				//Can't move forward and counter is not zero, so turn left
				else {
					robot.rotate(Turn.LEFT);
					counter--;
					Thread.sleep(50);
				}
			}
			
			//Print current counter to console for debugging
			System.out.println(counter);
		}
		
		if(!(robot.getBatteryLevel() > 0)) {
			return false;
		}
		
		// If robot is at the exit, drive into it
		if (robot.canSeeExit(Direction.LEFT)) {
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
			Thread.sleep(50);
		}
		
		else if (robot.canSeeExit(Direction.RIGHT)){
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
			Thread.sleep(50);
		}
		
		else if (robot.canSeeExit(Direction.FORWARD)){
			robot.move(1, false);
			Thread.sleep(50);
		}
		
		else if (robot.canSeeExit(Direction.BACKWARD)){
			robot.rotate(Turn.AROUND);
			robot.move(1, false);
			Thread.sleep(50);
		}
		
		return false;
	}
	
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
	}

	
	//Make the compiler happy
	@Override
	public void setDimensions(int width, int height) {	}

	@Override
	public void setDistance(Distance distance) { }
	
	@Override
	public float getEnergyConsumption() { return 0;	}

	@Override
	public int getPathLength() { return 0; }

}
