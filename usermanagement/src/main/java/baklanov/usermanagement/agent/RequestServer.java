package baklanov.usermanagement.agent;

import baklanov.usermanagement.User;
import baklanov.usermanagement.db.DaoFactory;
import baklanov.usermanagement.db.DatabaseException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class RequestServer extends CyclicBehaviour {
    @Override
    public void action() {
        ACLMessage message=myAgent.receive();
        if(message!=null) {
            if (message.getPerformative() == ACLMessage.REQUEST) {
                myAgent.send(createReply(message));
            } else {
                Collection users = parseMessage(message);
                ((SearchAgent) myAgent).showUsers(users);
            }
        }
        else {
            block();
        }
    }

    private Collection parseMessage(ACLMessage message) {
        Collection users=new LinkedList();
        String content = message.getContent();
        if(content!=null){
            StringTokenizer tokenizer= new StringTokenizer(content,";");
            while(tokenizer.hasMoreTokens()){
                String userInfo = tokenizer.nextToken();
                StringTokenizer tokenizer1 = new StringTokenizer(userInfo,",");
                String id = tokenizer1.nextToken();
                String firstName = tokenizer1.nextToken();
                String lastName = tokenizer1.nextToken();
                users.add(new User(new Long(id),firstName,lastName, null));
            }
        }
        return users;
    }

    private ACLMessage createReply(ACLMessage message) {
        ACLMessage reply = message.createReply();
        String content = message.getContent();
        StringTokenizer tokenizer=new StringTokenizer(content, ",");
        if (tokenizer.countTokens()==2){
            String firstName=tokenizer.nextToken();
            String lastName=tokenizer.nextToken();
            Collection users=null;
            try{
                users= DaoFactory.getInstance().getUserDao().find(firstName,lastName);
            } catch (DatabaseException e) {
                e.printStackTrace();
                users=new ArrayList(0);
            }
            StringBuffer buffer = new StringBuffer();
            for(Iterator it = users.iterator(); it.hasNext();){
                User user = (User) it.next();
                buffer.append(user.getId()).append(",");
                buffer.append(user.getFirstName()).append(",");
                buffer.append(user.getLastName()).append(",");
            }
            reply.setContent(buffer.toString());
        }
        return reply;
    }
}
