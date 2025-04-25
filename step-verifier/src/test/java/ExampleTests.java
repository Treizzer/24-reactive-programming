import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ExampleTests {
    
    @Test
    public void testFlux() {
        // Given
        Flux<Integer> fluxToTest = Flux.just(1, 2, 3, 4, 5);

        // Create test
        // When
        StepVerifier.create(fluxToTest)
            .expectNext(1)
            .expectNext(2)
            .expectNext(3)
            .expectNext(4)
            .expectNext(5)
            .expectComplete()
            // Then
            .verify();
    }

    @Test
    public void testFluxString() {
        // Given
        Flux<String> fluxToTest = Flux.just("Hugo", "Paco", "Luis", "Joseline", "Ashley", "Abril", "Isaac", "Sandra")
            .filter(name -> name.length() <= 5)
            .map(String::toUpperCase);

        // When
        StepVerifier.create(fluxToTest)
            .expectNext("HUGO")
            .expectNext("PACO")
            .expectNextMatches(n -> n.startsWith("LU"))
            .expectNext("ABRIL")
            .expectNext("ISAAC")
            .expectComplete()
            // Then
            .verify();
    }

}
