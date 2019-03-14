package com.training.springcore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SIMULATED")
public class SimulatedCaptor extends Captor {
    private Integer minPowerInWatt;
    private Integer maxPowerInWatt;


    @Deprecated
    public SimulatedCaptor() {
        super();
// used only by serializer and deserializer
    }

    public SimulatedCaptor(String name, Site site) {
        super(name, site);
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
