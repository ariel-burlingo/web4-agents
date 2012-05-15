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
   public Field OurMap[][]; //= new Field[mapa.getWidth()][mapa.getHeight()];
   public int x=0;
   public int y=0;
   

   
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
      
      this.addBehaviour(new TickerBehaviour(this, 1000 * 5) {
          private static final long serialVersionUID = 1L;

          @Override
          public void onTick() {
        	  Random go = new Random();
        	  if(go.nextInt()%2==1){
        		  if(go.nextInt()%2==1 && x-1>0){
        			  if(mapa.map[x-1][y].isObstacle()){
        			  	OurMap[x-1][y].obstacle=true;
        		  	}else{
        			  	OurMap[x-1][y].obstacle=false;
        			  	x=x-1;
        		  	}
        		  }else{
        			  if(x+1<100)
        		  	if(mapa.map[x+1][y].isObstacle()){
        			  	OurMap[x+1][y].obstacle=true;
        		  	}else{
        			  	OurMap[x+1][y].obstacle=false;
        			  	x=x+1;
        		  	}
        	  	}
        		  System.out.println(x+ " "+ y);
        	  }else{
        		  System.out.println(x+ " "+ y);
        		  if(go.nextInt()%2==1){
        			  if(mapa.map[x][y-1].isObstacle() && y-1>0){
        			  	OurMap[x][y-1].obstacle=true;
        		  	}else{
        			  	OurMap[x][y-1].obstacle=false;
        			  	y=y-1;
        		  	}
        		  }else{
        			  if(y+1<100)
        		  	if(mapa.map[x][y+1].isObstacle()){
        			  	OurMap[x][y+1].obstacle=true;
        		  	}else{
        			  	OurMap[x][y+1].obstacle=false;
        			  	y=y+1;
        		  	}
        		  }
        		  System.out.println(x+ " "+ y);
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
