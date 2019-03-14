package com.training.springcore.utils;

import com.training.springcore.model.Site;
import com.training.springcore.repository.SiteDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SiteDaoImpl implements SiteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Site findById(String s) {

        try {
            return em.find(Site.class,s);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Site> findAll() {
        return em.createQuery("select s from Site s", Site.class).getResultList();
    }

    @Override
    public void persist(Site site) {
        em.persist(site);
    }

    @Override
    public void delete(Site site) {
        em.remove(site);
    }


}
