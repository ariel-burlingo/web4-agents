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

public class PersonalAgent extends Agent {

   private static final long serialVersionUID = 1L;
   private String weather;
   private AID user;
   private AID[] tempAgents;

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
         }
      });

      this.addBehaviour(new TickerBehaviour(this, 1000 * 5) {
         private static final long serialVersionUID = 1L;

         @Override
         public void onTick() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("weather");
            sd.setName("weatheragent");
            template.addServices(sd);

            try {
               DFAgentDescription[] r = DFService
                     .search(myAgent, template);
               tempAgents = new AID[r.length];

               for (int i = 0; i < r.length; ++i)
                  tempAgents[i] = r[i].getName();

            } catch (FIPAException ex) {
               ex.printStackTrace();
            }
         }
      });

      this.addBehaviour(new CyclicBehaviour(this) {
         private static final long serialVersionUID = 1L;

         @Override
         public void action() {
            ACLMessage rec = receive(MessageTemplate
                  .MatchPerformative(ACLMessage.QUERY_IF));

            if (rec != null) {
               ACLMessage msgs = new ACLMessage(ACLMessage.QUERY_IF);
               msgs.addReceiver(tempAgents[0]);
               user = rec.getSender();
               msgs.setContent("Daj pogode");
               send(msgs);
            } else
               rec = receive();

            if (rec != null && rec.getSender() != getAMS()) {
               switch (rec.getPerformative()) {
               case ACLMessage.INFORM:
                  if (rec.getConversationId() != null
                        && rec.getConversationId().equalsIgnoreCase(
                              "inform"))
                     weather = rec.getContent();
                  else if (rec.getConversationId() != null
                        && rec.getConversationId().equalsIgnoreCase(
                              "query")) {
                     ACLMessage da0 = new ACLMessage(ACLMessage.INFORM);
                     da0.addReceiver(user);
                     da0.setContent(rec.getContent());
                     send(da0);
                  }
                  break;
               case ACLMessage.REQUEST:
                  ACLMessage receive = rec.createReply();
                  receive.setPerformative(ACLMessage.INFORM);
                  receive.setContent(weather);
                  send(receive);
                  break;
               default:
                  break;
               }
            }
         }
      });

      this.addBehaviour(new CyclicBehaviour(this) {
         private static final long serialVersionUID = 1L;

         @Override
         public void action() {
            MessageTemplate template = MessageTemplate.MatchSender(getAMS());

            ACLMessage req = receive(template);

            if (req != null) {
               Location loc = parseAMSResponse(req);

               if (loc != null) {
                  doMove(loc);
                  removeBehaviour(this);
               }
            }
         }
      });

      this.addBehaviour(new CyclicBehaviour(this) {
         private static final long serialVersionUID = 1L;

         @Override
         public void action() {
            if (tempAgents != null && tempAgents.length > 0) {
               ACLMessage req = prepareRequest(tempAgents[0]);
               send(req);
               removeBehaviour(this);
            }
         }
      });
   }

   private ACLMessage prepareRequest(AID agent) {
      ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
      request.addReceiver(getAMS());
      request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
      request.setOntology(MobilityOntology.NAME);
      request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

      Action act = new Action();
      act.setActor(getAMS());

      WhereIsAgentAction action = new WhereIsAgentAction();
      action.setAgentIdentifier(agent);
      act.setAction(action);

      try {
         getContentManager().fillContent(request, act);
      } catch (CodecException e) {
         e.printStackTrace();
      } catch (OntologyException e) {
         e.printStackTrace();
      }

      return request;
   }

   private Location parseAMSResponse(ACLMessage response) {
      Result results = null;

      try {
         results = (Result) getContentManager().extractContent(response);
      } catch (UngroundedException e) {
         e.printStackTrace();
      } catch (CodecException e) {
         e.printStackTrace();
      } catch (OntologyException e) {
         e.printStackTrace();
      }

      Iterator it = results.getItems().iterator();

      Location loc = null;

      if (it.hasNext())
         loc = (Location) it.next();

      return loc;
   }
}