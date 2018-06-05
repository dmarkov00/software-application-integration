package gateways;
import org.apache.camel.Consumer;

import javax.jms.*;

public class MessageReceiverGateway {

    private Connection connection;
    private Session session;
    private Destination destination;
    private Consumer consumer;
    private String channelName;

    public void MessageReceiverGateway(String channelName) {
        this.channelName = channelName;
    }


}
