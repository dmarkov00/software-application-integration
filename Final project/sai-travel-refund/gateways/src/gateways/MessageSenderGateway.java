package gateways;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessageSenderGateway {
    private Session session;
    private MessageProducer producer;
    private Context jndiContext;
    private String replyQueue;
    private String requestQueue;

    public MessageSenderGateway(String requestQueue, String replyQueue) {
        this.requestQueue = requestQueue;
        this.replyQueue = replyQueue;

        try {
            // Setup context
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("queue." + requestQueue), requestQueue);
            props.put(("queue." + replyQueue), replyQueue);

            this.jndiContext = new InitialContext(props);

            // Setup connection
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination sendDestination = (Destination) jndiContext.lookup(requestQueue);

            producer = session.createProducer(sendDestination);

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }


    public Message createTextMessage(String messageBody) {

        Message msg = null;
        try {
            Destination replyDestination = (Destination) jndiContext.lookup(replyQueue);

            // Create a text message
            msg = session.createTextMessage(messageBody);

            // Set message reply destination
            msg.setJMSReplyTo(replyDestination);


        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        return msg;

    }

    public void send(Message msg) {
        try {
            // Send the message
            producer.send(msg);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

