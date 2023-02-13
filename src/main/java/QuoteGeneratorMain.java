import akka.actor.ActorSystem;
import akka.kafka.ProducerSettings;
import org.apache.kafka.common.serialization.StringSerializer;
import akka.stream.javadsl.Source;
import akka.kafka.javadsl.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.ArrayList;

public class QuoteGeneratorMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("QuoteGenerator");

        ProducerSettings<String, String> producerSettings =
                ProducerSettings.create(system, new StringSerializer(), new StringSerializer())
                        .withBootstrapServers("localhost:9092");

        Company Apple = new Company("Apple", 150);
        Company Microsoft = new Company("Microsoft", 83);
        Company Google = new Company("Google", 123);
        Company Facebook = new Company("Facebook", 173);
        Company Tesla = new Company("Tesla", 393);

        ArrayList<Integer> quotes = new ArrayList<Integer>();

        quotes.add(Apple.getCompanyQuote());
        quotes.add(Microsoft.getCompanyQuote());
        quotes.add(Google.getCompanyQuote());
        quotes.add(Facebook.getCompanyQuote());
        quotes.add(Tesla.getCompanyQuote());

        Source.cycle(() -> {return quotes.iterator();})
                .map(number -> number.toString())
                .map(value -> new ProducerRecord<String, String>("quotes", value))
                .runWith(Producer.plainSink(producerSettings), system);
    }
}
