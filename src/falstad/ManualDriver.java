package falstad;

import falstad.MazeController.UserInput;
import falstad.Robot.Turn;
import generation.Distance;

/**
 * Implements RobotDriver with Manual Input
 */

public class ManualDriver implements RobotDriver {
	
	public BasicRobot robot;
	public int width, height;
	public Distance distance;
	
	public ManualDriver() {	}
	
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
	public float getEnergyConsumption() {
		return ( 2500 - robot.getBatteryLevel() );
	}

	@Override
	public int getPathLength() {
		return robot.getOdometerReading();
	}
	
	public boolean keyDown(UserInput uikey, int value) {
		float newBattery = robot.getBatteryLevel();
		
		switch (uikey) {
			case Up: 
				System.out.println("Up");
				robot.move(1, true);
				robot.getController().keyDown(uikey, value);
				break;
			case Left: 
				robot.rotate(Turn.LEFT);
				robot.getController().keyDown(uikey, value);
				break;
			case Right: // turn right
				robot.rotate(Turn.RIGHT);
				robot.getController().keyDown(uikey, value);
				break;
			case Down: // move backward
				robot.rotate(Turn.AROUND);
				robot.setBatteryLevel(newBattery);
				robot.move(1, true);
				robot.setBatteryLevel(newBattery);
				robot.rotate(Turn.AROUND);
				newBattery -= 5;
				robot.setBatteryLevel(newBattery);
				robot.getController().keyDown(uikey, value);
				break;
			default:
				robot.getController().keyDown(uikey, value);
				break;
		}
		return true;
	}
	
	@Override
	public boolean drive2Exit() throws Exception { 
		while(true) {
			if(robot.isAtExit()) {
				break;
			}
		}
		return false;
	}
	
	@Override
	public void setDistance(Distance distance) { this.distance = distance; }


}
