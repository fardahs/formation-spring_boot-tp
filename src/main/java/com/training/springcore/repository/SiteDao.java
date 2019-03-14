package com.training.springcore.repository;

import com.training.springcore.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteDao extends JpaRepository<Site, String> {

}
