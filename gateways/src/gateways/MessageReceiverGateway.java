package gateways;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

public class MessageReceiverGateway {

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private String channelName;
    private Properties props;

    public MessageReceiverGateway(String channelName) {
        this.channelName = channelName;
    }

    public void setListener(MessageListener ml) {

        try {

            props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("queue." + channelName), channelName);

            Context jndiContext = new InitialContext(props);

            // Setup of connection
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Connect to the destination
            destination = (Destination) jndiContext.lookup(channelName);
            consumer = session.createConsumer(destination);

            // Set the listener
            consumer.setMessageListener(ml);

            connection.start(); // This is needed to start receiving messages

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

}
