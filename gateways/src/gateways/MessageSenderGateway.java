package gateways;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

public class MessageSenderGateway {
    private Connection connection;
    private Session session;
    private Destination sendDestination;
    private Destination replyDestination;
    private MessageProducer producer;
    private Properties props;
    private String replyQueue;
    private String requestQueue;

    public MessageSenderGateway(String requestQueue, String replyQueue) {
        this.requestQueue = requestQueue;
        this.replyQueue = replyQueue;
    }


    public Message createTextMessage(String messageBody) {

        Message msg = null;
        try {
            props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("queue." + requestQueue), requestQueue);
            props.put(("queue." + replyQueue), replyQueue);

            Context jndiContext = new InitialContext(props);

            // Setup connection
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Setup destinations
            sendDestination = (Destination) jndiContext.lookup(requestQueue);
            replyDestination = (Destination) jndiContext.lookup(replyQueue);

            producer = session.createProducer(sendDestination);

            // Create a text message
            msg = session.createTextMessage(messageBody);

            // Set message reply destination
            msg.setJMSReplyTo(replyDestination);

            // Send the message
            producer.send(msg);

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        return msg;

    }

    public void send(Message msg) {

    }
}

