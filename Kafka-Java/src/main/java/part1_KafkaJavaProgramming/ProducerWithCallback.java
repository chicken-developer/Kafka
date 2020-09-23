package part1_KafkaJavaProgramming;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {
    public static void main(String[] args) {
        //Init value and variable
        final Logger logger = LoggerFactory.getLogger(ProducerWithCallback.class);
        String bootstrapServers = "127.0.0.1:9092";

        //Create Producer properties
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Create the producer & producer record
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int i =0; i < 10; i++){
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("quickstart-events", "Hello Kafka from Java " + Integer.toString(i));

            //Send data
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //executed every time a record is successfully sent or an exception thrown
                    if (e == null){
                        //the record was successfully sent
                        logger.info("Received new data: \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp() + "\n" );
                    }else {
                        logger.error("Error when receiving data", e);
                    }
                }
            });
        }

        producer.flush();
        producer.close();
    }
}
