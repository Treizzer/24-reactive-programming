import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ExercisesTest {

    @Test
    public void testMergeWith() {
        // Given
        // When

        // Then
        StepVerifier.create(returnMerge())
            .expectNext("a")
            .expectNext("c")
            .expectNext("b")
            .expectNext("d")
            .expectComplete()
            .verify();
    }

    private static Flux<String> returnMerge() {
        Flux<String> flux1 = Flux.fromArray(new String[] {"a", "b"})
            .delayElements(Duration.ofMillis(100));

        Flux<String> flux2 = Flux.fromArray(new String[] {"c", "d"})
            .delayElements(Duration.ofMillis(125));

        return flux1.mergeWith(flux2);
    }
    
}
