package com.musuala.soft.aliouthiema.entities;

import com.musuala.soft.aliouthiema.enums.Model;
import com.musuala.soft.aliouthiema.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private Model model;

    @Column(name = "weight_limit")
    @Max(value = 500, message = "Weight limit should  be less than 500")
    private Integer weightLimit;

    @Column(name = "battery_capacity")
    private String batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private List<Medication> medications;

}
