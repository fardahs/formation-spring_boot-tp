package com.training.springcore.repository;

import com.training.springcore.model.Captor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaptorDao extends JpaRepository<Captor, String>, CaptorCustomDao {
    List<Captor> findBySiteId(String siteId);
    void deleteBySiteId(String siteId);
}
