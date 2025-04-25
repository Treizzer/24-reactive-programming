import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ExamplesTest {
    
    @Test
    public void testTransformMap() {
        // Given
        List<String> names = Arrays.asList("google", "abc", "fb", "stackoverflow");

        // When
        Flux<String> fluxNames = Flux.fromIterable(names)
            .filter(name -> name.length() > 5)
            .map(String::toUpperCase)
            .log();

        // Then
        StepVerifier.create(fluxNames)
            .expectNext(
                "GOOGLE", 
                "STACKOVERFLOW"
            )
            .verifyComplete();
    }

    @Test
    public void testTransformFlatMap() {
        // Given
        List<String> names = Arrays.asList("google", "abc", "fb", "stackoverflow");
        
        // When
        Flux<String> fluxNames = Flux.fromIterable(names)
            .filter(name -> name.length() > 5)
            .flatMap(name -> {
                return Mono.just(name.toUpperCase());
            })
            .log();

        // Then
        StepVerifier.create(fluxNames)
            .expectNext(
                "GOOGLE",
                "STACKOVERFLOW"
            )
            .verifyComplete();
    }

    @Test
    public void testMergeFlows() {
        // Given
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie");
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker");

        // When
        Flux<String> fluxMerge = Flux.merge(flux1, flux2).log();

        // Then
        StepVerifier.create(fluxMerge)
            .expectSubscription()
            .expectNext(
                "Blenders", "Old", "Johnnie",
                "Pride", "Monk", "Walker"
            )
            .verifyComplete();
    }

    @Test
    public void testMergeDelayFlows() {
        // Given
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
            .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
            .delayElements(Duration.ofSeconds(1));

        // When
        Flux<String> fluxMerge = Flux.merge(flux1, flux2).log();

        // Then
        StepVerifier.create(fluxMerge)
            .expectSubscription()
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    public void testConcatDelayFlows() {
        // Given
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
            .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
            .delayElements(Duration.ofSeconds(1));

        // When
        Flux<String> fluxConcat = Flux.concat(flux1, flux2).log();

        // Then
        StepVerifier.create(fluxConcat)
            .expectSubscription()
            .expectNext(
                "Blenders", "Old", "Johnnie",
                "Pride", "Monk", "Walker"
            )
            .verifyComplete();
    }

    @Test
    public void testZipDelayFlows() {
        // Given
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
            .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
            .delayElements(Duration.ofSeconds(1));

        // When
        Flux<String> fluxZip = Flux.zip(flux1, flux2, (f1, f2) -> {
            return f1.concat(" ").concat(f2);
        })
            .log();

        // Then
        StepVerifier.create(fluxZip)
            .expectNext("Blenders Pride", "Old Monk", "Johnnie Walker")
            .verifyComplete();
    }

}
