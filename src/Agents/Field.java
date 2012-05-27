package Agents;

public class Field {
	
	
	private boolean exit; 	
	private boolean obstacle;

	public Field(){
		this.obstacle = false;
		this.exit = false;
	}
	
	public Field(boolean obstacle) {
		this.obstacle = obstacle;
		this.exit = false; // always defaults to false;
	}
	

	public boolean isObstacle() {
		return obstacle;
	}

	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}
	
	public boolean isExit() {
		return exit;
	}
	
	public void setAsExit(){
		this.exit = true;
	}

	
	
	
}