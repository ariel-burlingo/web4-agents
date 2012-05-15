package Agents;
import java.util.Random;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;

public class RobotAgent extends Agent {

   private static final long serialVersionUID = 1L;
   public Sandbox mapa;
   public Field OurMap[][];// = new Field[mapa.getWidth()][mapa.getHeight()];
   public int x=0;
   public int y=0;
   
   public boolean isDeadEnd(){
	   try{
		   if(OurMap[x+1][y].isObstacle() 
				   && OurMap[x-1][y].isObstacle()
				   && OurMap[x][y+1].isObstacle()
				   && OurMap[x][y-1].isObstacle()
				   ){
			   	return true;
			   }
		   } catch(Exception e){
			   return false;
		   }
	   return false;
   }
   
   public boolean canMove(int posX, int posY){
	   try{
		   if(mapa.map[posX][posY].isObstacle()){
			   System.out.println("Obstacle @ " + posX + ":" + posY);
			   return false;
		   } else {
			   System.out.println("Free @ " + posX + ":" + posY);
			   return true;
		   }
	   } catch(Exception e) { // array out of bound etc
		   //System.out.println("STH Gone wrong - while trying to move to " + posX + ":" + posY);
		   return false;		   		   
	   }
   }
   
   public void move(int posX, int posY){
	   if(isDeadEnd())
		   throw new Error("Dead end - app will close - i.e agent should die");
	   if(canMove(posX, posY)){
		  x = posX;
		  y = posY;
	   } else { // can't move post action
		   try{		   
			   OurMap[posX][posY].setObstacle(true);
			   System.out.println("Obstacle add " + posX + ":" + posY);
		   } catch(Exception e){
			   try{
				   OurMap[posX][posY] = new Field(true);
				   System.out.println("Obstacle init " + posX + ":" + posY);
			   }catch(Exception e2){
				   System.err.print(e2.getMessage());
				   System.out.println("STH Gone terribly wrong - while moving - tried"  + posX + ":" + posY);
			   }
		   }
		  	
	   }
	   System.out.println("Position " + x + ":" + y);	   
   }

   
   @Override
   protected void setup(){
       System.out.println("1");
       this.addBehaviour(new OneShotBehaviour(this) {
         private static final long serialVersionUID = 1L;
         
         @Override
         public void action() {
            getContentManager().registerLanguage(new SLCodec(),
                  FIPANames.ContentLanguage.FIPA_SL0);
            getContentManager().registerOntology(
                  MobilityOntology.getInstance());

            mapa = new Sandbox(true, 0, 0); // Tu siedzą smoki
            // initialize ourMap
            OurMap = new Field[mapa.getWidth()][];
            int i=0;
            while(i < mapa.getWidth()){
            	OurMap[i] = new Field[mapa.getHeight()];
            	i++;
            }
            
            
            Random rand = new Random();
            System.out.println("1");
           x = Math.abs(rand.nextInt())%mapa.getWidth();
           y = Math.abs(rand.nextInt())%mapa.getHeight();
           System.out.println("2");
           System.out.println(mapa.map[0][0].isObstacle());
           while(mapa.map[x][y].isObstacle()){
        	   System.out.println("3");
        	   x = Math.abs(rand.nextInt())%mapa.getWidth();
               y = Math.abs(rand.nextInt())%mapa.getHeight();	
           }
           
         }
      });
       
            
      this.addBehaviour(new TickerBehaviour(this, 1000 * 1) { // faster!
          private static final long serialVersionUID = 1L;

          @Override
          public void onTick() {
        	  Random go = new Random();
        	  int direction = go.nextInt()%4; // 0 top, 1 right, 2 bottom, 3 left    
        	  switch(direction){
        	  	case 0:
        	  		move(x,y+1);
        	  		break;
        	  	case 1:
        	  		move(x+1,y);
        	  		break;
        	  	case 2:
        	  		move(x,y-1);
        	  		break;
        	  	case 3:
        	  		move(x-1,y);
        	  		break;	
        	  }
        	  
        	 
        	  
        	  /*
             ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
             msg.setConversationId("request");
             msg.addReceiver(new AID("RB", AID.ISLOCALNAME));
             msg.setContent("spam, spam, spam");
             send(msg);
            */
             
          }
       });
   }

}
