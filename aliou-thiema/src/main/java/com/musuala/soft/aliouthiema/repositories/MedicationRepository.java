package com.musuala.soft.aliouthiema.repositories;

import com.musuala.soft.aliouthiema.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
