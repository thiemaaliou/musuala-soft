package com.musuala.soft.aliouthiema.services;

import com.musuala.soft.aliouthiema.entities.Drone;
import com.musuala.soft.aliouthiema.entities.Medication;
import com.musuala.soft.aliouthiema.enums.State;
import com.musuala.soft.aliouthiema.helpers.ResponseHelper;
import com.musuala.soft.aliouthiema.repositories.DroneRepository;
import com.musuala.soft.aliouthiema.repositories.MedicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository){
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    public ResponseEntity<Object> registerDrone(Drone drone){
        try{
            return ResponseHelper.generateResponse("", HttpStatus.CREATED, this.droneRepository.save(drone));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> loadMedicationsForDrone(Long droneId, List<Medication> medications){
        try{
            Optional<Drone> drone = droneRepository.findById(droneId);
            if(drone.isPresent()){
                if(!drone.get().getState().equals(State.IDLE)){
                    return ResponseHelper.generateResponse("This drone is not available", HttpStatus.NOT_FOUND, null);
                }
                drone.get().setState(State.LOADING);
                for (Medication m: medications){
                    Medication medication = new Medication();
                    medication.setDrone(drone.get());
                    medication.setName(m.getName());
                    medication.setCode(m.getCode());
                    medication.setWeight(m.getWeight());
                    this.medicationRepository.save(medication);
                }
                drone.get().setState(State.LOADED);
                return ResponseHelper.generateResponse("Medications items loaded successfully", HttpStatus.CREATED, null);

            }else{
                return ResponseHelper.generateResponse("No drone found for given id", HttpStatus.NOT_FOUND, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> findDroneById(Long id){
        try {
            return ResponseHelper.generateResponse("", HttpStatus.OK, this.droneRepository.findById(id));
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }
    }

    public ResponseEntity<Object> findDronesBy(State state){
        try {
            Optional<List<Drone>> drones = this.droneRepository.findAllByState(state);
            if (drones.get().isEmpty()){
                return  ResponseHelper.generateResponse("No drone available found", HttpStatus.OK, null);
            }
            return ResponseHelper.generateResponse("", HttpStatus.OK, drones.get());
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }
    }

    public ResponseEntity<Object> getBatteryCapacity(Long id){
        try {
            Optional<Drone> drone = this.droneRepository.findById(id);
            if (drone.isEmpty()){
                return  ResponseHelper.generateResponse("No drone available found", HttpStatus.NOT_FOUND, null);
            }
            return ResponseHelper.generateResponse("", HttpStatus.OK, drone.get().getBatteryCapacity());
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }
    }

    public ResponseEntity<Object> getLoadedMedications(Long id){
        try {
            Optional<Drone> drone = this.droneRepository.findById(id);
            if (drone.isEmpty()){
                return  ResponseHelper.generateResponse("No drone found for given criteria", HttpStatus.NOT_FOUND, null);
            }
            return ResponseHelper.generateResponse("", HttpStatus.OK, drone.get().getMedications());
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }
    }

}
