import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.ProducerSettings;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import akka.kafka.javadsl.Consumer;
import akka.kafka.Subscriptions;
import org.apache.kafka.common.serialization.StringSerializer;
import akka.kafka.javadsl.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
public class TradingMain {
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("Trading");

        ConsumerSettings<String, String> consumerSettings =
                ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
                        .withBootstrapServers("localhost:9092")
                        .withGroupId("trader")
                        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                        .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
                        .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        ProducerSettings<String, String> producerSettings =
                ProducerSettings.create(system, new StringSerializer(), new StringSerializer())
                        .withBootstrapServers("localhost:9092");

        Trader fares = new Trader(1, "fares", 6500);


        Consumer.plainSource(consumerSettings,Subscriptions.topics("quotes"))
                .mapAsync(5, record -> tradingAction(fares, record.value()))
                .map(value -> new ProducerRecord<String, String>("actions", value))
                .runWith(Producer.plainSink(producerSettings), system);


    }

    public static CompletionStage<String>  tradingAction(Trader trader, String quote) {
        String action = trader.tradingAction(quote);
        return CompletableFuture.completedFuture(trader.getTraderName() + " did " + action + " for " + quote + " and their total credit is " + trader.getCredit());
    }
}
