package com.training.springcore.service;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Site;
import com.training.springcore.repository.CaptorDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CaptorDaoImpl implements CaptorDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static String SELECT_WITH_JOIN =
            "SELECT c.id, c.name, c.site_id, s.name as site_name " +
                    "FROM Captor c inner join Site s on c.site_id = s.id ";
    @Override
    public List<Captor> findBySiteId(String siteId) {
        return null;
    }

    @Override
    public void create(Site site) {
        jdbcTemplate.update("insert into SITE (id, name) values (:id, :name)",
                new MapSqlParameterSource()
                        .addValue("id", site.getId())
                        .addValue("name", site.getName()));
    }

    @Override
    public Site findById(String s) {
        return null;
    }

    @Override
    public List<Site> findAll() {
        return null;
    }

    @Override
    public void update(Site site) {
        jdbcTemplate.update("update SITE set name = :name where id =:id",
                new MapSqlParameterSource()
                        .addValue("id", site.getId())
                        .addValue("name", site.getName()));
    }

    @Override
    public void deleteById(String s) {

    }
}
