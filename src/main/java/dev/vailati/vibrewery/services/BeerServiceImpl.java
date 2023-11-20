package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.model.BeerDTO;
import dev.vailati.vibrewery.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beer) {
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .version(beer.getVersion())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existingBeer = beerMap.get(beerId);

        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(beerId, existingBeer);

        return Optional.of(existingBeer);
    }

    @Override
    public boolean deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);

        return true;
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existingBeer = beerMap.get(beerId);
        boolean beerWasUpdated = false;

        if (StringUtils.hasText(beer.getBeerName())) {
            existingBeer.setBeerName(beer.getBeerName());
            beerWasUpdated = true;
        }

        if (beer.getBeerStyle() != null) {
            existingBeer.setBeerStyle(beer.getBeerStyle());
            beerWasUpdated = true;
        }

        if (beer.getPrice() != null) {
            existingBeer.setPrice(beer.getPrice());
            beerWasUpdated = true;
        }

        if (beer.getQuantityOnHand() != null) {
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
            beerWasUpdated = true;
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existingBeer.setUpc(beer.getUpc());
            beerWasUpdated = true;
        }

        if (beerWasUpdated) {
            existingBeer.setUpdateDate(LocalDateTime.now());
        }

        beerMap.put(beerId, existingBeer);
    }
}
