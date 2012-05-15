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
            
            Sandbox mapa = new Sandbox(true, 0, 0);
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
                    if (rec.getConversationId() != null && rec.getConversationId()=="request")
                         dane = rec.getContent();
                    System.out.println("Otrzymalem: "+dane);
            		}
             	}
             }
      });
   }

}

