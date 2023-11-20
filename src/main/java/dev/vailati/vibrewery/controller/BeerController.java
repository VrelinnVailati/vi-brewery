package dev.vailati.vibrewery.controller;

import dev.vailati.vibrewery.model.BeerDTO;
import dev.vailati.vibrewery.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<Void> patchBeerById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        if(beerService.patchBeerById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Void> deleteBeerById(@PathVariable UUID beerId) {
        if(!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Void> updateBeerById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        if(beerService.updateBeerById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.saveBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId());

        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable UUID beerId) {
        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
