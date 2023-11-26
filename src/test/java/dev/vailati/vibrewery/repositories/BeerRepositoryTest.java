package dev.vailati.vibrewery.repositories;

import dev.vailati.vibrewery.entities.Beer;
import dev.vailati.vibrewery.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                        .beerName("New Beer")
                        .price(new BigDecimal("12.99"))
                        .upc("123123123")
                        .beerStyle(BeerStyle.PALE_ALE)
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("New Beer that is definitely too long so I hopefully get it right")
                    .price(new BigDecimal("12.99"))
                    .upc("123123123")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .build());

            beerRepository.flush();
        });
    }
}