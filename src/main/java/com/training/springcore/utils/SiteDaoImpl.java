package com.training.springcore.utils;

import com.training.springcore.model.Site;
import com.training.springcore.repository.SiteDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SiteDaoImpl implements SiteDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public SiteDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        try {
            return jdbcTemplate.queryForObject("select * from SITE where id = :id ",
                    new MapSqlParameterSource("id", s),
                    this::siteMapper);
        }catch (EmptyResultDataAccessException e){
            return null;

        }
    }

    @Override
    public List<Site> findAll() {
        return jdbcTemplate.query("select id, name from SITE",
                this::siteMapper);
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
        jdbcTemplate.update("delete from SITE where id =:id",
                new MapSqlParameterSource("id", s));

    }

    private Site siteMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("name"));
        site.setId(rs.getString("id"));
        return site;
    }

}
