package Agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class WeatherAgent extends Agent {

   private static final long serialVersionUID = 1L;
   private String[] array = new String[] { "Slonecznie", "Zachmurzenie", "Deszcz", "Snieg" };

   @Override
   protected void setup() {

      DFAgentDescription dfa = new DFAgentDescription();
      dfa.setName(this.getAID());
      ServiceDescription sd = new ServiceDescription();
      sd.setType("weather");
      sd.setName("weatheragent");
      dfa.addServices(sd);

      try {
         DFService.register(this, dfa);
      } catch (FIPAException ex) {
         ex.printStackTrace();
      }

      this.addBehaviour(new TickerBehaviour(this, 1000 * 5) {
         private static final long serialVersionUID = 1L;

         @Override
         public void onTick() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setConversationId("inform");
            msg.addReceiver(new AID("PA", AID.ISLOCALNAME));
            Random r = new Random();
            int index = r.nextInt(2) % 4;
            msg.setContent(array[index]);
            send(msg);
         }
      });

      this.addBehaviour(new CyclicBehaviour(this) {

         private static final long serialVersionUID = 1L;

         @Override
         public void action() {
            ACLMessage rec = receive();

            if (rec != null) {
               switch (rec.getPerformative()) {
               case ACLMessage.QUERY_IF:
                  ACLMessage receive = rec.createReply();
                  receive.setPerformative(ACLMessage.INFORM);
                  receive.setConversationId("query");
                  Random r = new Random();
                  int index = r.nextInt(2) % 4;
                  receive.setContent(array[index]);
                  send(receive);
                  break;
               default:
                  break;
               }
            }

         }
      });
   }
}


