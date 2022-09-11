package com.musuala.soft.aliouthiema.services;

import com.musuala.soft.aliouthiema.entities.Drone;
import com.musuala.soft.aliouthiema.entities.Medication;
import com.musuala.soft.aliouthiema.enums.State;
import com.musuala.soft.aliouthiema.helpers.FileStorageProperties;
import com.musuala.soft.aliouthiema.helpers.ResponseHelper;
import com.musuala.soft.aliouthiema.repositories.DroneRepository;
import com.musuala.soft.aliouthiema.repositories.MedicationRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    @Value("${file.upload.dir}")
    private String upladDir;

    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository) throws Exception {
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
                    if(!m.getImage().isEmpty()){
                        medication.setImage(uploadImage(m.getImage(), m.getName().replaceAll("\\s", "").toLowerCase()));
                    }
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

    public String uploadImage(String image, String imageName){
        try{
            File file = new File(this.upladDir);
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory already exist ");
            }

            String img = URLDecoder.decode(image, "UTF-8");
            img = img.replace("\n", "");
            byte[] imageByte = Base64.decodeBase64(img);
            /**
             * Need refractor
             * Image must be File
             * Extension must be dynamique
             * Name must improved
             * checking directory must be a part of service
             */
            String extension = image.indexOf("png") != -1 ? ".png" : ".jpg";
            String fileUri = upladDir.concat("/"+imageName+extension);
            new FileOutputStream(fileUri).write(imageByte);

            return fileUri;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
