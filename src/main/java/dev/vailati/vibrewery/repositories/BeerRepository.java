package dev.vailati.vibrewery.repositories;

import dev.vailati.vibrewery.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
