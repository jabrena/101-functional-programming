package info.jab.fp.easyracer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.Future.State;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.function.Function;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Race 2 concurrent requests
 * GET /1
 * The winner returns a 200 response with a body containing right
 */
public class EasyRacer1 {

    private static final Logger logger = LoggerFactory.getLogger(EasyRacer1.class);

    enum ConnectionProblem {
        INVALID_URI,
        INVALID_CONNECTION,
    }

    Function<String, Either<ConnectionProblem, String>> toHTML = param -> {
        try {
            URI uri = new URI(param);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Either.right(response.body());
        } catch (URISyntaxException | IllegalArgumentException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            return Either.left(ConnectionProblem.INVALID_URI);
        } catch (IOException | InterruptedException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            return Either.left(ConnectionProblem.INVALID_CONNECTION);
        }
    };

    public String call(String address) {
        try (StructuredTaskScope scope = new StructuredTaskScope.ShutdownOnSuccess<Either>()) { // Create a StructuredTaskScope
            // Define subtasks using fork()
            Subtask<Either> cs1 = scope.fork(() -> toHTML.apply(address));
            Subtask<Either> cs2 = scope.fork(() -> toHTML.apply(address));

            var result = scope.join();

            result.res
            
            /*
            return Stream.of(cs1, cs2)
                .filter(s -> s.state() == Subtask.State.SUCCESS)
                .map(Subtask::get)
                .filter(Either::isRight)
                .map(Either::get)
                .map(String::valueOf)
                .peek(System.out::println)
                .findAny()
                .get();
                */
        } catch (InterruptedException ex) {
            return "";
        }
    }

}
