package com.musuala.soft.aliouthiema.repositories;

import com.musuala.soft.aliouthiema.entities.Drone;
import com.musuala.soft.aliouthiema.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    Optional<Drone> findByState(State state);

    Optional<List<Drone>> findAllByState(State state);
}
