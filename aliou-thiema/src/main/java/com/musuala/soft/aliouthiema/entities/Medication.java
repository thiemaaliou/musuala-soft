package com.musuala.soft.aliouthiema.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Pattern(regexp = "/^[ A-Za-z0-9_-]*$/", message = "Only letters, numbers, - and _ are allowed")
    private String name;

    @Column(name = "weight")
    private String weight;

    @Column(name = "code")
    @Pattern(regexp = "/^[ A-Z0-9_]*$/", message = "Only uppercase letters, numbers and _ are allowed")
    private String code;

    @Column(name = "image")
    private String image;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;
}
