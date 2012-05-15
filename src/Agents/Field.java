package Agents;

public class Field {
	
	// Enum for recursion
	/*public enum Position {
		TL,TC,TR,
		CL,CC,CR,
		BL,BC,BR,
		TOP
		};
	*/
	//private int recursionDeep;
	//private Field parentBlock = null;
	//private Position relativePos = Position.TOP;
	public boolean obstacle = false;

	public Field(boolean obstacle) {
		this.setObstacle(obstacle);
		//this.recursionDeep = recursion;	
	}
	

	public boolean isObstacle() {
		return obstacle;
	}

	private void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}

	
	
	
}