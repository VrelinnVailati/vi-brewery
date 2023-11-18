package dev.vailati.vibrewery.mappers;

import dev.vailati.vibrewery.entities.Beer;
import dev.vailati.vibrewery.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
