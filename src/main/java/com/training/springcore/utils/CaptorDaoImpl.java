package com.training.springcore.utils;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Site;
import com.training.springcore.repository.CaptorDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CaptorDaoImpl implements CaptorDao {
    @PersistenceContext
    private EntityManager em;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public CaptorDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String SELECT_WITH_JOIN =
            "SELECT c.id, c.name, c.site_id, s.name as site_name " +
                    "FROM Captor c inner join Site s on c.site_id = s.id ";

    @Override
    public List<Captor> findByCaptorId(String captorId) {

        return em.createQuery("select c from Captor c inner join c.site s where s.id =:siteId", Captor.class)
                .setParameter("siteId", captorId)
                .getResultList();

    }


    public void create(Captor captor) {
        jdbcTemplate.update("insert into CAPTOR (id, name, site_id) values (:id, :name, :site_id)",
                new MapSqlParameterSource()
                        .addValue("id", captor.getId())
                        .addValue("name", captor.getName())
                        .addValue("site_id", captor.getSite().getId()));
    }


    @Override
    public void persist(Captor captor) {
        em.persist(captor);
    }



    @Override
    public Captor findById(String s) {

        try {
            return em.find(Captor.class, s);
        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public List<Captor> findAll() {

        return em.createQuery("select c from Captor c inner join c.site s",
                Captor.class)
                .getResultList();
    }

    @Override
    public void delete(Captor captor) {
        em.remove(captor);

    }


    public void update(Captor captor) {
        jdbcTemplate.update("update CAPTOR set name = :name where id =:id",
                new MapSqlParameterSource()
                        .addValue("id", captor.getId())
                        .addValue("name", captor.getName()));
    }


    public void deleteById(String s) {
        jdbcTemplate.update("delete from CAPTOR where id =:id",
                new MapSqlParameterSource("id", s));
    }

    private Captor captorMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("name"));
        site.setId(rs.getString("id"));

        Captor captor = new Captor(rs.getString("name"), site);
        captor.setId(rs.getString("id"));

        return captor;
    }
}
