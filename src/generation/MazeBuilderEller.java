package generation;
import java.util.Arrays;


/**
 * Everything performed by this class outside of constructors is contained in the generatePathways method
 * <p>
 * The code contained in generatePathways has heavy non-javadoc commenting to describe algorithms and purpose of variables 
 * as I did not feel it was necessary to create additional methods
 */
public class MazeBuilderEller extends MazeBuilder implements Runnable {
	/**
	 * Default constructor
	 */
	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	/**
	 * Constructor for deterministic input
	 * @param det
	 */
	public MazeBuilderEller(boolean det) {
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	@Override
	protected void generatePathways() {
		//Declare and initialize the key variables for generating pathways
		//cellValues is an array representation of the current maze with values that correspond to "sets" in Eller's algorithm
		Wall curWall = new Wall(1,1,CardinalDirection.East); 
		int[][] cellValues = new int[height][width];
				
		//Give each cell in the array a unique number to identify its set  
		//A 3x3 maze fills from '1' in [0][0] scaling up to '9' in [2][2]
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(cells.isInRoom(j, i)) {
					cellValues[i][j] = 9999;
				}
				else {
					cellValues[i][j] = i*height + j + 1;	
				}
			}
		}
		
		//Eller's algorithm goes row by row and has a horizontal stage followed by a vertical stage
		//Loop finishes once every cell except the last cell has been visited
		for(int row = 0; row < height; row++) { 					//For every row in the maze
			for(int col = 0; col < width - 1; col++) { 				//Number of walls = width - 1, so inner loop runs 1 less time
				curWall.setWall(col, row, CardinalDirection.East); 	//Current operable wall = east of current cell 				
				
				// Special case for the last row in the generation
				if(row == height-1) {
					if(cellValues[row][col] != cellValues[row][col+1]) //Merge if not in the same set
					{
						cells.deleteWall(curWall);
					}
				}
				
				// All other rows
				else {
					int flip = random.nextIntWithinInterval(0, 1); 	//50% chance that non-matching sets will merge
					int absorbedSet = cellValues[row][col+1];		//Holds the value of the set that might be absorbed
					int tempCol = col;								//Allows us to manipulate the current column without breaking everything
					int count = 1;									//Stores the size of the set that might be absorbed
					
					//If cells differ sets and the RNG passed we will merge
					if( (cellValues[row][col] != absorbedSet) && (flip == 1) ) {
						//Increment count until it is the size of the absorbed set
						while(tempCol + 2 < width) {
							if(cellValues[row][tempCol + 2] == absorbedSet) { count++; } //We originally checked col+1, so +2 is the next one over
							tempCol++; //Move to the next neighbor
						}
						//Delete count # of walls and merge sets
						for (int i = 0; i < count; i++) {
							curWall.setWall(col + i, row, CardinalDirection.East);
							if(cells.canGo(curWall)) {
								cells.deleteWall(curWall);
								cellValues[row][col+i+1] = cellValues[row][col];
							}
						}
					}
					//Correctly reposition the column marker after working with a set. If set size is 1, increments by 1 naturally from the for loop 
					col += count - 1;
				}
			}
						
			//Second loop carves "south"
			if(row == height - 1) {break;} //We don't carve vertically on the last row
			else { 
				for(int col = 0; col < width; col++) {
					curWall.setWall(col, row, CardinalDirection.South);
					int tempCol = col;
					int count = 1;
	
					//Get size of set
					while(tempCol + 1 != width) { // Not at edge
						if(cellValues[row][tempCol + 1] == cellValues[row][tempCol]) { count++; } // Count++ if next is in set	
						else { break; } // Break count if not
						if(tempCol + 1 == width - 1) { break; }
						else { tempCol++; }}
						
					//If size of set is 1, guaranteed vertical path
					if(count == 1) {
						cells.deleteWall(curWall);
						cellValues[row+1][col] = cellValues[row][col];
					}
					else {
						int ranNum = random.nextIntWithinInterval(0, count-1); //Used pick a random cell in the current set
						int maxTries = 250; //If we failed this many times it's probably a room that we need to force our way into
						int x = 0;
						tempCol = col;
						boolean didMove = false; //Used to ensure that we always destroy at least 1 wall per set
						
						while( (x < maxTries) && (didMove == false) ) { //This loop ends after the first deletion
							tempCol = col + ranNum;
							curWall.setWall( tempCol, row, CardinalDirection.South ); //Choose a wall to carve randomly
							if(cells.canGo(curWall)) { //Fails if the wall has a border flag, for example
								cells.deleteWall(curWall); 
								cellValues[row+1][tempCol] = cellValues[row][tempCol]; //Update sets
								didMove = true; //Required deletion is done 
							}
							else { 
								ranNum = random.nextIntWithinInterval(0, count-1); //Try a different wall next time 
								x++; 
							}
						}
					
						if(didMove == true) { //Already did 1 move, so we don't care if they all fail	
							tempCol = col;
							for(int i = 1; i < count; i++) { //Try each vertical wall in the set once
								curWall.setWall(tempCol, row, CardinalDirection.South);	
								int roll = random.nextIntWithinInterval(0, 1);
								if( (roll == 1) && (cells.canGo(curWall)) ) {
									cells.deleteWall(curWall);
									cellValues[row+1][tempCol] = cellValues[row][tempCol];
								}
								else { tempCol++; }
							}
						}
						else { //maxTries failed, so force a deletion
							curWall.setWall( tempCol, row, CardinalDirection.South );
							cells.deleteWall(curWall); 
							cellValues[row+1][tempCol] = cellValues[row][tempCol];	
						}
						col = col + (count - 1); //After iterating through a set move the original counter to the end of it
					}
				}
			}
		}
	}
}

