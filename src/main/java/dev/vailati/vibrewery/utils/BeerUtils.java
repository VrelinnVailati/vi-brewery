package dev.vailati.vibrewery.utils;

import dev.vailati.vibrewery.entities.Beer;
import dev.vailati.vibrewery.model.BeerStyle;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class BeerUtils {
    public static Beer buildPatchObject(Beer original, Beer updated) {
        Beer builtBeer = Beer.builder()
                .id(original.getId())
                .version(original.getVersion())
                .createdDate(original.getCreatedDate())
                .updateDate(original.getUpdateDate())
                .build();

        builtBeer.setBeerName(StringUtils.hasText(updated.getBeerName()) ? updated.getBeerName() : original.getBeerName());
        builtBeer.setBeerStyle(updated.getBeerStyle() != null ? updated.getBeerStyle() : original.getBeerStyle());
        builtBeer.setUpc(StringUtils.hasText(updated.getUpc()) ? updated.getUpc() : original.getUpc());
        builtBeer.setQuantityOnHand(updated.getQuantityOnHand() != null ? updated.getQuantityOnHand() : original.getQuantityOnHand());
        builtBeer.setPrice(updated.getPrice() != null ? updated.getPrice() : original.getPrice());

        return builtBeer;
    }
}
