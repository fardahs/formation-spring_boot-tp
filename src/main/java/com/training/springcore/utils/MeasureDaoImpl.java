package com.training.springcore.utils;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Measure;
import com.training.springcore.model.Site;
import com.training.springcore.repository.MeasureDao;
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
public class MeasureDaoImpl implements MeasureDao {

    @PersistenceContext
    private EntityManager em;

    private NamedParameterJdbcTemplate jdbcTemplate;
    private H2DateConverter h2DateConverter;

    public MeasureDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, H2DateConverter h2DateConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.h2DateConverter = h2DateConverter;
    }

    private static String SELECT_WITH_JOIN =
            "SELECT m.id, m.instant, m.value_in_watt, m.captor_id, c.name as captor_name, c.site_id, s.name as site_name " +
                    "FROM Measure m inner join Captor c on m.captor_id=c.id inner join site s on c.site_id = s.id ";


    public void create(Measure measure) {
        jdbcTemplate.update("insert into MEASURE (id,instant, value_in_watt, captor_id) values (:id, :instant, :value_in_waTt, :captor_id)",
                new MapSqlParameterSource()
                        .addValue("id", measure.getId())
                        .addValue("instant", measure.getInstant())
                        .addValue("value_in_watt", measure.getValueInWatt())
                        .addValue("captor_id", measure.getCaptor().getId())
        );

    }

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
        return em.createQuery("select m, c, s from Measure m inner join m.captor c inner join c.site s", Measure.class).getResultList();
    }


    public void update(Measure measure) {
        jdbcTemplate.update("update MEASURE set value_in_watt= :value_in_watt where id= :id",
                new MapSqlParameterSource()
                        .addValue("id", measure.getId())
                        .addValue("value_in_watt", measure.getValueInWatt())
        );
    }
    @Override
    public void persist(Measure measure) {
        em.persist(measure);
    }

    public void deleteById(Long aLong) {
        jdbcTemplate.update("delete from MEASURE where id =:id",
                new MapSqlParameterSource("id", aLong));
    }

    @Override
    public void delete(Measure measure) {
        em.remove(measure);
    }

    private Measure measureMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("site_name"));
        site.setId(rs.getString("site_id"));

        Captor captor = new Captor(rs.getString("captor_name"), site);
        captor.setId(rs.getString("captor_id"));

        Measure measure = new Measure(h2DateConverter.convert(rs.getString("instant")),
                rs.getInt("value_in_watt"),
                captor);

        measure.setId(rs.getLong("id"));
        return measure;
    }
}
