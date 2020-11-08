//package part2_TwitterAnalysis;
//
//import java.util.List;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//
//import com.google.common.collect.Lists;
//import com.twitter.hbc.ClientBuilder;
//import com.twitter.hbc.core.Client;
//import com.twitter.hbc.core.Constants;
//import com.twitter.hbc.core.Hosts;
//import com.twitter.hbc.core.HttpHosts;
//import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
//import com.twitter.hbc.core.processor.StringDelimitedProcessor;
//import com.twitter.hbc.httpclient.auth.Authentication;
//import com.twitter.hbc.httpclient.auth.OAuth1;
//import org.apache.kafka.clients.producer.*;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//
//public class TwitterProducer {
//    public TwitterProducer() { }
//    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());
//    String apikey = "F3A6KC7NOklHfPuzG8X7FigKo";
//    String apikeySecret = "E3prnkfryKq9a3CKFZoZMSIauDgBMVNR1E0GP6mEquR7ffCcuu";
//    String accessToken = "1254194581681262593-XVYZiaOBN3jCDOPfFMsFWTd2CkOMaQ";
//    String accessTokenSecret = "1o8ObLvHmrORdRtwqeA8ctNdfT3nvJPXI73ACxGMJ3y8J";
//    List<String> terms = Lists.newArrayList("trump");
//
//    public static void main(String[] args) {
//        new TwitterProducer().run();
//
//    }
//
//    public void run(){
//        //Create twitter client
//        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
//        Client client = createTwitterClient(msgQueue);
//        client.connect();
//
//        //Create kafka producer
//        KafkaProducer<String, String> kafkaProducer = createKafkaProducer();
//
//        //Add a shutdown hook
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//           logger.info("Stopping application");
//            logger.info("Shutting down client from twitter");
//            client.stop();
//            logger.info("Closing producer");
//            kafkaProducer.close();
//            logger.info("Done !");
//        }));
//
//        //Loop to send tweets to kafka
//        while (!client.isDone()) {
//            String msg = null;
//            try {
//                msg = msgQueue.poll(5, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                client.stop();
//            }
//            if(msg != null){
//                logger.info(msg);
//                kafkaProducer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
//                    @Override
//                    public void onCompletion(RecordMetadata metadata, Exception exception) {
//                        if(exception != null){
//                            logger.error("Have an error: " + exception);
//                        }
//                    }
//                });
//            }
//        }
//        logger.info("End of program ! ");
//    }
//
//
//
//    public Client createTwitterClient(BlockingQueue<String> msgQueue ){
//        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
//        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
//        hosebirdEndpoint.trackTerms(terms);
//
//        // These secrets should be read from a config file
//        Authentication hosebirdAuth = new OAuth1(apikey, apikeySecret, accessToken, accessTokenSecret);
//        ClientBuilder builder = new ClientBuilder()
//                .name("Hosebird-Client-01")
//                .hosts(hosebirdHosts)
//                .authentication(hosebirdAuth)
//                .endpoint(hosebirdEndpoint)
//                .processor(new StringDelimitedProcessor(msgQueue));
//
//        Client hosebirdClient = builder.build();
//        return hosebirdClient;
//    }
//
//    public KafkaProducer<String, String> createKafkaProducer(){
//        String bootstrapServers = "127.0.0.1:9092";
//
//        //Create Producer properties
//        Properties properties = new Properties();
//        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        //Create the producer & producer record
//        return new KafkaProducer<String, String>(properties);
//    }
//}
