package com.training.springcore.utils;

import com.training.springcore.model.Measure;
import com.training.springcore.repository.MeasureDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MeasureDaoImpl implements MeasureDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Measure findById(Long aLong) {
        try {
           return em.find(Measure.class, aLong);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Measure> findAll() {
        return em.createQuery("select m from Measure m inner join m.captor c inner join c.site s", Measure.class).getResultList();
    }

    @Override
    public void persist(Measure measure) {
        em.persist(measure);
    }


    @Override
    public void delete(Measure measure) {
        // Suppression prenant en compte les foreignekey
        em.remove(measure);
    }

}
