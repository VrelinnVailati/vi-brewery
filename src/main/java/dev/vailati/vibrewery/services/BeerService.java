package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
