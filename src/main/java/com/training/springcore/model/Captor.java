package com.training.springcore.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Captor {
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
     * Site
     */

    @ManyToOne
    private Site site;


    @Deprecated
    public Captor() {
        // Use for serializer or deserializer
    }



    public Captor(String id, String name,  Site site) {
        this.id = id;
        this.name = name;

        this.site = site;

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



    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Captor site = (Captor) o;
        return Objects.equals(name, site.name) ;
    }


    @Override
    public int hashCode() {

        return Objects.hash(name,  site, id);
    }

    @Override
    public String toString() {
        return "Captor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                '}';
    }

}
