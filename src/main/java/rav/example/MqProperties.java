package rav.example;

import ru.qatools.properties.Property;
import ru.qatools.properties.Resource;

@Resource.Classpath("mq.properties")
public interface MqProperties {
    @Property("mq.host.name")
    String getMqHostName();

    @Property("mq.port")
    int getMqPort();

    @Property("mq.channel")
    String getMqChannel();

    @Property("mq.queue.manager")
    String getMqQueueManager();

    @Property("mq.queue.name")
    String getMqQueueName();

    @Property("mq.app.name")
    String getMqAppName();
}
