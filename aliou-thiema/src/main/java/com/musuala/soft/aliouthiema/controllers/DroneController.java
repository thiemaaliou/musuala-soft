package com.musuala.soft.aliouthiema.controllers;

import com.musuala.soft.aliouthiema.entities.Drone;
import com.musuala.soft.aliouthiema.entities.Medication;
import com.musuala.soft.aliouthiema.enums.State;
import com.musuala.soft.aliouthiema.services.DroneService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/drone")
public class DroneController {
    private final DroneService droneService;
    public DroneController(DroneService droneService){
        this.droneService = droneService;
    }

    @PostMapping(path = "register")
    public HttpEntity<Object> saveDrone(@RequestBody Drone drone){
        return this.droneService.registerDrone(drone);
    }

    @PostMapping(path = "load/medication/{droneId}")
    public HttpEntity<Object> loadMedicationsForDrone(@PathVariable Long droneId, @RequestBody List<Medication> medications){
        return this.droneService.loadMedicationsForDrone(droneId, medications);
    }

    @GetMapping(path = "findby/{state}")
    public HttpEntity<Object> getAvailableDrones(@PathVariable State state){
        return this.droneService.findDronesBy(state);
    }

    @GetMapping(path = "battery/level/{id}")
    public HttpEntity<Object> getBatteryLevel(@PathVariable Long id){
        return this.droneService.getBatteryCapacity(id);
    }

    @GetMapping(path = "medications/{id}")
    public HttpEntity<Object> getMedications(@PathVariable Long id){
        return this.droneService.getLoadedMedications(id);
    }

    @PostMapping(path = "upload/images")
    public  HttpEntity<Object> uploadImages(@RequestParam("images") MultipartFile images){

    }

}
