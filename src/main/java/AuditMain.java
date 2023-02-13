import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.javadsl.Sink;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.bson.Document;

public class AuditMain {
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("Auditing");

        ConsumerSettings<String, String> consumerSettings =
                ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
                        .withBootstrapServers("localhost:9092")
                        .withGroupId("audit")
                        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                        .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
                        .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        Consumer.plainSource(consumerSettings,Subscriptions.topics("actions"))
                .mapAsync(5, record -> audit(record.value()))
                .to(Sink.foreach(value -> {System.out.println(value);}))
                .run(system);
    }

    public static MongoClient client = new MongoClient("localhost", 27017);
    public static MongoDatabase database = client.getDatabase("finance_audit");
    public static CompletionStage<String> audit(String actions){
        Document doc = new Document();
        doc.append("Action: ", actions);
        database.getCollection("logs").insertOne(doc);
        return CompletableFuture.completedFuture("Actions has been logged successfully");
    }
}
