package com.training.springcore.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Captor {
    /**
     * Captor id
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Captor name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Power source
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PowerSource powerSource;

    /**
     * Site
     */

    @ManyToOne
    private Site site;

    private Integer defaultPowerInWatt;

    @Deprecated
    public Captor() {
        // Use for serializer or deserializer
    }

    /**
     * Constructor to use with required property
     * @param name, power source
     */
    public Captor(String name, PowerSource powerSource) {
        this.name = name;
        this.powerSource = powerSource;
        this.site = site;

    }

    public Captor(String id, String name, PowerSource powerSource, Site site, Integer defaultPowerInWatt) {
        this.id = id;
        this.name = name;
        this.powerSource = powerSource;
        this.site = site;
        this.defaultPowerInWatt = defaultPowerInWatt;
    }


    /**
     * Constructor to use with required property
     * @param name
     * @param site
     */
    public Captor(String name, Site site) {
        this.name = name;
        this.site = site;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PowerSource getPowerSource() {
        return powerSource;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Integer getDefaultPowerInWatt() {
        return defaultPowerInWatt;
    }

    public void setDefaultPowerInWatt(Integer defaultPowerInWatt) {
        this.defaultPowerInWatt = defaultPowerInWatt;
    }

    public void setPowerSource(PowerSource powerSource) {
        this.powerSource = powerSource;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Captor site = (Captor) o;
        return Objects.equals(name, site.name) && Objects.equals(powerSource, site.powerSource);
    }


    @Override
    public int hashCode() {

        return Objects.hash(name, powerSource, defaultPowerInWatt, site, id);
    }

    @Override
    public String toString() {
        return "Captor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", powerSource='" + powerSource + '\'' +
                ", defaultPowerInWatt='" + defaultPowerInWatt + '\'' +
                ", site='" + site + '\'' +
                '}';
    }

}
