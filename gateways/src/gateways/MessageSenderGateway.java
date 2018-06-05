package gateways;

import org.apache.camel.Producer;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

public class MessageSenderGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private Producer producer;
    private String channelName;

    public MessageSenderGateway(String channelName) {
        this.channelName = channelName;
    }

    public Message createTextMessage(String body) {
        return null;
    }

    public void send(Message msg) {

    }
}

