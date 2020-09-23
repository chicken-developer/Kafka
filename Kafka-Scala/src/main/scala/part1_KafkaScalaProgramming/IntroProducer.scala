package part1_KafkaScalaProgramming

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object IntroProducer extends App{
  val bootstrapServer = "127.0.0.1:9092"

  //Create properties
  val kafkaProducerProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrapServer)
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)
    props
  }

  //Create producer and record
  val producer = new KafkaProducer[String, String](kafkaProducerProps)
  val record = new ProducerRecord[String, String]("quickstart-events", "Hello Kafka from Scala 2")

  producer.send(record)
  producer.close()
}
