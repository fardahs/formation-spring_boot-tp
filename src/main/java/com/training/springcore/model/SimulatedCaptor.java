package com.training.springcore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("SIMULATED")
public class SimulatedCaptor extends Captor {
    @NotNull
    private Integer minPowerInWatt;
    @NotNull
    private Integer maxPowerInWatt;


    @Deprecated
    public SimulatedCaptor() {
        super();
// used only by serializer and deserializer
    }



    @AssertTrue(message = "minPowerInWatt should be less than maxPowerInWatt")
    public boolean isValid(){
        return this.minPowerInWatt <= this.maxPowerInWatt;
    }

    public SimulatedCaptor(String name, Site site, Integer minPowerInWatt, Integer maxPowerInWatt) {
        super(name, site);
        this.minPowerInWatt = minPowerInWatt;
        this.maxPowerInWatt = maxPowerInWatt;
    }


    public Integer getMinPowerInWatt() {
        return minPowerInWatt;
    }

    public void setMinPowerInWatt(Integer minPowerInWatt) {
        this.minPowerInWatt = minPowerInWatt;
    }

    public Integer getMaxPowerInWatt() {
        return maxPowerInWatt;
    }

    public void setMaxPowerInWatt(Integer maxPowerInWatt) {
        this.maxPowerInWatt = maxPowerInWatt;
    }
}
