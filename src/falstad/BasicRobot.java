package falstad;

import falstad.MazeController.UserInput;
import generation.CardinalDirection;
import generation.Cells;


/**
 * Implements Robot with "basic" functionality
 */

public class BasicRobot implements Robot {

	private Cells cells;
	private MazeController maze;
	private CardinalDirection currentDirection;
	private boolean stopped;
	private float batteryLevel;
	private int odometerReading;
	private int[] currentPosition;
	private boolean frontSensor;
	private boolean backSensor;
	private boolean leftSensor;
	private boolean rightSensor;
	
	public BasicRobot() {
		maze = null;
		stopped = false;
		batteryLevel = 3000;
		odometerReading = 0;
		currentPosition = new int[2];
		currentPosition[0] = 0;
		currentPosition[1] = 0;
		frontSensor = true;
		backSensor = true;
		leftSensor = true;
		rightSensor = true;
		currentDirection = CardinalDirection.East;
	}
	
	@Override
	public void rotate(Turn turn) {		
		switch(turn) {
			case RIGHT:
				//If battery is OK, rotate clockwise to simulate turning right 
				if (batteryLevel >= 3) {
					currentDirection = currentDirection.rotateClockwise();
					batteryLevel -= 3;
					maze.operateGameInPlayingState(UserInput.Right);
					
					System.out.println("Battery: " + batteryLevel + " Odometer: " + odometerReading);
				}
				//Else stop the robot
				else { stopped = true; }
				break;
			case LEFT:
				if (batteryLevel >= 3) {
					currentDirection = currentDirection.rotateCounterClockwise();	
					batteryLevel -= 3;
					maze.operateGameInPlayingState(UserInput.Left);
					
					System.out.println("Battery: " + batteryLevel + " Odometer: " + odometerReading);
				}
				else { stopped = true; }
				break;
			case AROUND:
				//This move costs 6 because it really needs two moves
				if (batteryLevel >= 6) {
					currentDirection = currentDirection.oppositeDirection();
					maze.operateGameInPlayingState(UserInput.Left);
					maze.operateGameInPlayingState(UserInput.Left);
					batteryLevel -= 6;
					
					System.out.println("Battery: " + batteryLevel + " Odometer: " + odometerReading);
				}
				else { stopped = true; }
				break;
		}
	}

	@Override
	public void move(int distance, boolean manual) {
		while (distance > 0) {
			//Forward move costs 5; will not move without enough battery
			if (batteryLevel >= 5) {
				//Manual mode only does one move at a time
				if (manual == true) { distance = 1; }
				
				//If robot isn't standing in front of a wall
				if (distanceToObstacle(Direction.FORWARD) > 0) {
					maze.operateGameInPlayingState(UserInput.Up);
					batteryLevel -= 5;
					odometerReading++;
					distance--;
					
					System.out.println("Battery: " + batteryLevel + " Odometer: " + odometerReading);
				}
			}
			
			//Didn't move because not enough battery. Stopping.
			else { stopped  = true;	distance = 0; }
		}
	}

	@Override
	public void setMaze(MazeController maze1) {
		//Update Robot's controller object and current position
		maze = maze1;
		currentPosition = maze1.getCurrentPosition();
		
		//Update Robot's cells
		cells = maze.getMazeConfiguration().getMazecells();
		System.out.println(">>>>>>>>>>>>>>>"+cells);
	}

	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		//Can only check for the exit in given direction if the robot can sense in that direction
		if (this.hasDistanceSensor(direction) == true) {
			//Costs 1
			batteryLevel -= 1;
			
			//Distance to the nearest wall is "infinity" if we're looking at the exit
			if (distanceToObstacle(direction) == Integer.MAX_VALUE) { return true; }
			else { return false; }
		}
		//Throw unsupported operation exception because robot does not have the sensor
		else { throw new UnsupportedOperationException(); }
	}
	
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		switch(direction) {
			case RIGHT:
				return rightSensor;
			case LEFT:
				return leftSensor;
			case FORWARD:
				return frontSensor;
			case BACKWARD:
				return backSensor;
			default:
				return false;
		}
	}
	
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		batteryLevel -= 1;
		if(batteryLevel <= 0){
			stopped = true;
			return 0;
		}
		if(!hasDistanceSensor(direction)){
			throw new UnsupportedOperationException();
		}
		
		//Setup 
		cells = maze.getMazeConfiguration().getMazecells();
		int posX = maze.getCurrentPosition()[0];
		int posY = maze.getCurrentPosition()[1];
		int[] dir = currentDirection.getDirection();
		int x = dir[0];
		int y = dir[1];
		int temp;
		if(direction == Direction.BACKWARD){
			x = -x;
			y = -y;
		}else if(direction == Direction.LEFT){
			temp = x;
			x = -y;
			y = temp;	
		}else if(direction == Direction.RIGHT){
			temp = x;
			x = y;
			y = -temp;
		}
		
		//Main function
		CardinalDirection tmpDirection = CardinalDirection.getDirection(x, y);
		int distance = 0;
		while(!cells.hasWall(posX, posY, tmpDirection)){
			if(tmpDirection == CardinalDirection.North)		 { posY--; }
			else if(tmpDirection == CardinalDirection.South) { posY++; }
			else if(tmpDirection == CardinalDirection.East)	 { posX++; }
			else if(tmpDirection == CardinalDirection.West)	 { posX--; }
			
			
			if(posX >= maze.getMazeConfiguration().getWidth() || posX < 0 || posY >= maze.getMazeConfiguration().getHeight() || posY < 0){
				return Integer.MAX_VALUE;
			}
			distance++;
		}
		return distance;
	}
	
	/* Basic set/get functions */
	@Override
	public void setBatteryLevel(float level) 
		{ batteryLevel = level; }
	
	@Override
	public void resetOdometer() 
		{ odometerReading = 0; }
	
	@Override
	public MazeController getController() {
		return maze;
	}
	
	@Override
	public int[] getCurrentPosition() throws Exception
		{ return maze.getCurrentPosition(); }
	
	@Override
	public float getBatteryLevel() 
		{ return batteryLevel; }
	
	@Override
	public int getOdometerReading() 
		{ return odometerReading; }
	
	@Override
	public float getEnergyForFullRotation() 
		{ return 12; }
	
	@Override
	public float getEnergyForStepForward() 
		{ return 5; }
	
	@Override
	public CardinalDirection getCurrentDirection() 
		{ return maze.getCurrentDirection(); }
	
	@Override
	public boolean hasRoomSensor() 
		{ return true; }
	
	@Override
	public boolean hasStopped() 
		{ return stopped; }
	
	@Override
	public boolean isAtExit() {
		return maze.getMazeConfiguration().getMazecells().isExitPosition(maze.getCurrentPosition()[0], maze.getCurrentPosition()[1]); 
	}
	
	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException	{ 
		System.out.println(maze);
		System.out.println(maze.getMazeConfiguration());
		System.out.println(maze.getMazeConfiguration().getMazecells());
		return maze.getMazeConfiguration().getMazecells().isInRoom(currentPosition[0],currentPosition[1]);	
	}
}
