package rav.example;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.qatools.properties.PropertyLoader;

import javax.jms.JMSContext;
import javax.jms.JMSException;

public class TestBase {
    protected static JMSContext context;
    protected static MqProperties properties = PropertyLoader.newInstance()
            .populate(MqProperties.class);
    protected static Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeAll
    static void setUp() {
        try {
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, properties.getMqHostName());
            cf.setIntProperty(WMQConstants.WMQ_PORT, properties.getMqPort());
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, properties.getMqChannel());
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, properties.getMqQueueManager());
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, properties.getMqAppName());
//            cf.setStringProperty(WMQConstants.USERID, properties.getUserId());
//            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
//            cf.setStringProperty(WMQConstants.PASSWORD, properties.getPasswd());

            context = cf.createContext();
        } catch (JMSException jmsEx) {
            recordFailure(jmsEx);
        }
    }

    @AfterAll
    static void tearDown() {
        context.close();
    }

    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                logger.info(String.valueOf(ex));
            }
        }
    }

    private static void processJMSException(JMSException jmsEx) {
        Throwable innerException = jmsEx.getLinkedException();
        if (innerException != null) {
            logger.info("Inner exception(s):");
            while (innerException != null) {
                logger.info(String.valueOf(innerException));
                innerException = innerException.getCause();
            }
        } else {
            logger.info(String.valueOf(jmsEx));
        }
    }
}
