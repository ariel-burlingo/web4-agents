package Agents;
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

public class ServerAgent extends Agent {

   private static final long serialVersionUID = 1L;
   private String dane;
   public Field OurMap[][];
   @Override
   protected void setup() {
      this.addBehaviour(new OneShotBehaviour(this) {
         private static final long serialVersionUID = 1L;

         @Override
         public void action() {
            getContentManager().registerLanguage(new SLCodec(),
                  FIPANames.ContentLanguage.FIPA_SL0);
            getContentManager().registerOntology(
                  MobilityOntology.getInstance());
            
          //  Sandbox mapa = new Sandbox(true, 0, 0);
          //  mapa.loadMap("");
         }
      });
      
      this.addBehaviour(new CyclicBehaviour(this) {
          private static final long serialVersionUID = 1L;

          @Override
          public void action() {
             ACLMessage rec = receive(MessageTemplate
                   .MatchPerformative(ACLMessage.QUERY_IF));
             rec = receive();
             
             if (rec != null && rec.getSender() != getAMS()) {
            	if(rec.getPerformative()==ACLMessage.INFORM){
                    if (rec.getConversationId() != null && rec.getConversationId()=="inform")
                         dane = rec.getContent();
                    
                    //odbior całej tablicy: Begin
                    String tab[] = dane.split(" ");
                    String rows[] = tab[2].split("R");

                    		OurMap = new Field[Integer.parseInt(tab[1])][Integer.parseInt(tab[0])];
                    		for(int i=0; i<Integer.parseInt(tab[0]); i++){
                    			System.out.println("");
                    			for(int j=0; j<Integer.parseInt(tab[1]); j++){
                    				if(rows[i+1].charAt(j)=='1'){
                    					OurMap[j][i]= new Field(true);
                    					System.out.print("X");
                    				}
                    				
                    				if(rows[i+1].charAt(j)=='0'){
                    					OurMap[j][i]= new Field(false);
                    					System.out.print(".");
                    				}
                    				
                    				if(rows[i+1].charAt(j)=='2'){
                    					OurMap[j][i]= new Field(false);
                    					OurMap[j][i].setAsExit();
                    					System.out.print("E");
                    				}
                    			}
                    		}
                    
                    //odbior całej tablicy: end
            		}
             	}
             }
      });
   }

}

