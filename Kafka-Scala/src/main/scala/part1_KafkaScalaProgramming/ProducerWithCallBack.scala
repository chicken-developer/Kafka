package part1_KafkaScalaProgramming

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

object ProducerWithCallBack extends App{
  val bootstrapServer = "127.0.0.1:9092"
  val logger = LoggerFactory.getLogger(IntroProducer.getClass)
  //Create properties
  val kafkaProducerProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrapServer)
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)
    props
  }
  val producer = new KafkaProducer[String, String](kafkaProducerProps)

  for(index <- 1 to 10){
    //Create producer and record
    val record = new ProducerRecord[String, String]("quickstart-events", s"Hello Kafka from Scala: $index")
    producer.send(record, new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        if( exception == null){ // TODO: not scala style, need change !!!
          //the record was successfully sent
          logger.info("Received new data: \n" +
            "Topic: " + metadata.topic() + "\n" +
            "Partition: " + metadata.partition() + "\n" +
            "Offset: " + metadata.offset() + "\n" +
            "Timestamp: " + metadata.timestamp() + "\n" );
        }else {
          logger.error("Error when receiving data", exception);
        }
      }
    })

  }

  producer.close()
}
