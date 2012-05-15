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
	private boolean obstacle;

	public Field(){
		this.obstacle = false;
	}
	
	public Field(boolean obstacle) {
		this.obstacle = obstacle;
		//this.recursionDeep = recursion;	
	}
	

	public boolean isObstacle() {
		return obstacle;
	}

	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}

	
	
	
}