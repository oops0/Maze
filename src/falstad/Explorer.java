package falstad;

import generation.Distance;

public class Explorer implements RobotDriver {

	BasicRobot robot;
	
	public Explorer() {
		robot = new BasicRobot();
	}
	
	@Override
	public boolean drive2Exit() throws Exception {
		/*
		 * while !atExit:
		 *  
		 */
		
		
		
		
		return false;
	}
	
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		robot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDistance(Distance distance) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
