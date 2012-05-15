package Agents;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import Agents.Field;

public class Sandbox {
	
	//public static int recursionLevel = 2;
	private int width;
	private int height;
	//public static boolean isConstructed = false;
	public Field map [][];
	
	public Sandbox(boolean fromFile, int width, int height) {
		// TODO Auto-generated method stub
		/*if(Sandbox.isConstructed){
			System.out.print("Singletone already constructed!");
			throw new InstantiationError("Only one instance allowed!");
			//return this;
		} else {*/
			if(fromFile){
				this.loadMap("sample.map");
				//return this;
			} else {
				this.width = width;
				this.height = height;
				this.map = new Field [width][height];
				//Sandbox.isConstructed = true;
				//return this;
			}
		//}			
	}

	
	// getters setters
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void loadMap(String fileName){
		// Loads map from file
		
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // get first line
            strLine = br.readLine();
            Scanner scanner = new Scanner(strLine);
            scanner.useDelimiter(" ");
            if ( scanner.hasNext() ){
            	this.width = Integer.parseInt(scanner.next());
            	this.height = Integer.parseInt(scanner.next());
            }
            // Create empty sandbox for data
            this.map = new Field[this.width][this.height];
            
            // Parsing map
            System.out.println("Loading maze");
            int i=0; // i = current line
            while ((strLine = br.readLine()) != null) {
            	int characterIdx = 0;
            	while(characterIdx < this.width){
            		char data = strLine.charAt(characterIdx);
            		switch(data){
            	  		case 'X':  this.map[characterIdx][i] = new Field(true);
            	  		break;
            	  		case '.': this.map[characterIdx][i] = new Field(false);
            	  		break;
            		}
            		characterIdx++;
            		System.out.print(data);
            	}
                System.out.println();
                i++;
            }
            System.out.println();
            //Close the input stream
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            //throw new UnsupportedOperationException();
        }
    	System.out.println("Sandbox constructed");
    	//this.printMap();
	}
	
	public void printMap(){
		System.out.println("Debug, maze loaded to memory:");
		int i=0,j=0;
		for(i=0; i<this.getHeight(); i++){
			for(j=0; j<this.getWidth(); j++){
				if(this.map[j][i].isObstacle())
					System.out.print('X');
				else 
					System.out.print('.');
			}
			System.out.println();		
		}	
	}
	
}