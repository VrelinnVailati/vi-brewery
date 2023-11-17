package dev.vailati.vibrewery.controller;

import dev.vailati.vibrewery.model.Beer;
import dev.vailati.vibrewery.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {
    static final String BEER_PATH = "/api/v1/beer";
    static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Void> patchBeerById(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Void> deleteBeerById(@PathVariable UUID beerId) {
        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Void> updateBeerById(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId());

        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public Beer getBeerById(@PathVariable UUID beerId) {
        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId);
    }
}
