package com.applesphere.applesphere_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orchards")
public class Orchard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double areInKanal;
    private Integer totalTrees;
    private String location;
    private String village;
    private String district;
    private Integer establishedYear;
    private Double elevation;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getAreInKanal() { return areInKanal; }
    public void setAreInKanal(Double areInKanal) { this.areInKanal = areInKanal; }
    public Integer getTotalTrees() { return totalTrees; }
    public void setTotalTrees(Integer totalTrees) { this.totalTrees = totalTrees; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public Integer getEstablishedYear() { return establishedYear; }
    public void setEstablishedYear(Integer establishedYear) { this.establishedYear = establishedYear; }
    public Double getElevation() { return elevation; }
    public void setElevation(Double elevation) { this.elevation = elevation; }
    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}