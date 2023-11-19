package dev.vailati.vibrewery.bootstrap;

import dev.vailati.vibrewery.repositories.BeerRepository;
import dev.vailati.vibrewery.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BootstrapDataTest {
    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository);
    }

    @Test
    void testBootstrapData() {
        bootstrapData.run((String) null);

        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(2);
    }
}