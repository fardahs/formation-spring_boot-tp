package com.training.springcore.repository;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Site;

import java.util.List;

public interface CaptorDao extends CrudDao<Site, String> {
    List<Captor> findBySiteId(String siteId);
}
